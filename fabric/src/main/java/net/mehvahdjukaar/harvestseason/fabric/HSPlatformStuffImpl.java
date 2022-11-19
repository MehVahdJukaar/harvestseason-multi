package net.mehvahdjukaar.harvestseason.fabric;

import net.mehvahdjukaar.harvestseason.HarvestSeason;
import net.mehvahdjukaar.harvestseason.blocks.ModCarvedPumpkinBlockTile;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.SwordItem;

public class HSPlatformStuffImpl {
    public static boolean isTopCarver(ItemStack stack) {
        return stack.getItem() instanceof ShearsItem || (!(stack.getItem() instanceof SwordItem) && stack.is(HarvestSeason.CARVERS));
    }

    public static void addPumpkinData(ModCarvedPumpkinBlockTile tile, SnowGolem snowGolem) {
    }

    public static ShaderInstance getBlur() {
        return null;
    }
}
