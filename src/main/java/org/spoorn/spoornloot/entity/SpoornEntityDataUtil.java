package org.spoorn.spoornloot.entity;

import lombok.extern.log4j.Log4j2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.PacketByteBuf;
import org.apache.commons.lang3.SerializationUtils;
import org.spoorn.spoornloot.mixin.DataTrackerAccessorMixin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Vanilla Minecraft entity data tracking is heavily tied to inheritance...
 *
 * Example:
 *  CowEntity is a LivingEntity
 *  when generating the entity TrackedData for CowEntity, there are TrackedData for LivingEntity too which
 *  are generated first because TrackedData is registered statically and Java will load static fields
 *  in an order following inheritance where subclasses are loaded before superclasses.
 *  Which is usually fine but for TrackedData, subclass TrackedData is index appended onto subclass index:
 *
 *  LivingEntity:
 *      1. Health
 *      2. Damage
 *      3. Lifespan
 *  CowEntity:
 *      4. Color
 *      5. Milkdata
 *
 * This means if I want to access the TrackedData of a CowEntity, everything is fine.  But if I want to
 * add more data to a subclass (i.e. LivingEntity), then by default it will try index appending to the
 * subclass (e.g. LivingEntity 4. NewLivingEntityData).  This will cause a conflict for CowEntity since
 * index 4 now has multiple entries and the game crashes.
 *
 * This is fucking stupid.
 *
 * To get around it, I create my own structs for TrackedData and extend on the vanilla system, using helpers
 * to seamlessly register and access my own custom data at runtime, registered at the correct indices
 * to avoid conflicting with any superclasses.
 *
 * It's also extra annoying because vanilla Minecraft limits data to 255 entries, so once we hit that we're
 * fucked and the game will crash.  Until then, pray and hope
 */
@Log4j2
public class SpoornEntityDataUtil {

    // Our own TrackedData registry, extension of the vanilla one in DataTracker
    public static Map<Class<? extends Entity>, TrackedData<Optional<SpoornTrackedData>>> SPOORN_TRACKED_ENTITY_DATA
            = new HashMap<>();

    public static void init() {
        // Do nothing but this is required because mod loader stupid and needs explicit code entrypoints to load for
        // client vs server
    }

    /**
     * Registers the new SpoornTrackedData for an Entity type.  This registers the data in my own Map above instead of
     * building on top of the vanilla one since vanilla code sucks.
     */
    public static TrackedData<Optional<SpoornTrackedData>> registerSpoornEntityTrackedData(Entity entity) {
        Class<? extends Entity> entityClass = entity.getClass();

        // Keep track of [Entity -> LastDataTrackerIndex] so that we can append onto an entity's data tracker
        // at runtime with a non-conflicting TrackedData Id.
        if (!SPOORN_TRACKED_ENTITY_DATA.containsKey(entityClass)) {
            //log.info("not in spoorn entity map");
            // Get the last TrackedData index of the LivingEntity's class
            Integer lastTrackedDataIndex = DataTrackerAccessorMixin.getTrackedEntities().get(entityClass);
            //log.info(entityClass.getName());
            //log.info("last tracked data index: " + lastTrackedDataIndex);

            // If entity doesn't have tracked data, check its superclasses.  This is the same logic as in DataTracker
            if (lastTrackedDataIndex == null) {
                int j = 0;
                Class class2 = entityClass;

                while (class2 != Entity.class) {
                    class2 = class2.getSuperclass();
                    if (DataTrackerAccessorMixin.getTrackedEntities().containsKey(class2)) {
                        j = (Integer)  DataTrackerAccessorMixin.getTrackedEntities().get(class2) + 1;
                        break;
                    }
                }
                lastTrackedDataIndex = j;
                //log.info("updated lastTrackedDataIndex: " + lastTrackedDataIndex);
            }

            // At runtime, dynamically create this mod's custom TrackedData with an incremented Id based off the
            // last TrackedData index.  Cache this in a map to be accessed later and avoid recreation of
            // the TrackedData type.  This is just the TYPE of the TrackedData, not the value.
            if (lastTrackedDataIndex != null) {
                TrackedData<Optional<SpoornTrackedData>> spoornTrackedData =
                        OPTIONAL_SPOORN_TRACKED_DATA.create(lastTrackedDataIndex + 1);
                SPOORN_TRACKED_ENTITY_DATA.put(entityClass, spoornTrackedData);
                return spoornTrackedData;
            }
        }

        return null;
    }

