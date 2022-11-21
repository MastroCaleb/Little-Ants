package toxican.caleb.ants.blocks.nest;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.Debug;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import toxican.caleb.ants.blocks.AntsBlocks;
import toxican.caleb.ants.blocks.NestTag;
import toxican.caleb.ants.debug.DebugAntSender;
import toxican.caleb.ants.entities.AbstractAntEntity;
import toxican.caleb.ants.entities.AntEntity;
import toxican.caleb.ants.entities.AntsEntities;
import toxican.caleb.ants.more_ants_api.AntRegistry;
import toxican.caleb.ants.more_ants_api.AntVariant;
import toxican.caleb.ants.sounds.AntsSounds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

//The Colony Block Entity. Once upon a time they were called Nests and im too lazy to change that in the code

public class AntNestEntity
extends BlockEntity {
    public static final String LEAF_POS_KEY = "LeavesPos";
    public static final String MIN_OCCUPATION_TICKS_KEY = "MinOccupationTicks";
    public static final String ENTITY_DATA_KEY = "EntityData";
    public static final String VARIANT_KEY = "Variant";
    public static final String TICKS_IN_HIVE_KEY = "TicksInHive";
    public static final String HAS_CLAY_KEY = "HasClay";
    public static final String ANTS_KEY = "Ants";
    public static final String LAST_HARVESTED_LEAF_ID_KEY = "LastHarvestedLeafID";
    private static final List<String> IRRELEVANT_ANT_NBT_KEYS = Arrays.asList("Air", "ArmorDropChances", "ArmorItems", "Brain", "CanPickUpLoot", "DeathTime", "FallDistance", "FallFlying", "Fire", "HandDropChances", "HandItems", "HurtByTimestamp", "HurtTime", "LeftHanded", "Motion", "OnGround", "PortalCooldown", "Pos", "Rotation", "CannotEnterHiveTicks", "TicksSinceNutrition", "HivePos", "Passengers", "Leash", "UUID");
    public static final int MAX_ANT_COUNT = 8;
    private static final int ANGERED_CANNOT_ENTER_HIVE_TICKS = 400;
    private static final int MIN_OCCUPATION_TICKS_WITH_CLAY = 2400;
    public static final int MIN_OCCUPATION_TICKS_WITHOUT_CLAY = 600;
    private final List<Ant> ants = Lists.newArrayList();
    @Nullable
    private BlockPos leafPos;
    @Nullable
    public Identifier lastHarvestedLeafID = null;
    
    public AntNestEntity(BlockPos pos, BlockState state) {
        super(AntsBlocks.NEST_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void markDirty() {
        if (this.isNearFire()) {
            this.angerAnts(null, this.world.getBlockState(this.getPos()), AntState.EMERGENCY);
        }
        super.markDirty();
    }

    public boolean isNearFire() {
        if (this.world == null) {
            return false;
        }
        for (BlockPos blockPos : BlockPos.iterate(this.pos.add(-1, -1, -1), this.pos.add(1, 1, 1))) {
            if (!(this.world.getBlockState(blockPos).getBlock() instanceof FireBlock)) continue;
            return true;
        }
        return false;
    }

    public boolean hasNoAnts() {
        return this.ants.isEmpty();
    }

    public boolean isFullOfAnts() {
        return this.ants.size() == 8;
    }

    public void angerAnts(@Nullable PlayerEntity player, BlockState state, AntState antState) {
        List<Entity> list = this.tryReleaseAnt(state, antState);
        if (player != null) {
            for (Entity entity : list) {
                if (!(entity instanceof AbstractAntEntity)) continue;
                AbstractAntEntity antEntity = (AbstractAntEntity)entity;
                if (!(player.getPos().squaredDistanceTo(entity.getPos()) <= 16.0)) continue;
                if (!this.isSmoked()) {
                    antEntity.setTarget(player);
                    continue;
                }
                antEntity.setCannotEnterHiveTicks(400);
            }
        }
    }

    private List<Entity> tryReleaseAnt(BlockState state, AntState antState) {
        ArrayList<Entity> list = Lists.newArrayList();
        this.ants.removeIf(ant -> AntNestEntity.releaseAnt(this.world, this.pos, this, state, ant, list, antState, this.leafPos));
        if (!list.isEmpty()) {
            super.markDirty();
        }
        return list;
    }

    public void tryEnterHive(Entity entity, boolean hasNectar, AntVariant antVariant, boolean isFromBottle) {
        this.tryEnterHive(entity, hasNectar, 0, antVariant, isFromBottle);
    }

    @Debug
    public int getAntCount() {
        return this.ants.size();
    }

    public static int getClayLevel(BlockState state) {
        return state.get(AntNestBlock.CLAY_LEVEL);
    }

    @Debug
    public boolean isSmoked() {
        return CampfireBlock.isLitCampfireInRange(this.world, this.getPos());
    }

    public void tryEnterHive(Entity entity, boolean hasNectar, int ticksInNest, AntVariant antVariant, boolean isFromBottle) {
        if (this.ants.size() >= 8) {
            return;
        }
        entity.stopRiding();
        entity.removeAllPassengers();
        NbtCompound nbtCompound = new NbtCompound();
        entity.saveNbt(nbtCompound);
        this.addAnt(nbtCompound, ticksInNest, hasNectar, antVariant, isFromBottle);
        if (this.world != null) {
            AbstractAntEntity antEntity;
            if (entity instanceof AbstractAntEntity && (antEntity = (AbstractAntEntity)entity).hasLeaves() && (!this.hasLeavesPos() || this.world.random.nextBoolean())) {
                this.leafPos = antEntity.getLeafPos();
            }
            BlockPos blockPos = this.getPos();
            this.world.playSound(null, (double)blockPos.getX(), (double)blockPos.getY(), blockPos.getZ(), AntsSounds.ENTER_NEST_EVENT, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
        entity.discard();
        super.markDirty();
    }

    public void addAnt(NbtCompound nbtCompound, int ticksInNest, boolean hasNectar, AntVariant antVariant, boolean isFromBottle) {
        this.ants.add(new Ant(nbtCompound, ticksInNest, hasNectar ? 2400 : 600, antVariant, isFromBottle));
    }

    private static boolean releaseAnt(World world, BlockPos pos, AntNestEntity antNestEntity, BlockState state2, Ant ant, @Nullable List<Entity> entities, AntState antState, @Nullable BlockPos leafPos) {
        boolean bl;
        if ((world.isNight() || world.isRaining()) && antState != AntState.EMERGENCY) {
            return false;
        }
        NbtCompound nbtCompound = ant.entityData.copy();
        AntNestEntity.removeIrrelevantNbtKeys(nbtCompound);
        nbtCompound.put("HivePos", NbtHelper.fromBlockPos(pos));
        Direction direction = state2.get(AntNestBlock.FACING);
        BlockPos blockPos = pos.offset(direction);
        bl = !world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty();
        if (bl && antState != AntState.EMERGENCY) {
            return false;
        }
        Entity entity2 = EntityType.loadEntityWithPassengers(nbtCompound, world, entity -> entity);
        if (entity2 != null) {
            if (!entity2.getType().isIn(AntsEntities.ANTS)) {
                return false;
            }
            if (entity2 instanceof AbstractAntEntity) {
                AntEntity antEntity = (AntEntity)entity2;
                if (leafPos != null && !antEntity.hasLeaves() && world.random.nextFloat() < 0.9f) {
                    antEntity.setLeafPos(leafPos);
                }
                if (antState == AntState.CLAY_DELIVERED) {
                    int i;
                    deliverLeaf(antNestEntity, antEntity);
                    if (state2.isIn(NestTag.NEST, state -> state.contains(AntNestBlock.CLAY_LEVEL)) && (i = AntNestEntity.getClayLevel(state2)) < 5) {
                        int j;
                        j = world.random.nextInt(100) == 0 ? 2 : 1;
                        if (i + j > 5) {
                            --j;
                        }
                        
                        world.setBlockState(pos, (BlockState)state2.with(AntNestBlock.CLAY_LEVEL, i + j));
                    }
                }
                AntNestEntity.ageAnt(ant.ticksInNest, antEntity);
                if (entities != null) {
                    entities.add(antEntity);
                }
                float f = entity2.getWidth();
                double d = bl ? 0.0 : 0.55 + (double)(f / 2.0f);
                double e = (double)pos.getX() + 0.5 + d * (double)direction.getOffsetX();
                double g = (double)pos.getY() + 0.5 - (double)(entity2.getHeight() / 2.0f);
                double h = (double)pos.getZ() + 0.5 + d * (double)direction.getOffsetZ();
                entity2.refreshPositionAndAngles(e, g, h, entity2.getYaw(), entity2.getPitch());
            }
            world.playSound(null, pos, AntsSounds.EXIT_NEST_EVENT, SoundCategory.BLOCKS, 1.0f, 1.0f);
            return world.spawnEntity(entity2);
        }
        return false;
    }
    
    private static void deliverLeaf(AntNestEntity antNestEntity, AbstractAntEntity antEntity) {
        BlockState leafBlockState = antEntity.getLeaf();
        if(leafBlockState != null) {
            Item leafBlockItem = leafBlockState.getBlock().asItem();
            if(leafBlockItem != null && leafBlockItem != Items.AIR) {
                antNestEntity.lastHarvestedLeafID = Registry.ITEM.getId(leafBlockItem);
            }
        }
        antEntity.onLeafDelivered();
    }
    
    static void removeIrrelevantNbtKeys(NbtCompound compound) {
        for (String string : IRRELEVANT_ANT_NBT_KEYS) {
            compound.remove(string);
        }
    }

    private static void ageAnt(int ticks, AbstractAntEntity ant) {
        int i = ant.getBreedingAge();
        if (i < 0) {
            ant.setBreedingAge(Math.min(0, i + ticks));
        } else if (i > 0) {
            ant.setBreedingAge(Math.max(0, i - ticks));
        }
        ant.setLoveTicks(Math.max(0, ant.getLoveTicks() - ticks));
    }

    private boolean hasLeavesPos() {
        return this.leafPos != null;
    }

    private static void tickAnts(World world, BlockPos pos, AntNestEntity blockEntity, BlockState state, List<Ant> ants, @Nullable BlockPos leafPos) {
        boolean bl = false;
        Iterator<Ant> iterator = ants.iterator();
        while (iterator.hasNext()) {
            Ant ant = iterator.next();
            if (ant.ticksInNest > ant.minOccupationTicks) {
                AntState antState;
                antState = ant.entityData.getBoolean(HAS_CLAY_KEY) ? AntState.CLAY_DELIVERED : AntState.ANT_RELEASED;
                if (AntNestEntity.releaseAnt(world, pos, blockEntity, state, ant, null, antState, leafPos)) {
                    bl = true;
                    iterator.remove();
                }
            }
            ++ant.ticksInNest;
        }
        if (bl) {
            AntNestEntity.markDirty(world, pos, state);
        }
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, AntNestEntity blockEntity) {
        AntNestEntity.tickAnts(world, pos, blockEntity, state, blockEntity.ants, blockEntity.leafPos);
        if (!blockEntity.ants.isEmpty() && world.getRandom().nextDouble() < 0.005) {
            double d = (double)pos.getX() + 0.5;
            double e = pos.getY();
            double f = (double)pos.getZ() + 0.5;
            world.playSound(null, d, e, f, AntsSounds.ANT_SOUND_EVENT, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
        DebugAntSender.sendAntnestDebugData(world, pos, state, blockEntity);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.ants.clear();
        NbtList nbtList = nbt.getList(ANTS_KEY, 10);
        for (int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            Ant ant = new Ant(nbtCompound.getCompound(ENTITY_DATA_KEY), nbtCompound.getInt(TICKS_IN_HIVE_KEY), nbtCompound.getInt(MIN_OCCUPATION_TICKS_KEY), AntRegistry.ANT_VARIANT.get(Identifier.tryParse(nbtCompound.getString(VARIANT_KEY))), nbtCompound.getBoolean("FromBottle"));
            this.ants.add(ant);
        }
        this.leafPos = null;
        if (nbt.contains(LEAF_POS_KEY)) {
            this.leafPos = NbtHelper.toBlockPos(nbt.getCompound(LEAF_POS_KEY));
        }
        this.lastHarvestedLeafID = null;
        if(nbt.contains(LAST_HARVESTED_LEAF_ID_KEY, NbtElement.STRING_TYPE)) {
            this.lastHarvestedLeafID = Identifier.tryParse(nbt.getString(LAST_HARVESTED_LEAF_ID_KEY));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.put(ANTS_KEY, this.getAnts());
        if (this.hasLeavesPos()) {
            nbt.put(LEAF_POS_KEY, NbtHelper.fromBlockPos(this.leafPos));
        }
        if(this.lastHarvestedLeafID != null) {
            nbt.putString(LAST_HARVESTED_LEAF_ID_KEY, this.lastHarvestedLeafID.toString());
        }
    }
    
    public @Nullable Identifier getLastHarvestedLeafID() {
        return this.lastHarvestedLeafID;
    }

    public NbtList getAnts() {
        NbtList nbtList = new NbtList();
        for (Ant ant : this.ants) {
            NbtCompound nbtCompound = ant.entityData.copy();
            nbtCompound.remove("UUID");
            NbtCompound nbtCompound2 = new NbtCompound();
            nbtCompound2.put(ENTITY_DATA_KEY, nbtCompound);
            nbtCompound2.putInt(TICKS_IN_HIVE_KEY, ant.ticksInNest);
            nbtCompound2.putInt(MIN_OCCUPATION_TICKS_KEY, ant.minOccupationTicks);
            nbtList.add(nbtCompound2);
        }
        return nbtList;
    }

    public static enum AntState {
        CLAY_DELIVERED,
        ANT_RELEASED,
        EMERGENCY;

    }

    static class Ant {
        final NbtCompound entityData;
        int ticksInNest;
        final int minOccupationTicks;
        final AntVariant antVariant;
        final boolean isFromBottle;

        Ant(NbtCompound entityData, int ticksInNest, int minOccupationTicks, AntVariant antVariant, boolean isFromBottle) {
            AntNestEntity.removeIrrelevantNbtKeys(entityData);
            this.antVariant = antVariant;
            this.isFromBottle = isFromBottle;
            this.entityData = entityData;
            this.ticksInNest = ticksInNest;
            this.minOccupationTicks = minOccupationTicks;
        }
    }
}
