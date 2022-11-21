package toxican.caleb.ants.entities;

import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.NoWaterTargeting;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.UniversalAngerGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.annotation.Debug;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import toxican.caleb.ants.blocks.AntsBlocks;
import toxican.caleb.ants.blocks.NestTag;
import toxican.caleb.ants.blocks.nest.AntNestEntity;
import toxican.caleb.ants.debug.DebugAntSender;
import toxican.caleb.ants.enchantment.AntHelper;
import toxican.caleb.ants.poi.POITags;
import toxican.caleb.ants.sounds.AntsSounds;

import org.jetbrains.annotations.Nullable;

//AI and stuff are implemented here, additional stuff in AntEntity
public abstract class AbstractAntEntity
extends AnimalEntity
implements Angerable, IAnimatable {
    public static final float field_30271 = 120.32113f;
    public static final int field_28638 = MathHelper.ceil(1.4959966f);
    private static final TrackedData<Byte> ANT_FLAGS = DataTracker.registerData(AbstractAntEntity.class, TrackedDataHandlerRegistry.BYTE);
    public static final TrackedData<Integer> ANGER = DataTracker.registerData(AbstractAntEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Optional<BlockState>> LEAF = DataTracker.registerData(AbstractAntEntity.class, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_STATE);
    private static final int NEAR_TARGET_FLAG = 2;
    private static final int HAS_CLAY_FLAG = 8;
    private static final int MAX_LIFETIME_AFTER_STINGING = 1200;
    private static final int LEAVES_NAVIGATION_START_TICKS = 2400;
    private static final int NUTRITION_FAIL_TICKS = 3600;
    private static final int field_30287 = 4;
    private static final int TOO_FAR_DISTANCE = 32;
    private static final int field_30292 = 2;
    private static final int MIN_HIVE_RETURN_DISTANCE = 16;
    private static final int field_30294 = 20;
    public static final String CANNOT_ENTER_HIVE_TICKS_KEY = "CannotEnterHiveTicks";
    public static final String TICKS_SINCE_NUTRITION_KEY = "TicksSinceNutrition";
    public static final String HAS_CLAY_KEY = "HasClay";
    public static final String LEAVES_POS_KEY = "LeavesPos";
    public static final String HIVE_POS_KEY = "HivePos";
    public static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);
    @Nullable
    public UUID angryAt;
    int ticksSinceNutrition;
    private int cannotEnterHiveTicks;
    private static final int field_30274 = 200;
    int ticksLeftToFindHive;
    private static final int field_30275 = 200;
    int ticksUntilCanFeed;
    @Nullable
    public BlockPos leavesPos;
    @Nullable
    BlockPos hivePos;
    protected FeedGoal feedGoal;
    protected MoveToHiveGoal moveToHiveGoal;
    protected MoveToLeavesGoal moveToLeafGoal;
    private int ticksInsideWater;
    private AnimationFactory factory = new AnimationFactory(this);
    public static final EntityDimensions STANDING_DIMENSIONS = EntityDimensions.changing(0.60f, 0.60f);
    public boolean fromBottle = false;

    public AbstractAntEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super((EntityType<? extends AnimalEntity>)entityType, world);
        this.ticksUntilCanFeed = MathHelper.nextInt(this.random, 20, 60);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0f);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0f);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0f);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0f);
        this.setPathfindingPenalty(PathNodeType.FENCE, -1.0f);
    }

    public static DefaultAttributeContainer.Builder createAntAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5f).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1f);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(LEAF, Optional.empty());
        this.dataTracker.startTracking(ANT_FLAGS, (byte)0);
        this.dataTracker.startTracking(ANGER, 0);
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return 10.0f;
    }
    

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new StingGoal(this, 0.8f, true));
        this.goalSelector.add(2, new AnimalMateGoal(this, 0.5));
        this.goalSelector.add(3, new TemptGoal(this, 0.5, Ingredient.fromTag(ItemTags.LEAVES), false));
        this.feedGoal = new FeedGoal();
        this.goalSelector.add(4, this.feedGoal);
        this.goalSelector.add(5, new FollowParentGoal(this, 0.5));
        this.goalSelector.add(5, new FindHiveGoal());
        this.moveToHiveGoal = new MoveToHiveGoal();
        this.goalSelector.add(5, this.moveToHiveGoal);
        this.moveToLeafGoal = new MoveToLeavesGoal();
        this.goalSelector.add(6, this.moveToLeafGoal);
        this.goalSelector.add(8, new AntWanderAroundGoal());
        this.goalSelector.add(9, new SwimGoal(this));
        this.targetSelector.add(1, new AntRevengeGoal(this).setGroupRevenge(new Class[0]));
        this.targetSelector.add(2, new StingTargetGoal(this));
        this.targetSelector.add(3, new UniversalAngerGoal<AbstractAntEntity>(this, true));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(event.isMoving()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("ant.walk", true));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("ant.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<AbstractAntEntity>(this, "controller", 3, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.hasHive()) {
            nbt.put(HIVE_POS_KEY, NbtHelper.fromBlockPos(this.getHivePos()));
        }
        if (this.hasLeaves()) {
            nbt.put(LEAVES_POS_KEY, NbtHelper.fromBlockPos(this.getLeafPos()));
        }
        BlockState blockState = this.getLeaf();
        if (blockState != null) {
            nbt.put("carriedLeaf", NbtHelper.fromBlockState(blockState));
        }
        nbt.putBoolean(HAS_CLAY_KEY, this.hasClay());
        nbt.putInt(TICKS_SINCE_NUTRITION_KEY, this.ticksSinceNutrition);
        nbt.putInt(CANNOT_ENTER_HIVE_TICKS_KEY, this.cannotEnterHiveTicks);
        this.writeAngerToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.hivePos = null;
        if (nbt.contains(HIVE_POS_KEY)) {
            this.hivePos = NbtHelper.toBlockPos(nbt.getCompound(HIVE_POS_KEY));
        }
        this.leavesPos = null;
        if (nbt.contains(LEAVES_POS_KEY)) {
            this.leavesPos = NbtHelper.toBlockPos(nbt.getCompound(LEAVES_POS_KEY));
        }
        BlockState blockState = null;
        if (nbt.contains("carriedLeaf", 10) && (blockState = NbtHelper.toBlockState(nbt.getCompound("carriedLeaf"))).isAir()) {
            blockState = null;
        }
        this.setLeaf(blockState);
        super.readCustomDataFromNbt(nbt);
        this.setHasClay(nbt.getBoolean(HAS_CLAY_KEY));
        this.ticksSinceNutrition = nbt.getInt(TICKS_SINCE_NUTRITION_KEY);
        this.cannotEnterHiveTicks = nbt.getInt(CANNOT_ENTER_HIVE_TICKS_KEY);
        this.readAngerFromNbt(this.world, nbt);
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean bl = target.damage(DamageSource.mob(this), (int)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
        boolean aR = AntHelper.hasAntResistance((LivingEntity) target);
        if (bl && !aR) {
            if(!AntHelper.hasAntResistance((LivingEntity)target)){
                this.applyDamageEffects(this, target);
                int i = 0;
                if (this.world.getDifficulty() == Difficulty.EASY || this.world.getDifficulty() == Difficulty.NORMAL || this.world.getDifficulty() == Difficulty.HARD) {
                    i = 1;
                }
                if (i > 0) {
                    ((LivingEntity)target).damage(DamageSource.mob(this), 1.0f);
                }
            }
            this.stopAnger();
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, 1.0f, 1.0f);
        }
        return bl;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.hasClay()) {
            for (int i = 0; i < this.random.nextInt(2) + 1; ++i) {
                this.addParticle(this.world, this.getX() - (double)0.3f, this.getX() + (double)0.3f, this.getZ() - (double)0.3f, this.getZ() + (double)0.3f, this.getBodyY(0.5), ParticleTypes.FALLING_NECTAR);
            }
        }
    }

    private void addParticle(World world, double lastX, double x, double lastZ, double z, double y, ParticleEffect effect) {
        world.addParticle(effect, MathHelper.lerp(world.random.nextDouble(), lastX, x), y, MathHelper.lerp(world.random.nextDouble(), lastZ, z), 0.0, 0.0, 0.0);
    }

    void startMovingTo(BlockPos pos) {
        Vec3d vec3d2;
        Vec3d vec3d = Vec3d.ofBottomCenter(pos);
        int i = 0;
        BlockPos blockPos = this.getBlockPos();
        int j = (int)vec3d.y - blockPos.getY();
        if (j > 2) {
            i = 4;
        } else if (j < -2) {
            i = -4;
        }
        int k = 6;
        int l = 8;
        int m = blockPos.getManhattanDistance(pos);
        if (m < 15) {
            k = m / 2;
            l = m / 2;
        }
        if ((vec3d2 = NoWaterTargeting.find(this, k, l, i, vec3d, 0.3141592741012573)) == null) {
            return;
        }
        this.navigation.setRangeMultiplier(0.5f);
        this.navigation.startMovingTo(vec3d2.x, vec3d2.y, vec3d2.z, 1.0);
    }

    @Nullable
    public BlockPos getLeafPos() {
        return this.leavesPos;
    }

    public boolean hasLeaves() {
        return this.leavesPos != null;
    }

    public void setLeafPos(BlockPos leavesPos) {
        this.leavesPos = leavesPos;
    }

    @Debug
    public int getMoveGoalTicks() {
        return Math.max(this.moveToHiveGoal.ticks, this.moveToLeafGoal.ticks);
    }

    @Debug
    public List<BlockPos> getPossibleHives() {
        return this.moveToHiveGoal.possibleHives;
    }

    private boolean failedPollinatingTooLong() {
        return this.ticksSinceNutrition > 3600;
    }

    boolean canEnterHive() {
        if (this.cannotEnterHiveTicks > 0 || this.feedGoal.isRunning() || this.getTarget() != null) {
            return false;
        }
        boolean bl = this.failedPollinatingTooLong() || this.world.isRaining() || this.world.isNight() || this.hasClay();
        return bl && !this.isHiveNearFire();
    }

    public void setCannotEnterHiveTicks(int cannotEnterHiveTicks) {
        this.cannotEnterHiveTicks = cannotEnterHiveTicks;
    }

    @Override
    protected void mobTick() {
        this.ticksInsideWater = this.isInsideWaterOrBubbleColumn() ? ++this.ticksInsideWater : 0;
        if (this.ticksInsideWater > 20) {
            this.damage(DamageSource.DROWN, 1.0f);
        }
        if (!this.hasClay()) {
            ++this.ticksSinceNutrition;
        }
        if (!this.world.isClient) {
            this.tickAngerLogic((ServerWorld)this.world, false);
        }
    }

    public void resetNutritionTicks() {
        this.ticksSinceNutrition = 0;
    }

    private boolean isHiveNearFire() {
        if (this.hivePos == null) {
            return false;
        }
        BlockEntity blockEntity = this.world.getBlockEntity(this.hivePos);
        return blockEntity instanceof AntNestEntity && ((AntNestEntity)blockEntity).isNearFire();
    }

    @Override
    public int getAngerTime() {
        return this.dataTracker.get(ANGER);
    }

    @Override
    public void setAngerTime(int angerTime) {
        this.dataTracker.set(ANGER, angerTime);
    }

    @Override
    @Nullable
    public UUID getAngryAt() {
        return this.angryAt;
    }

    @Override
    public void setAngryAt(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
    }

    private boolean doesHiveHaveSpace(BlockPos pos) {
        BlockEntity blockEntity = this.world.getBlockEntity(pos);
        if (blockEntity instanceof AntNestEntity) {
            return !((AntNestEntity)blockEntity).isFullOfAnts();
        }
        return false;
    }

    @Debug
    public boolean hasHive() {
        return this.hivePos != null;
    }

    @Nullable
    @Debug
    public BlockPos getHivePos() {
        return this.hivePos;
    }

    @Debug
    public GoalSelector getGoalSelector() {
        return this.goalSelector;
    }

    @Override
    protected void sendAiDebugData() {
        super.sendAiDebugData();
        DebugAntSender.sendAntDebugData(this);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.world.isClient) {
            if (this.cannotEnterHiveTicks > 0) {
                --this.cannotEnterHiveTicks;
            }
            if (this.ticksLeftToFindHive > 0) {
                --this.ticksLeftToFindHive;
            }
            if (this.ticksUntilCanFeed > 0) {
                --this.ticksUntilCanFeed;
            }
            boolean bl = this.hasAngerTime() && this.getTarget() != null && this.getTarget().squaredDistanceTo(this) < 4.0;
            this.setNearTarget(bl);
            if (this.age % 20 == 0 && !this.isHiveValid()) {
                this.hivePos = null;
            }
        }
    }

    boolean isHiveValid() {
        if (!this.hasHive()) {
            return false;
        }
        BlockEntity blockEntity = this.world.getBlockEntity(this.hivePos);
        return blockEntity != null && blockEntity.getType() == AntsBlocks.NEST_BLOCK_ENTITY;
    }

    public void setLeaf(@Nullable BlockState state) {
        this.dataTracker.set(LEAF, Optional.ofNullable(state));
    }

    @Nullable
    public BlockState getLeaf() {
        return this.dataTracker.get(LEAF).orElse(null);
    }

    public boolean hasClay() {
        return this.getAntFlag(HAS_CLAY_FLAG);
    }

    public void setHasClay(boolean hasClay) {
        if (hasClay) {
            this.resetNutritionTicks();
        }
        this.setAntFlag(HAS_CLAY_FLAG, hasClay);
    }

    private boolean isNearTarget() {
        return this.getAntFlag(NEAR_TARGET_FLAG);
    }

    private void setNearTarget(boolean nearTarget) {
        this.setAntFlag(NEAR_TARGET_FLAG, nearTarget);
    }

    boolean isTooFar(BlockPos pos) {
        return !this.isWithinDistance(pos, 32);
    }

    private void setAntFlag(int bit, boolean value) {
        if (value) {
            this.dataTracker.set(ANT_FLAGS, (byte)(this.dataTracker.get(ANT_FLAGS) | bit));
        } else {
            this.dataTracker.set(ANT_FLAGS, (byte)(this.dataTracker.get(ANT_FLAGS) & ~bit));
        }
    }

    private boolean getAntFlag(int location) {
        return (this.dataTracker.get(ANT_FLAGS) & location) != 0;
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        MobNavigation antNavigation = new MobNavigation(this, world){

            @Override
            public boolean isValidPosition(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isFullCube(world, pos);
            }

            @Override
            public void tick() {
                if (AbstractAntEntity.this.feedGoal.isRunning()) {
                    return;
                }
                super.tick();
            }
        };
        antNavigation.setCanPathThroughDoors(false);
        antNavigation.setCanSwim(true);
        antNavigation.setCanEnterOpenDoors(true);
        return antNavigation;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isIn(ItemTags.LEAVES);
    }

    boolean isLeaves(BlockPos pos) {
        return this.world.canSetBlock(pos) && this.world.getBlockState(pos).isIn(NestTag.ANT_FOOD);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AntsSounds.ANT_SOUND_EVENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return AntsSounds.ANT_HURT_EVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AntsSounds.ANT_HURT_EVENT;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }

    @Override
    public AbstractAntEntity createChild(ServerWorld world, PassiveEntity passiveEntity) {
        return null;
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        if (this.isBaby()) {
            return dimensions.height * 0.5f;
        }
        return dimensions.height * 0.5f;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return true;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
    }

    public void onLeafDelivered() {
        this.setHasClay(false);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (!this.world.isClient) {
            this.feedGoal.cancel();
        }
        return super.damage(source, amount);
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.ARTHROPOD;
    }

    @Override
    protected void swimUpward(TagKey<Fluid> fluid) {
        this.setVelocity(this.getVelocity().add(0.0, 0.2, 0.0));
    }

    @Override
    public Vec3d getLeashOffset() {
        return new Vec3d(0.0, 0.5f * this.getStandingEyeHeight(), this.getWidth() * 0.2f);
    }

    boolean isWithinDistance(BlockPos pos, int distance) {
        return pos.isWithinDistance(this.getBlockPos(), (double)distance);
    }

    public class FeedGoal
    extends NotAngryGoal {
        private static final int field_30300 = 400;
        private static final int field_30301 = 20;
        private static final int field_30302 = 60;
        private final Predicate<BlockState> leafPredicate;
        private static final double field_30303 = 0.1;
        private static final int field_30304 = 25;
        private static final float field_30305 = 0.35f;
        private static final float field_30306 = 0.6f;
        private static final float field_30307 = 0.33333334f;
        private int pollinationTicks;
        private int lastNutritionTick;
        private boolean running;
        @Nullable
        private Vec3d nextTarget;
        private int ticks;
        private static final int field_30308 = 600;

        public FeedGoal() {
            this.leafPredicate = state -> {
                if (state.isIn(NestTag.ANT_FOOD) || state.isOf(AntsBlocks.WINDY_DANDELION)) {
                    AbstractAntEntity.this.setLeaf(state);
                    return true;
                }
                return false;
            };
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canAntStart() {
            if (AbstractAntEntity.this.ticksUntilCanFeed > 0) {
                return false;
            }
            if (AbstractAntEntity.this.hasClay()) {
                return false;
            }
            Optional<BlockPos> optional = this.getLeaves();
            if (optional.isPresent()) {
                AbstractAntEntity.this.leavesPos = optional.get();
                AbstractAntEntity.this.navigation.startMovingTo((double)AbstractAntEntity.this.leavesPos.getX() - 0.5f, (double)AbstractAntEntity.this.leavesPos.getY(), (double)AbstractAntEntity.this.leavesPos.getZ() - 0.5f, 0.5f);
                return true;
            }
            AbstractAntEntity.this.ticksUntilCanFeed = MathHelper.nextInt(AbstractAntEntity.this.random, 20, 60);
            return false;
        }

        @Override
        public boolean canAntContinue() {
            if (!this.running) {
                return false;
            }
            if (!AbstractAntEntity.this.hasLeaves()) {
                return false;
            }
            if (AbstractAntEntity.this.world.isRaining()) {
                return false;
            }
            if (this.completedNutrition()) {
                return AbstractAntEntity.this.random.nextFloat() < 0.2f;
            }
            if (AbstractAntEntity.this.age % 20 == 0 && !AbstractAntEntity.this.isLeaves(AbstractAntEntity.this.leavesPos)) {
                AbstractAntEntity.this.leavesPos = null;
                return false;
            }
            return true;
        }

        private boolean completedNutrition() {
            return this.pollinationTicks > 400;
        }

        boolean isRunning() {
            return this.running;
        }

        void cancel() {
            this.running = false;
        }

        @Override
        public void start() {
            this.pollinationTicks = 0;
            this.ticks = 0;
            this.lastNutritionTick = 0;
            this.running = true;
            AbstractAntEntity.this.resetNutritionTicks();
        }

        @Override
        public void stop() {
            if (this.completedNutrition()) {
                AbstractAntEntity.this.setHasClay(true);
            }
            this.running = false;
            AbstractAntEntity.this.navigation.stop();
            AbstractAntEntity.this.ticksUntilCanFeed = 200;
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            ++this.ticks;
            if (this.ticks > 600) {
                AbstractAntEntity.this.leavesPos = null;
                return;
            }
            Vec3d vec3d = Vec3d.ofBottomCenter(AbstractAntEntity.this.leavesPos).add(0.0, 0.6f, 0.0);
            if (vec3d.distanceTo(AbstractAntEntity.this.getPos()) > 1.0) {
                this.nextTarget = vec3d;
                this.moveToNextTarget();
                return;
            }
            if (this.nextTarget == null) {
                this.nextTarget = vec3d;
            }
            boolean bl = AbstractAntEntity.this.getPos().distanceTo(this.nextTarget) <= 0.1;
            boolean bl2 = true;
            if (!bl && this.ticks > 600) {
                AbstractAntEntity.this.leavesPos = null;
                return;
            }
            if (bl) {
                boolean bl3;
                boolean bl4 = bl3 = AbstractAntEntity.this.random.nextInt(25) == 0;
                if (bl3) {
                    this.nextTarget = new Vec3d(vec3d.getX() + (double)this.getRandomOffset(), vec3d.getY(), vec3d.getZ() + (double)this.getRandomOffset());
                    AbstractAntEntity.this.navigation.stop();
                } else {
                    bl2 = false;
                }
                AbstractAntEntity.this.getLookControl().lookAt(vec3d.getX(), vec3d.getY(), vec3d.getZ());
            }
            if (bl2) {
                this.moveToNextTarget();
            }
            ++this.pollinationTicks;
            if (AbstractAntEntity.this.random.nextFloat() < 0.05f && this.pollinationTicks > this.lastNutritionTick + 60) {
                this.lastNutritionTick = this.pollinationTicks;
                AbstractAntEntity.this.playSound(AntsSounds.ANT_SOUND_EVENT, 1.0f, 1.0f);
            }
        }

        private void moveToNextTarget() {
            AbstractAntEntity.this.getMoveControl().moveTo(this.nextTarget.getX(), this.nextTarget.getY(), this.nextTarget.getZ(), 0.5f);
        }

        private float getRandomOffset() {
            return (AbstractAntEntity.this.random.nextFloat() * 2.0f - 1.0f) * 0.33333334f;
        }

        private Optional<BlockPos> getLeaves() {
            return this.findLeaves(this.leafPredicate, 5.0);
        }

        private Optional<BlockPos> findLeaves(Predicate<BlockState> predicate, double searchDistance) {
            BlockPos blockPos = AbstractAntEntity.this.getBlockPos();
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            int i = 0;
            while ((double)i <= searchDistance) {
                int j = 0;
                while ((double)j < searchDistance) {
                    int k = 0;
                    while (k <= j) {
                        int l;
                        int n = l = k < j && k > -j ? j : 0;
                        while (l <= j) {
                            mutable.set(blockPos, k, i - 1, l);
                            if (blockPos.isWithinDistance(mutable, searchDistance) && predicate.test(AbstractAntEntity.this.world.getBlockState(mutable))) {
                                return Optional.of(mutable);
                            }
                            l = l > 0 ? -l : 1 - l;
                        }
                        k = k > 0 ? -k : 1 - k;
                    }
                    ++j;
                }
                i = i > 0 ? -i : 1 - i;
            }
            return Optional.empty();
        }
    }

    public class StingGoal
    extends MeleeAttackGoal {
        public StingGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
        }

        @Override
        public boolean canStart() {
            return super.canStart() && AbstractAntEntity.this.hasAngerTime();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && AbstractAntEntity.this.hasAngerTime();
        }
    }

    public class FindHiveGoal
    extends NotAngryGoal {
        public FindHiveGoal() {
        }

        @Override
        public boolean canAntStart() {
            return AbstractAntEntity.this.ticksLeftToFindHive == 0 && !AbstractAntEntity.this.hasHive() && AbstractAntEntity.this.canEnterHive();
        }

        @Override
        public boolean canAntContinue() {
            return false;
        }

        @Override
        public void start() {
            AbstractAntEntity.this.ticksLeftToFindHive = 200;
            List<BlockPos> list = this.getNearbyFreeHives();
            if (list.isEmpty()) {
                return;
            }
            for (BlockPos blockPos : list) {
                if (AbstractAntEntity.this.moveToHiveGoal.isPossibleHive(blockPos)) continue;
                AbstractAntEntity.this.hivePos = blockPos;
                return;
            }
            AbstractAntEntity.this.moveToHiveGoal.clearPossibleHives();
            AbstractAntEntity.this.hivePos = list.get(0);
        }

        private List<BlockPos> getNearbyFreeHives() {
            BlockPos blockPos = AbstractAntEntity.this.getBlockPos();
            PointOfInterestStorage pointOfInterestStorage = ((ServerWorld)AbstractAntEntity.this.world).getPointOfInterestStorage();
            Stream<PointOfInterest> stream = pointOfInterestStorage.getInCircle(poiType -> poiType.isIn(POITags.ANT_HOME), blockPos, 20, PointOfInterestStorage.OccupationStatus.ANY);
            return stream.map(PointOfInterest::getPos).filter(AbstractAntEntity.this::doesHiveHaveSpace).sorted(Comparator.comparingDouble(blockPos2 -> blockPos2.getSquaredDistance(blockPos))).collect(Collectors.toList());
        }
    }

    @Debug
    public class MoveToHiveGoal
    extends NotAngryGoal {
        public static final int field_30295 = 600;
        int ticks;
        private static final int field_30296 = 3;
        final List<BlockPos> possibleHives;
        @Nullable
        private Path path;
        private static final int field_30297 = 60;
        private int ticksUntilLost;

        public MoveToHiveGoal() {
            this.ticks = AbstractAntEntity.this.world.random.nextInt(10);
            this.possibleHives = Lists.newArrayList();
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canAntStart() {
            return AbstractAntEntity.this.hivePos != null && !AbstractAntEntity.this.hasPositionTarget() && AbstractAntEntity.this.canEnterHive() && !this.isCloseEnough(AbstractAntEntity.this.hivePos) && (AbstractAntEntity.this.world.getBlockState(AbstractAntEntity.this.hivePos).isOf(AntsBlocks.DIRT_ANT_NEST) || AbstractAntEntity.this.world.getBlockState(AbstractAntEntity.this.hivePos).isOf(AntsBlocks.SAND_ANT_NEST));
        }

        @Override
        public boolean canAntContinue() {
            return this.canAntStart();
        }

        @Override
        public void start() {
            this.ticks = 0;
            this.ticksUntilLost = 0;
            super.start();
        }

        @Override
        public void stop() {
            this.ticks = 0;
            this.ticksUntilLost = 0;
            AbstractAntEntity.this.navigation.stop();
            AbstractAntEntity.this.navigation.resetRangeMultiplier();
        }

        @Override
        public void tick() {
            if (AbstractAntEntity.this.hivePos == null) {
                return;
            }
            ++this.ticks;
            if (this.ticks > this.getTickCount(600)) {
                this.makeChosenHivePossibleHive();
                return;
            }
            if (AbstractAntEntity.this.navigation.isFollowingPath()) {
                return;
            }
            if (AbstractAntEntity.this.isWithinDistance(AbstractAntEntity.this.hivePos, 16)) {
                boolean bl = this.startMovingToFar(AbstractAntEntity.this.hivePos);
                if (!bl) {
                    this.makeChosenHivePossibleHive();
                } else if (this.path != null && AbstractAntEntity.this.navigation.getCurrentPath().equalsPath(this.path)) {
                    ++this.ticksUntilLost;
                    if (this.ticksUntilLost > 60) {
                        this.setLost();
                        this.ticksUntilLost = 0;
                    }
                } else {
                    this.path = AbstractAntEntity.this.navigation.getCurrentPath();
                }
                return;
            }
            if (AbstractAntEntity.this.isTooFar(AbstractAntEntity.this.hivePos)) {
                this.setLost();
                return;
            }
            AbstractAntEntity.this.startMovingTo(AbstractAntEntity.this.hivePos);
        }

        private boolean startMovingToFar(BlockPos pos) {
            AbstractAntEntity.this.navigation.setRangeMultiplier(10.0f);
            AbstractAntEntity.this.navigation.startMovingTo(pos.getX(), pos.getY(), pos.getZ(), 0.5);
            return AbstractAntEntity.this.navigation.getCurrentPath() != null && AbstractAntEntity.this.navigation.getCurrentPath().reachesTarget();
        }

        boolean isPossibleHive(BlockPos pos) {
            return this.possibleHives.contains(pos);
        }

        private void addPossibleHive(BlockPos pos) {
            this.possibleHives.add(pos);
            while (this.possibleHives.size() > 8) {
                this.possibleHives.remove(0);
            }
        }

        void clearPossibleHives() {
            this.possibleHives.clear();
        }

        private void makeChosenHivePossibleHive() {
            if (AbstractAntEntity.this.hivePos != null) {
                this.addPossibleHive(AbstractAntEntity.this.hivePos);
            }
            this.setLost();
        }

        private void setLost() {
            AbstractAntEntity.this.hivePos = null;
            AbstractAntEntity.this.ticksLeftToFindHive = 200;
        }

        private boolean isCloseEnough(BlockPos pos) {
            if (AbstractAntEntity.this.isWithinDistance(pos, 2)) {
                return true;
            }
            Path path = AbstractAntEntity.this.navigation.getCurrentPath();
            return path != null && path.getTarget().equals(pos) && path.reachesTarget() && path.isFinished();
        }
    }

    public class MoveToLeavesGoal
    extends NotAngryGoal {
        private static final int MAX_LEAVES_NAVIGATION_TICKS = 600;
        int ticks;

        public MoveToLeavesGoal() {
            this.ticks = AbstractAntEntity.this.world.random.nextInt(10);
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canAntStart() {
            return AbstractAntEntity.this.leavesPos != null && !AbstractAntEntity.this.hasPositionTarget() && this.shouldMoveToLeaf() && AbstractAntEntity.this.isLeaves(AbstractAntEntity.this.leavesPos) && !AbstractAntEntity.this.isWithinDistance(AbstractAntEntity.this.leavesPos, 2);
        }

        @Override
        public boolean canAntContinue() {
            return this.canAntStart();
        }

        @Override
        public void start() {
            this.ticks = 0;
            super.start();
        }

        @Override
        public void stop() {
            this.ticks = 0;
            AbstractAntEntity.this.navigation.stop();
            AbstractAntEntity.this.navigation.resetRangeMultiplier();
        }

        @Override
        public void tick() {
            if (AbstractAntEntity.this.leavesPos == null) {
                return;
            }
            ++this.ticks;
            if (this.ticks > this.getTickCount(600)) {
                AbstractAntEntity.this.leavesPos = null;
                return;
            }
            if (AbstractAntEntity.this.navigation.isFollowingPath()) {
                return;
            }
            if (AbstractAntEntity.this.isTooFar(AbstractAntEntity.this.leavesPos)) {
                AbstractAntEntity.this.leavesPos = null;
                return;
            }
            AbstractAntEntity.this.startMovingTo(AbstractAntEntity.this.leavesPos);
        }

        private boolean shouldMoveToLeaf() {
            return AbstractAntEntity.this.ticksSinceNutrition > 2400;
        }
    }

    public class AntWanderAroundGoal
    extends Goal {
        private static final int MAX_DISTANCE = 12;

        public AntWanderAroundGoal() {
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            return AbstractAntEntity.this.navigation.isIdle() && AbstractAntEntity.this.random.nextInt(10) == 0;
        }

        @Override
        public boolean shouldContinue() {
            return AbstractAntEntity.this.navigation.isFollowingPath();
        }

        @Override
        public void start() {
            Vec3d vec3d = this.getRandomLocation();
            if (vec3d != null) {
                AbstractAntEntity.this.navigation.startMovingAlong(AbstractAntEntity.this.navigation.findPathTo(new BlockPos(vec3d), 1), 1.0);
            }
        }

        @Nullable
        private Vec3d getRandomLocation() {
            Vec3d vec3d2;
            if (AbstractAntEntity.this.isHiveValid() && !AbstractAntEntity.this.isWithinDistance(AbstractAntEntity.this.hivePos, 22)) {
                Vec3d vec3d = Vec3d.ofCenter(AbstractAntEntity.this.hivePos);
                vec3d2 = vec3d.subtract(AbstractAntEntity.this.getPos()).normalize();
            } else {
                vec3d2 = AbstractAntEntity.this.getRotationVec(0.0f);
            }
            int i = 8;
            Vec3d vec3d3 = AboveGroundTargeting.find(AbstractAntEntity.this, 8, 7, vec3d2.x, vec3d2.z, 1.5707964f, 3, 1);
            if (vec3d3 != null) {
                return vec3d3;
            }
            return NoPenaltySolidTargeting.find(AbstractAntEntity.this, 8, 4, -2, vec3d2.x, vec3d2.z, 1.5707963705062866);
        }
    }

    public class AntRevengeGoal
    extends RevengeGoal {
        public AntRevengeGoal(AbstractAntEntity ant) {
            super(ant, new Class[0]);
        }

        @Override
        public boolean shouldContinue() {
            return AbstractAntEntity.this.hasAngerTime() && super.shouldContinue();
        }

        @Override
        protected void setMobEntityTarget(MobEntity mob, LivingEntity target) {
            if (mob instanceof AbstractAntEntity && this.mob.canSee(target)) {
                mob.setTarget(target);
            }
        }
    }

    public static class StingTargetGoal
    extends ActiveTargetGoal<PlayerEntity> {
        public StingTargetGoal(AbstractAntEntity ant) {
            super(ant, PlayerEntity.class, 10, true, false, ant::shouldAngerAt);
        }

        @Override
        public boolean canStart() {
            return super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            if (this.mob.getTarget() == null) {
                this.target = null;
                return false;
            }
            return super.shouldContinue();
        }
    }

    abstract class NotAngryGoal
    extends Goal {
        NotAngryGoal() {
        }

        public abstract boolean canAntStart();

        public abstract boolean canAntContinue();

        @Override
        public boolean canStart() {
            return this.canAntStart() && !AbstractAntEntity.this.hasAngerTime();
        }

        @Override
        public boolean shouldContinue() {
            return this.canAntContinue() && !AbstractAntEntity.this.hasAngerTime();
        }
    }
    
}
