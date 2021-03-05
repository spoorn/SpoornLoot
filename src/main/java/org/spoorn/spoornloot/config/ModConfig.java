package org.spoorn.spoornloot.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import org.spoorn.spoornloot.util.SpoornUtil;

@Config(name = SpoornUtil.MODID)
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.CollapsibleObject
    public ServerConfig serverConfig = new ServerConfig();

    public static void init() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
    }

    public static ModConfig get() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}
