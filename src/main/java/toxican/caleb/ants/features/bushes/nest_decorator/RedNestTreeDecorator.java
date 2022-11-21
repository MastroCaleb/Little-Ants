package toxican.caleb.ants.features.bushes.nest_decorator;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import toxican.caleb.ants.blocks.AntsBlocks;
import toxican.caleb.ants.more_ants_api.AntVariant;

public class RedNestTreeDecorator extends AntNestTreeDecorator {

    public static final RedNestTreeDecorator INSTANCE = new RedNestTreeDecorator(0.1f, AntVariant.RED, AntsBlocks.DIRT_ANT_NEST);
    // Our constructor doesn't have any arguments, so we create a unit codec that returns the singleton instance
    public static final Codec<RedNestTreeDecorator> CODEC = Codec.unit(() -> INSTANCE);
 
    public RedNestTreeDecorator(float probability, AntVariant antVariant, Block nestBlock) {
        super(probability, antVariant, nestBlock);
    }

    @Override
    protected TreeDecoratorType<?> getType() {
        return AntsDecorations.RED_NEST_DECORATOR;
    }
}
