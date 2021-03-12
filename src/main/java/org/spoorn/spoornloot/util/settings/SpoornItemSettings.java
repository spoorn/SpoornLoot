package org.spoorn.spoornloot.util.settings;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import org.spoorn.spoornloot.util.rarity.SpoornRarity;

/**
 * Item settings for Spoorn items.
 */
public class SpoornItemSettings extends FabricItemSettings {

    private SpoornRarity spoornRarity;

    public SpoornItemSettings spoornRarity(SpoornRarity spoornRarity) {
        this.spoornRarity = spoornRarity;
        return this;
    }
}
