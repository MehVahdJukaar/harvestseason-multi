package net.mehvahdjukaar.harvestseason.mixins.forge;

import net.mehvahdjukaar.harvestseason.blocks.ModCarvedPumpkinBlock;
import net.minecraft.client.renderer.entity.layers.SnowGolemHeadLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowGolemHeadLayer.class)
public abstract class SnowGolemHeadLayerMixin {

    @ModifyVariable(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/SnowGolem;FFFFFF)V",
            at = @At(value = "NEW", target = "Lnet/minecraft/world/item/ItemStack;<init>(Lnet/minecraft/world/level/ItemLike;)V"),
            require = 1
    )
    private ItemStack grabPos(ItemStack value) {
        return value;
    }

}
