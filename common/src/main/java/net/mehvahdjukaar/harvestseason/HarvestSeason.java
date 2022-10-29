package net.mehvahdjukaar.harvestseason;

import net.mehvahdjukaar.harvestseason.network.NetworkHandler;
import net.mehvahdjukaar.harvestseason.reg.ModConfigs;
import net.mehvahdjukaar.harvestseason.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author: MehVahdJukaar
 */
public class HarvestSeason {

    public static final String MOD_ID = "harvestseason";

    public static ResourceLocation res(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static final Logger LOGGER = LogManager.getLogger();

    public static boolean SEASON_MOD_INSTALLED = PlatformHelper.isModLoaded(PlatformHelper.getPlatform().isForge() ? "sereneseasons" : "seasons");


    public static void commonInit() {
        ModConfigs.earlyLoad();

        ModRegistry.init();
        NetworkHandler.registerMessages();
    }

    //needs to be fired after configs are loaded
    public static void commonSetup() {

    }


    public static void onConfigReload() {


    }

}
