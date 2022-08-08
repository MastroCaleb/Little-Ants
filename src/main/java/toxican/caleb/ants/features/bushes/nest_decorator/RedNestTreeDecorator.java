package toxican.caleb.ants.features.bushes.nest_decorator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.mojang.serialization.Codec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import toxican.caleb.ants.blocks.AntsBlocks;
import toxican.caleb.ants.blocks.nest.AntNestBlock;
import toxican.caleb.ants.entities.AntsEntities;

public class RedNestTreeDecorator extends TreeDecorator {
    public static final RedNestTreeDecorator INSTANCE = new RedNestTreeDecorator(0.1f);
    private static final Direction BEE_NEST_FACE = Direction.SOUTH;
    private static final Direction[] GENERATE_DIRECTIONS = (Direction[])Direction.Type.HORIZONTAL.stream().filter(direction -> direction != BEE_NEST_FACE.getOpposite()).toArray(Direction[]::new);
    // Our constructor doesn't have any arguments, so we create a unit codec that returns the singleton instance
    public static final Codec<RedNestTreeDecorator> CODEC = Codec.unit(() -> INSTANCE);
    private final float probability;

    public RedNestTreeDecorator(float probability){
        this.probability = probability;
    }
 
    @Override
    protected TreeDecoratorType<?> getType() {
        return AntsDecorations.RED_NEST_DECORATOR;
    }

    @Override
    public void generate(Generator generator) {

        if (generator.getRandom().nextFloat() >= this.probability) {
            return;
        }
            for (BlockPos logPosition : generator.getLogPositions()) {
                if (generator.getRandom().nextInt(8) == 0) {
                    int sideRaw = generator.getRandom().nextInt(4);
                    Direction side = switch (sideRaw) {
                        case 0 -> Direction.NORTH;
                        case 1 -> Direction.SOUTH;
                        case 2 -> Direction.EAST;
                        case 3 -> Direction.WEST;
                        default -> throw new ArithmeticException("The picked side value doesn't fit in the 0 to 4 bounds");
                    };
     
                    BlockPos targetPosition = logPosition.offset(side, 1);
     
                    generator.replace(targetPosition, AntsBlocks.DIRT_ANT_NEST.getDefaultState());
                }
                int i = !generator.getLeavesPositions().isEmpty() ? Math.max(generator.getLeavesPositions().get(0).getY() - 1, generator.getLogPositions().get(0).getY() + 1) : Math.min(generator.getLogPositions().get(0).getY() + 1 + generator.getRandom().nextInt(3), generator.getLogPositions().get(generator.getLogPositions().size() - 1).getY());
                List list = generator.getLogPositions().stream().filter(pos -> pos.getY() == i).flatMap(pos -> Stream.of(GENERATE_DIRECTIONS).map(pos::offset)).collect(Collectors.toList());
                if (list.isEmpty()) {
                    return;
                }
                Collections.shuffle(list);
                Optional<BlockPos> optional = list.stream().filter(pos -> this.isAir(generator.getWorld(), (BlockPos) pos) && this.isAir(generator.getWorld(), ((BlockPos) pos).offset(BEE_NEST_FACE))).findFirst();
                if (optional.isEmpty()) {
                    return;
                }
                generator.replace(optional.get(), (BlockState)AntsBlocks.DIRT_ANT_NEST.getDefaultState().with(AntNestBlock.FACING, BEE_NEST_FACE));
                generator.getWorld().getBlockEntity(optional.get(), AntsBlocks.NEST_BLOCK_ENTITY).ifPresent(blockEntity -> {
                    int k = 1 + generator.getRandom().nextInt(2);
                    for (int j = 0; j < k; ++j) {
                        NbtCompound nbtCompound = new NbtCompound();
                        
                        nbtCompound.putString("id", Registry.ENTITY_TYPE.getId(AntsEntities.RED_ANT).toString());
                        blockEntity.addAnt(nbtCompound, generator.getRandom().nextInt(599), false);
                    }
                });
            }
        
    }

    public static boolean isAir(TestableWorld world, BlockPos pos) {
        return world.testBlockState(pos, AbstractBlock.AbstractBlockState::isAir);
    }
}
