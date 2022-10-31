package net.mehvahdjukaar.harvestseason.blocks;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.Locale;

public class CandyBagBlock extends Block {

    public static final EnumProperty<Content> CONTENT = EnumProperty.create("shape", Content.class);
    public static final IntegerProperty FILL_LEVEL = IntegerProperty.create("fill_level", 1, 6);

    public CandyBagBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(CONTENT, Content.POPCORN).setValue(FILL_LEVEL, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONTENT, FILL_LEVEL);
    }

    public enum Content implements StringRepresentable {
        CANDY,
        CANDY_CANE,
        CANDY_CORN,
        POPCORN,
        KERNELS,
        OTHER_CANDY;

        @Override
        public String getSerializedName() {
            return this.toString().toLowerCase(Locale.ROOT);
        }
    }
}
