package org.spoorn.spoornloot;

import lombok.extern.log4j.Log4j2;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.util.Identifier;
import org.spoorn.spoornloot.config.ModConfig;
import org.spoorn.spoornloot.item.swords.SwordRegistry;

@Log4j2
public class SpoornLoot implements ModInitializer {

    @Override
    public void onInitialize() {
        log.info("Hello mod from SpoornLoot!");
        ModConfig.init();
        SwordRegistry.init();
    }
}
