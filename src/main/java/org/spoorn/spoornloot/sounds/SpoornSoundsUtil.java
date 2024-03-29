package org.spoorn.spoornloot.sounds;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spoorn.spoornloot.util.SpoornUtil;

/**
 * Custom sounds.
 */
public class SpoornSoundsUtil {

    public static final Identifier SPOORN_SWORD_HIT_CHAR_ID = new Identifier(SpoornUtil.MODID, "spoorn_sword_hit_char");
    public static final Identifier SPOORN_SWORD_HIT_ENV_ID = new Identifier(SpoornUtil.MODID, "spoorn_sword_hit_env");
    public static final Identifier SM_WAND_ID = new Identifier(SpoornUtil.MODID, "sm_wand");
    public static final Identifier SM_THEME_ID = new Identifier(SpoornUtil.MODID, "sm_theme");

    public static final SoundEvent SPOORN_SWORD_HIT_CHAR_SOUND = registerSoundEvent(SPOORN_SWORD_HIT_CHAR_ID, new SoundEvent(SPOORN_SWORD_HIT_CHAR_ID));
    public static final SoundEvent SPOORN_SWORD_HIT_ENV_SOUND = registerSoundEvent(SPOORN_SWORD_HIT_ENV_ID, new SoundEvent(SPOORN_SWORD_HIT_ENV_ID));
    public static final SoundEvent SM_WAND_SOUND = registerSoundEvent(SM_WAND_ID, new SoundEvent(SM_WAND_ID));
    public static final SoundEvent SM_THEME_SOUND = registerSoundEvent(SM_THEME_ID, new SoundEvent(SM_THEME_ID));

    public static void init() {
        // Do nothing but this is required because mod loader stupid and needs explicit code entrypoints to load for
        // client vs server
    }

    private static SoundEvent registerSoundEvent(Identifier identifier, SoundEvent soundEvent) {
        return Registry.register(Registry.SOUND_EVENT, identifier, soundEvent);
    }
}
