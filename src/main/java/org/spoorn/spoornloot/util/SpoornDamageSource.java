package org.spoorn.spoornloot.util;

import net.minecraft.entity.damage.DamageSource;

/**
 * Damage specifically from a Spoorn sword.
 */
public class SpoornDamageSource extends DamageSource {

    protected SpoornDamageSource(String name) {
        super(name);
        this.setExplosive();
    }
}