    /**
     * If entity's data tracker isn't tracking SpoornTrackedData, start tracking.  If SpoornTrackedData is not yet
     * registered, register it here as a lazy init.
     */
    public static TrackedData<Optional<SpoornTrackedData>> startTrackingSpoornTrackedDataIfNotTracking(Entity entity) {
        Class<? extends Entity> entityClass = entity.getClass();

        // Lazy register SpoornTrackedData
        if (!SPOORN_TRACKED_ENTITY_DATA.containsKey(entityClass)) {
            registerSpoornEntityTrackedData(entity);
            //log.info("is in spoorn entity map");
        }

        TrackedData<Optional<SpoornTrackedData>> trackedData = SPOORN_TRACKED_ENTITY_DATA.get(entityClass);

        // If entity is not already tracking SpoornTrackedData,
        // officially track it in the entity's DataTracker so it can be de/serialized naturally in the vanilla code.
        if (!entityDataTrackerHasSpoornData(entity)) {
            //log.info("not in data tracker entries");
            entity.getDataTracker().startTracking(trackedData, Optional.empty());
        }

        return trackedData;
    }

    /**
     * Checks if SpoornTrackedData is in the entity's data tracker.  Assumes SpoornTrackedData is registered for this
     * entity already.  This is sort of like a validation for startTrackingSpoornTrackedDataIfNotTracking()
     */
    public static boolean entityDataTrackerHasSpoornData(Entity entity) {
        Map<Integer, DataTracker.Entry<?>> dataTrackerEntries =
                ((DataTrackerAccessorMixin)entity.getDataTracker()).getEntries();
        return dataTrackerEntries.containsKey(SPOORN_TRACKED_ENTITY_DATA.get(entity.getClass()).getId());
    }

    /**
     * If the SpoornTrackedData is already registered and has started tracking on entity,
     * fetch it from the entity data tracker.  Create a new clean SpoornTrackedData if entity is tracking
     * it, but is empty, so caller of this method can start populating.
     */
    public static Optional<SpoornTrackedData> getSpoornTrackedDataOrCreate(Entity entity) {
        if (containsTrackedData(entity.getClass())) {
            TrackedData<Optional<SpoornTrackedData>> trackedData
                    = getTrackedData(entity.getClass());
            Optional<SpoornTrackedData> spoornTrackedData = Optional.empty();
            if (entityDataTrackerHasSpoornData(entity)) {
                spoornTrackedData = entity.getDataTracker().get(trackedData);
            }

            if (!spoornTrackedData.isPresent()) {
                spoornTrackedData = Optional.of(new SpoornTrackedData());
            }

            return spoornTrackedData;
        }

        return Optional.empty();
    }

    /**
     * Helper to set SpoornTrackedData on an entity.  Assumes the entity already contains a previous SpoornTrackedData.
     */
    public static void setSpoornTrackedDataOnEntity(TrackedData<Optional<SpoornTrackedData>> trackedData,
                                                    Optional<SpoornTrackedData> spoornTrackedData, Entity entity) {
        entity.getDataTracker().set(trackedData, spoornTrackedData);
    }

    // Helper method for accessing our SpoornTrackedData registry
    public static boolean containsTrackedData(Class<? extends Entity> entityClass) {
        return SPOORN_TRACKED_ENTITY_DATA.containsKey(entityClass);
    }

    // Helper method for accessing our SpoornTrackedData registry
    public static TrackedData<Optional<SpoornTrackedData>> getTrackedData(Class<? extends Entity> entityClass) {
        return SPOORN_TRACKED_ENTITY_DATA.get(entityClass);
    }

    // Our TrackedDataHandler with our custom data object
    private static final TrackedDataHandler<Optional<SpoornTrackedData>> OPTIONAL_SPOORN_TRACKED_DATA =
            new TrackedDataHandler<Optional<SpoornTrackedData>>() {
                public void write(PacketByteBuf packetByteBuf, Optional<SpoornTrackedData> optional) {
                    packetByteBuf.writeBoolean(optional.isPresent());
                    if (optional.isPresent()) {
                        packetByteBuf.writeBytes(SerializationUtils.serialize(optional.get()));
                    }

                }

                public Optional<SpoornTrackedData> read(PacketByteBuf packetByteBuf) {
                    return !packetByteBuf.readBoolean() ? Optional.empty()
                            : Optional.of(SerializationUtils.deserialize(packetByteBuf.readByteArray()));
                }

                public Optional<SpoornTrackedData> copy(Optional<SpoornTrackedData> optional) {
                    return optional;
                }
            };

    // Register our data handler
    static {
        TrackedDataHandlerRegistry.register(OPTIONAL_SPOORN_TRACKED_DATA);
    }
}
