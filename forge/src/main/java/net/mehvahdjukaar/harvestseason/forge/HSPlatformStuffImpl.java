package net.mehvahdjukaar.harvestseason.forge;

import net.mehvahdjukaar.harvestseason.blocks.ModCarvedPumpkinBlockTile;
import net.minecraft.client.renderer.entity.layers.SnowGolemHeadLayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolActions;

public class HSPlatformStuffImpl {

    public static boolean isTopCarver(ItemStack stack) {
        return stack.canPerformAction(ToolActions.SHEARS_CARVE);
    }

    public static void addPumpkinData(ModCarvedPumpkinBlockTile tile, SnowGolem snowGolem) {
        var c = snowGolem.getPersistentData();
        c.put("pumpkin", tile.savePixels(new CompoundTag()));
    }
}
