package net.mehvahdjukaar.harvestseason.reg;

import net.minecraft.world.food.FoodProperties;

public class ModFood {

    public static final FoodProperties POPCORN = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0.8F / 2f).fast().alwaysEat().build();

    public static final FoodProperties CANDY_CORN = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0.4F / 2f).fast().alwaysEat().build();

    public static final FoodProperties CORNBREAD = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(7.2f / 2f).build();

    public static final FoodProperties CORN_ON_THE_COB = (new FoodProperties.Builder())
            .nutrition(7).saturationMod(14.4f / 2f).build();

}
