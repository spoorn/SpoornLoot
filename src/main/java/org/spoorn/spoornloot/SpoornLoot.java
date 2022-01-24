package org.spoorn.spoornloot;

import lombok.extern.log4j.Log4j2;
import net.fabricmc.api.ModInitializer;
import org.spoorn.spoornloot.config.ModConfig;
import org.spoorn.spoornloot.entity.SpoornEntityDataUtil;
import org.spoorn.spoornloot.item.swords.SwordRegistry;
import org.spoorn.spoornloot.sounds.SpoornSoundsUtil;

@Log4j2
public class SpoornLoot implements ModInitializer {

    // For future reference, for classes that have static fields only, they MUST be referenced in these entrypoints
    // otherwise it won't load on an actual multiplayer server.
    @Override
    public void onInitialize() {
        log.info("Hello mod from SpoornLoot!");

        // Config
        ModConfig.init();

        // Swords
        SwordRegistry.init();

        // Sounds
        SpoornSoundsUtil.init();

        // Entity data
        SpoornEntityDataUtil.init();
    }
}
