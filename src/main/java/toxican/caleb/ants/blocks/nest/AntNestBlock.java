package toxican.caleb.ants.blocks.nest;

import java.util.List;
import java.util.Random;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShovelItem;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import toxican.caleb.ants.blocks.AntsBlocks;
import toxican.caleb.ants.blocks.NestTag;
import toxican.caleb.ants.damage.AntsDamageSource;
import toxican.caleb.ants.enchantment.AntHelper;
import toxican.caleb.ants.entities.AntEntity;
import toxican.caleb.ants.items.AntsItems;
import org.jetbrains.annotations.Nullable;

//The Colony Block. Once upon a time they were called Nests and im too lazy to change that in the code

public class AntNestBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final IntProperty CLAY_LEVEL = IntProperty.of("clay_level", 0, 5);
    public static final int FULL_HONEY_LEVEL = 5;
    private static final int DROPPED_HONEYCOMB_COUNT = 3;

    public AntNestBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(CLAY_LEVEL, 0)).with(FACING, Direction.NORTH));
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        boolean bl = this.checkAnt(entity);
        if(bl){

        }
        else if (entity.isAlive() && entity instanceof LivingEntity && !AntHelper.hasAntResistance((LivingEntity)entity)) {
            entity.damage(AntsDamageSource.NEST, 1.0f);
        }
        super.onSteppedOn(world, pos, state, entity);
    }

    public boolean checkAnt(Entity entity){
        if(entity instanceof AntEntity){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(CLAY_LEVEL);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, state, blockEntity, stack);
        if (!world.isClient && blockEntity instanceof AntNestEntity) {
            AntNestEntity nestBlockEntity = (AntNestEntity)blockEntity;
            if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack) == 0) {
                nestBlockEntity.angerAnts(player, state, AntNestEntity.AntState.EMERGENCY);
                world.updateComparators(pos, this);
                this.angerNearbyAnts(world, pos);
            }
            CriteriaAnt.ANT_NEST_DESTROYED.trigger((ServerPlayerEntity)player, state, stack, nestBlockEntity.getAntCount());
        }
    }

    private void angerNearbyAnts(World world, BlockPos pos) {
        List<AntEntity> list = world.getNonSpectatingEntities(AntEntity.class, new Box(pos).expand(8.0, 6.0, 8.0));
        if (!list.isEmpty()) {
            List<PlayerEntity> list2 = world.getNonSpectatingEntities(PlayerEntity.class, new Box(pos).expand(8.0, 6.0, 8.0));
            int i = list2.size();
            for (AntEntity antEntity : list) {
                if (antEntity.getTarget() != null) continue;
                antEntity.setTarget(list2.get(world.random.nextInt(i)));
            }
        }
    }

    public static void dropHoneycomb(World world, BlockPos pos) {
        AntNestBlock.dropStack(world, pos, new ItemStack(Items.CLAY_BALL, 3));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player2, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player2.getStackInHand(hand);
        int i = state.get(CLAY_LEVEL);
        boolean bl = false;
        if (i >= 5) {
            Item item = itemStack.getItem();
            if (itemStack.getItem() instanceof ShovelItem) {
                world.playSound(player2, player2.getX(), player2.getY(), player2.getZ(), SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.NEUTRAL, 1.0f, 1.0f);
                AntNestBlock.dropHoneycomb(world, pos);
                itemStack.damage(1, player2, player -> player.sendToolBreakStatus(hand));
                bl = true;
                world.emitGameEvent((Entity)player2, GameEvent.SHEAR, pos);
            } else if (itemStack.isOf(Items.GLASS_BOTTLE)) {
                itemStack.decrement(1);
                world.playSound(player2, player2.getX(), player2.getY(), player2.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0f, 1.0f);
                if (itemStack.isEmpty()) {
                    player2.setStackInHand(hand, new ItemStack(AntsItems.CLAY_BOTTLE));
                } else if (!player2.getInventory().insertStack(new ItemStack(AntsItems.CLAY_BOTTLE))) {
                    player2.dropItem(new ItemStack(AntsItems.CLAY_BOTTLE), false);
                }
                bl = true;
                world.emitGameEvent((Entity)player2, GameEvent.FLUID_PICKUP, pos);
            }
            if (!world.isClient() && bl) {
                player2.incrementStat(Stats.USED.getOrCreateStat(item));
            }
        }
        if (bl) {
            if (!CampfireBlock.isLitCampfireInRange(world, pos)) {
                if (this.hasAnts(world, pos)) {
                    this.angerNearbyAnts(world, pos);
                }
                this.takeHoney(world, state, pos, player2, AntNestEntity.AntState.EMERGENCY);
            } else {
                this.takeHoney(world, state, pos);
            }
            return ActionResult.success(world.isClient);
        }
        return super.onUse(state, world, pos, player2, hand, hit);
    }

    public boolean isPlayer(Entity entity){
        if(entity instanceof PlayerEntity){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean hasAnts(World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AntNestEntity) {
            AntNestEntity nestBlockEntity = (AntNestEntity)blockEntity;
            return !nestBlockEntity.hasNoAnts();
        }
        return false;
    }

    public void takeHoney(World world, BlockState state, BlockPos pos, @Nullable PlayerEntity player, AntNestEntity.AntState antState) {
        this.takeHoney(world, state, pos);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AntNestEntity) {
            AntNestEntity nestBlockEntity = (AntNestEntity)blockEntity;
            nestBlockEntity.angerAnts(player, state, antState);
        }
    }

    public void takeHoney(World world, BlockState state, BlockPos pos) {
        world.setBlockState(pos, (BlockState)state.with(CLAY_LEVEL, 0), Block.NOTIFY_ALL);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(CLAY_LEVEL) >= 5) {
            for (int i = 0; i < random.nextInt(1) + 1; ++i) {
                this.spawnHoneyParticles(world, pos, state);
            }
        }
    }

    private void spawnHoneyParticles(World world, BlockPos pos, BlockState state) {
        if (!state.getFluidState().isEmpty() || world.random.nextFloat() < 0.3f) {
            return;
        }
        VoxelShape voxelShape = state.getCollisionShape(world, pos);
        double d = voxelShape.getMax(Direction.Axis.Y);
        if (d >= 1.0 && !state.isIn(NestTag.NEST)) {
            double e = voxelShape.getMin(Direction.Axis.Y);
            if (e > 0.0) {
                this.addHoneyParticle(world, pos, voxelShape, (double)pos.getY() + e - 0.05);
            } else {
                BlockPos blockPos = pos.down();
                BlockState blockState = world.getBlockState(blockPos);
                VoxelShape voxelShape2 = blockState.getCollisionShape(world, blockPos);
                double f = voxelShape2.getMax(Direction.Axis.Y);
                if ((f < 1.0 || !blockState.isFullCube(world, blockPos)) && blockState.getFluidState().isEmpty()) {
                    this.addHoneyParticle(world, pos, voxelShape, (double)pos.getY() - 0.05);
                }
            }
        }
    }

    private void addHoneyParticle(World world, BlockPos pos, VoxelShape shape, double height) {
        this.addHoneyParticle(world, (double)pos.getX() + shape.getMin(Direction.Axis.X), (double)pos.getX() + shape.getMax(Direction.Axis.X), (double)pos.getZ() + shape.getMin(Direction.Axis.Z), (double)pos.getZ() + shape.getMax(Direction.Axis.Z), height);
    }

    private void addHoneyParticle(World world, double minX, double maxX, double minZ, double maxZ, double height) {
        world.addParticle(ParticleTypes.COMPOSTER, MathHelper.lerp(world.random.nextDouble(), minX, maxX), height, MathHelper.lerp(world.random.nextDouble(), minZ, maxZ), 0.0, 0.0, 0.0);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CLAY_LEVEL, FACING);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AntNestEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : AntNestBlock.checkType(type, AntsBlocks.NEST_BLOCK_ENTITY, AntNestEntity::serverTick);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity;
        if (!world.isClient && player.isCreative() && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS) && (blockEntity = world.getBlockEntity(pos)) instanceof AntNestEntity) {
            boolean bl;
            AntNestEntity nestBlockEntity = (AntNestEntity)blockEntity;
            ItemStack itemStack = new ItemStack(this);
            int i = state.get(CLAY_LEVEL);
            boolean bl2 = bl = !nestBlockEntity.hasNoAnts();
            if (bl || i > 0) {
                NbtCompound nbtCompound;
                if (bl) {
                    nbtCompound = new NbtCompound();
                    nbtCompound.put("Ants", nestBlockEntity.getAnts());
                    BlockItem.setBlockEntityNbt(itemStack, AntsBlocks.NEST_BLOCK_ENTITY, nbtCompound);
                }
                nbtCompound = new NbtCompound();
                nbtCompound.putInt("clay_level", i);
                itemStack.setSubNbt("BlockStateTag", nbtCompound);
                ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            }
        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        BlockEntity blockEntity;
        Entity entity = builder.getNullable(LootContextParameters.THIS_ENTITY);
        if ((entity instanceof TntEntity || entity instanceof CreeperEntity || entity instanceof WitherSkullEntity || entity instanceof WitherEntity || entity instanceof TntMinecartEntity) && (blockEntity = builder.getNullable(LootContextParameters.BLOCK_ENTITY)) instanceof AntNestEntity) {
            AntNestEntity nestBlockEntity = (AntNestEntity)blockEntity;
            nestBlockEntity.angerAnts(null, state, AntNestEntity.AntState.EMERGENCY);
        }
        return super.getDroppedStacks(state, builder);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        BlockEntity blockEntity;
        if (world.getBlockState(neighborPos).getBlock() instanceof FireBlock && (blockEntity = world.getBlockEntity(pos)) instanceof AntNestEntity) {
            AntNestEntity nestBlockEntity = (AntNestEntity)blockEntity;
            nestBlockEntity.angerAnts(null, state, AntNestEntity.AntState.EMERGENCY);
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
}
