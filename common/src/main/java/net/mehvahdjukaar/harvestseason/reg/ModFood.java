package net.mehvahdjukaar.harvestseason.reg;

import net.minecraft.world.food.FoodProperties;

public class ModFood {

    public static final FoodProperties POPCORN = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0.4f).fast().alwaysEat().build();

    public static final FoodProperties CANDY_CORN = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0.2f).fast().alwaysEat().build();

    public static final FoodProperties CORNBREAD = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.6f).build();

    public static final FoodProperties CORN_ON_THE_COB = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.9f).build();

}
