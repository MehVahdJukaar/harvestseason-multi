package net.mehvahdjukaar.harvestseason.forge;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.mehvahdjukaar.harvestseason.client.CarvingManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class PumpkinBlurGuiOverlay extends Gui implements IGuiOverlay {

    public static final PumpkinBlurGuiOverlay INSTANCE = new PumpkinBlurGuiOverlay(Minecraft.getInstance());

    public PumpkinBlurGuiOverlay(Minecraft minecraft) {
        super(minecraft, minecraft.getItemRenderer());
    }


    @Override
    public void render(ForgeGui gui, PoseStack mStack, float partialTicks, int width, int height) {

        ItemStack itemstack = this.minecraft.player.getInventory().getArmor(3);
        if (this.minecraft.options.getCameraType().isFirstPerson() && !itemstack.isEmpty()) {
            renderPumpkin(itemstack, width, height);
        }
    }


    public void renderPumpkin(ItemStack itemstack, int width, int height) {
        ((ForgeGui) this.minecraft.gui).setupOverlayRenderState(true, false);
        CompoundTag tag = itemstack.getTag();
        if(tag != null) {

            CompoundTag com = itemstack.getTagElement("BlockEntityTag");
            long[] packed = new long[4];
            if (com != null && com.contains("Pixels")) {
                packed = com.getLongArray("Pixels");
            }
            var blackboard = CarvingManager.getInstance(CarvingManager.Key.of(packed, false));
            var textureLocation = blackboard.getTextureLocation();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(HarvestSeasonForgeClient::getBlur);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1);
            RenderSystem.setShaderTexture(0, textureLocation);
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(0.0, height, -90.0).uv(0.0F, 1.0F).endVertex();
            bufferbuilder.vertex(width, height, -90.0).uv(1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(width, 0.0, -90.0).uv(1.0F, 0.0F).endVertex();
            bufferbuilder.vertex(0.0, 0.0, -90.0).uv(0.0F, 0.0F).endVertex();
            tesselator.end();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }


}
