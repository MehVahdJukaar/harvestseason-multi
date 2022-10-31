package net.mehvahdjukaar.harvestseason.client;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mehvahdjukaar.moonlight.api.client.ItemStackRenderer;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;


public class CarvedPumpkinItemRenderer extends ItemStackRenderer {
    private final BlockRenderDispatcher blockRenderer;


    public CarvedPumpkinItemRenderer() {
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {

        matrixStackIn.pushPose();

        blockRenderer.renderSingleBlock(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);

        CompoundTag com = stack.getTagElement("BlockEntityTag");
        long[] packed = new long[4];
        if (com != null && com.contains("Pixels")) {
            packed = com.getLongArray("Pixels");
        }
        var blackboard = CarvingManager.getInstance(CarvingManager.Key.of(packed));
        VertexConsumer builder = bufferIn.getBuffer(blackboard.getRenderType());

        int lu = combinedLightIn & '\uffff';
        int lv = combinedLightIn >> 16 & '\uffff';

        matrixStackIn.mulPose(RotHlpr.Y180);
        matrixStackIn.translate(-1, 0, 0);
        CarvedPumpkinTileRenderer.addQuadSide(builder, matrixStackIn, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, lu, lv, 0, 0, 1);

        matrixStackIn.popPose();
    }
}