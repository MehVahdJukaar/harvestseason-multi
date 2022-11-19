package net.mehvahdjukaar.harvestseason.items;

import dev.architectury.injectables.annotations.PlatformOnly;
import net.mehvahdjukaar.harvestseason.client.CarvedPumpkinItemRenderer;
import net.mehvahdjukaar.harvestseason.client.CarvingManager;
import net.mehvahdjukaar.harvestseason.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.client.ICustomItemRendererProvider;
import net.mehvahdjukaar.moonlight.api.client.ItemStackRenderer;
import net.minecraft.client.renderer.entity.layers.SnowGolemHeadLayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Optional;
import java.util.function.Supplier;

public class ModCarvedPumpkinItem extends BlockItem implements ICustomItemRendererProvider {


    public ModCarvedPumpkinItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack pStack) {
        CompoundTag cmp = pStack.getTagElement("BlockEntityTag");
        if (cmp != null && cmp.contains("Pixels")) {
            return Optional.of(CarvingManager.Key.of(cmp.getLongArray("Pixels"), this == ModRegistry.MOD_JACK_O_LANTERN_ITEM.get()));
        }
        return Optional.empty();
    }

    @Override
    public Supplier<ItemStackRenderer> getRendererFactory() {
        return CarvedPumpkinItemRenderer::new;
    }

    @PlatformOnly(PlatformOnly.FORGE)
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        return armorType == EquipmentSlot.HEAD;
    }

    @PlatformOnly(PlatformOnly.FORGE)
    public boolean isEnderMask(ItemStack stack, Player player, EnderMan enderMan) {
        return true;
    }
}
