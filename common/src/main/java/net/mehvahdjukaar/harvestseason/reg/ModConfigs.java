package net.mehvahdjukaar.harvestseason.reg;

import net.mehvahdjukaar.harvestseason.HarvestSeason;
import net.mehvahdjukaar.harvestseason.blocks.ModCarvedPumpkinBlock;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigSpec;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;

import java.util.function.Supplier;

public class ModConfigs {

    public static Supplier<ModCarvedPumpkinBlock.UseMode> CARVE_MODE;


    public static ConfigSpec SPEC;

    public static void earlyLoad() {
        ConfigBuilder builder = ConfigBuilder.create(HarvestSeason.res("common"), ConfigType.COMMON);

        builder.push("pumpkin_carving");
        CARVE_MODE = builder.comment("Pumpkin carving mode")
                .define("carve_mode", ModCarvedPumpkinBlock.UseMode.BOTH);

        builder.pop();


        builder.onChange(HarvestSeason::onConfigReload);

        SPEC = builder.build();

        //load early
        SPEC.loadFromFile();
    }


}
