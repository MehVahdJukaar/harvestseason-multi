package net.mehvahdjukaar.harvestseason.reg;

import net.mehvahdjukaar.harvestseason.HarvestSeason;
import net.mehvahdjukaar.harvestseason.blocks.ModCarvedPumpkinBlock;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigSpec;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;

import java.util.function.Supplier;

public class ModConfigs {

    public static Supplier<ModCarvedPumpkinBlock.CarveMode> PUMPKIN_CARVE_MODE;
    public static Supplier<ModCarvedPumpkinBlock.CarveMode> JACK_O_LANTERN_CARVE_MODE;
    public static Supplier<ModCarvedPumpkinBlock.CarveMode> CARVE_MODE;


    public static ConfigSpec SPEC;

    public static void earlyLoad() {
        ConfigBuilder builder = ConfigBuilder.create(HarvestSeason.res("common"), ConfigType.COMMON);

        builder.push("pumpkin_carving");
        PUMPKIN_CARVE_MODE = builder.comment("Pumpkin carving mode")
                .define("pumpkin_carve_mode", ModCarvedPumpkinBlock.CarveMode.BOTH);
        JACK_O_LANTERN_CARVE_MODE = builder.comment("Jack o Lantern carving mode")
                .define("jack_o_lantern_carve_mode", ModCarvedPumpkinBlock.CarveMode.NONE);

        builder.pop();


        builder.onChange(HarvestSeason::onConfigReload);

        SPEC = builder.build();

        //load early
        SPEC.loadFromFile();
    }


}
