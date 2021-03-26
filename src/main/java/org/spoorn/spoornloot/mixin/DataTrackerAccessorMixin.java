package org.spoorn.spoornloot.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(DataTracker.class)
public interface DataTrackerAccessorMixin {

    @Accessor(value = "TRACKED_ENTITIES")
    public static Map<Class<? extends Entity>, Integer> getTrackedEntities() {
        throw new NotImplementedException("DataTrackerAccessorMixin in SpoornLoot failed to get tracked entities");
    };

    @Accessor(value = "entries")
    Map<Integer, DataTracker.Entry<?>> getEntries();
}
