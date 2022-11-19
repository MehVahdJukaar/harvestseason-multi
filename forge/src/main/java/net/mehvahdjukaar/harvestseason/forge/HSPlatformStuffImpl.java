package net.mehvahdjukaar.harvestseason.forge;

import net.mehvahdjukaar.harvestseason.blocks.ModCarvedPumpkinBlockTile;
import net.mehvahdjukaar.harvestseason.reg.ModRegistry;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolActions;

public class HSPlatformStuffImpl {

    public static boolean isTopCarver(ItemStack stack) {
        return stack.canPerformAction(ToolActions.SHEARS_CARVE);
    }

    public static void addPumpkinData(ModCarvedPumpkinBlockTile tile, SnowGolem snowGolem) {
        if (snowGolem instanceof ICustomPumpkinHolder customPumpkinHolder) {
            var s = tile.getItemWithNBT();
            ItemStack stack = ModRegistry.MOD_CARVED_PUMPKIN_ITEM.get().getDefaultInstance();
            stack.setTag(s.getTag());
            customPumpkinHolder.setCustomPumpkin(stack);
        }
    }

    public static ShaderInstance getBlur() {
        return HarvestSeasonForgeClient.getBlur();
    }
}
