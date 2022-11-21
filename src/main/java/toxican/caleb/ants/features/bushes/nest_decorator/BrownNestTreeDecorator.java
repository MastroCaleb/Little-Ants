package toxican.caleb.ants.features.bushes.nest_decorator;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import toxican.caleb.ants.blocks.AntsBlocks;
import toxican.caleb.ants.more_ants_api.AntVariant;

public class BrownNestTreeDecorator extends AntNestTreeDecorator {

    public static final BrownNestTreeDecorator INSTANCE = new BrownNestTreeDecorator(0.1f, AntVariant.BROWN, AntsBlocks.DIRT_ANT_NEST);
    // Our constructor doesn't have any arguments, so we create a unit codec that returns the singleton instance
    public static final Codec<BrownNestTreeDecorator> CODEC = Codec.unit(() -> INSTANCE);
 
    public BrownNestTreeDecorator(float probability, AntVariant antVariant, Block nestBlock) {
        super(probability, antVariant, nestBlock);
    }

    @Override
    protected TreeDecoratorType<?> getType() {
        return AntsDecorations.BROWN_NEST_DECORATOR;
    }
}
