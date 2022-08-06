package toxican.caleb.ants.features.bushes.nest_decorator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import toxican.caleb.ants.blocks.AntsBlocks;
import toxican.caleb.ants.blocks.nest.AntNestBlock;
import toxican.caleb.ants.entities.AntsEntities;

public class BlackNestTreeDecorator extends TreeDecorator {
    public static final BlackNestTreeDecorator INSTANCE = new BlackNestTreeDecorator(0.1f);
    private static final Direction BEE_NEST_FACE = Direction.SOUTH;
    private static final Direction[] GENERATE_DIRECTIONS = (Direction[])Direction.Type.HORIZONTAL.stream().filter(direction -> direction != BEE_NEST_FACE.getOpposite()).toArray(Direction[]::new);
    // Our constructor doesn't have any arguments, so we create a unit codec that returns the singleton instance
    public static final Codec<BlackNestTreeDecorator> CODEC = Codec.unit(() -> INSTANCE);
    private final float probability;

    public BlackNestTreeDecorator(float probability){
        this.probability = probability;
    }
 
    @Override
    protected TreeDecoratorType<?> getType() {
        return AntsDecorations.BLACK_NEST_DECORATOR;
    }

    @Override
    public void generate(TestableWorld var1, BiConsumer<BlockPos, BlockState> var2, java.util.Random var3, List<BlockPos> var4, List<BlockPos> var5) {

        if (var3.nextFloat() >= this.probability) {
            return;
        }
            for (BlockPos logPosition : var4) {
                if (var3.nextInt(4) == 0) {
                    int sideRaw = var3.nextInt(4);
                    Direction side = switch (sideRaw) {
                        case 0 -> Direction.NORTH;
                        case 1 -> Direction.SOUTH;
                        case 2 -> Direction.EAST;
                        case 3 -> Direction.WEST;
                        default -> throw new ArithmeticException("The picked side value doesn't fit in the 0 to 4 bounds");
                    };
     
                    BlockPos targetPosition = logPosition.offset(side, 1);
     
                    var2.accept(targetPosition, AntsBlocks.DIRT_ANT_NEST.getDefaultState());
                }
                int i = !var5.isEmpty() ? Math.max(var5.get(0).getY() - 1, var4.get(0).getY() + 1) : Math.min(var4.get(0).getY() + 1 + var3.nextInt(3), var4.get(var4.size() - 1).getY());
                List list = var4.stream().filter(pos -> pos.getY() == i).flatMap(pos -> Stream.of(GENERATE_DIRECTIONS).map(pos::offset)).collect(Collectors.toList());
                if (list.isEmpty()) {
                    return;
                }
                Collections.shuffle(list);
                Optional<BlockPos> optional = list.stream().filter(pos -> Feature.isAir(var1, (BlockPos) pos) && Feature.isAir(var1, ((BlockPos) pos).offset(BEE_NEST_FACE))).findFirst();
                if (optional.isEmpty()) {
                    return;
                }
                var2.accept(optional.get(), (BlockState)AntsBlocks.DIRT_ANT_NEST.getDefaultState().with(AntNestBlock.FACING, BEE_NEST_FACE));
                var1.getBlockEntity(optional.get(), AntsBlocks.NEST_BLOCK_ENTITY).ifPresent(blockEntity -> {
                    int k = 2 + var3.nextInt(2);
                    for (int j = 0; j < k; ++j) {
                        NbtCompound nbtCompound = new NbtCompound();
                        nbtCompound.putString("id", Registry.ENTITY_TYPE.getId(AntsEntities.BLACK_ANT).toString());
                        blockEntity.addAnt(nbtCompound, var3.nextInt(599), false);
                    }
                });
            }
        
    }
}
