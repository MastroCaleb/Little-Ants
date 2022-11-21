package toxican.caleb.ants.entities;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import toxican.caleb.ants.AntsMain;
import toxican.caleb.ants.blocks.interfaces.Bottleable;
import toxican.caleb.ants.blocks.nest.AntNestEntity;
import toxican.caleb.ants.items.ant_bottle.AntBottleItem;
import toxican.caleb.ants.more_ants_api.AntRegistry;
import toxican.caleb.ants.more_ants_api.AntVariant;

public class AntEntity extends AbstractAntEntity implements Bottleable{

    private static final TrackedData<Boolean> FROM_BOTTLE = DataTracker.registerData(AntEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<AntVariant> VARIANT = DataTracker.registerData(AntEntity.class, AntsMain.ANT_VARIANT);
    public static final String VARIANT_KEY = "Variant";
    public static final List<AntVariant> variants = AntRegistry.ANT_VARIANT.stream().toList();
    public static final List<Item> items = Registry.ITEM.stream().toList();

    public AntEntity(EntityType<? extends AnimalEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new EnterHiveGoal());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, AntVariant.BROWN);
        this.dataTracker.startTracking(FROM_BOTTLE, false);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString(VARIANT_KEY, AntRegistry.ANT_VARIANT.getId(this.getVariant()).toString());
        nbt.putBoolean("FromBottle", this.isFromBottle());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        AntVariant antVariant = AntRegistry.ANT_VARIANT.get(Identifier.tryParse(nbt.getString(VARIANT_KEY)));
        if (antVariant != null) {
            this.setVariant(antVariant);
        }
        this.setFromBottle(nbt.getBoolean("FromBottle"));
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        return Bottleable.tryBottle(player, hand, this).orElse(super.interactMob(player, hand));
    }

    public AntVariant getVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public void setVariant(AntVariant variant) {
        this.dataTracker.set(VARIANT, variant);
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

    @Override
    public AbstractAntEntity createChild(ServerWorld world, PassiveEntity passiveEntity) {
        Random rand = new Random();
        AntEntity antEntity = AntsEntities.ANT.create(world);
        int goldChance = rand.nextInt(200);
        if(goldChance==46){
            antEntity.setVariant(AntVariant.GOLD);
            return antEntity;
        }
        antEntity.setVariant(this.getVariant());
        return antEntity;
    }

    @Override
    public boolean isFromBottle() {
        return this.dataTracker.get(FROM_BOTTLE);
    }

    @Override
    public void setFromBottle(boolean var1) {
        this.dataTracker.set(FROM_BOTTLE, fromBottle);
    }

    @Override
    public void copyDataToStack(ItemStack stack) {
        Bottleable.copyDataToStack(this, stack);
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putInt("Age", this.getBreedingAge());
        if(this.hasClay()){
            nbtCompound.putBoolean("HasClay", this.hasClay());
        }
        BlockState blockState = this.getLeaf();
        if(blockState != null) {
            nbtCompound.put("carriedLeaf", NbtHelper.fromBlockState(blockState));
        }
        AntVariant antVariant = this.getVariant();
        if(antVariant != null){
            nbtCompound.putString(VARIANT_KEY, AntRegistry.ANT_VARIANT.getId(this.getVariant()).toString());
        }
    }

    @Override
    public void copyDataFromNbt(NbtCompound nbt) {
        Bottleable.copyDataFromNbt(this, nbt);
        if (nbt.contains("Age")) {
            this.setBreedingAge(nbt.getInt("Age"));
        }
        if (nbt.contains("HasClay")) {
            this.setHasClay(nbt.getBoolean("HasClay"));
        }
        BlockState blockState = null;
        if (nbt.contains("carriedLeaf", 10) && (blockState = NbtHelper.toBlockState(nbt.getCompound("carriedLeaf"))).isAir()) {
            blockState = null;
        }
        if (nbt.contains(VARIANT_KEY)) {
            this.setVariant(AntRegistry.ANT_VARIANT.get(Identifier.tryParse(nbt.getString(VARIANT_KEY))));
        }
        this.setLeaf(blockState);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        RegistryEntry<Biome> biome = world.getBiome(this.getBlockPos());
        if(!this.isFromBottle()){
            for(int i = 0; i<variants.size(); i++){
                if(variants.get(i).spawnBiomeTag() != null){
                    if(biome.isIn(variants.get(i).spawnBiomeTag())){
                        this.setVariant(variants.get(i));
                    }
                }
            }
        }
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public ItemStack getBottleItem() {
        for(int i = 0; i<items.size(); i++){
            if(items.get(i) != null){
                if(items.get(i) instanceof AntBottleItem antBottle && antBottle.getVariant().equals(this.getVariant())){
                    return antBottle.getDefaultStack();
                }
            }
        }
        return null;
    }

    @Override
    public SoundEvent getBottledSound() {
        return SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH;
    }

    public class EnterHiveGoal
    extends NotAngryGoal {
        public EnterHiveGoal() {
        }

        @Override
        public boolean canAntStart() {
            BlockEntity blockEntity;
            if (AntEntity.this.hasHive() && AntEntity.this.canEnterHive() && AntEntity.this.hivePos.isWithinDistance(AntEntity.this.getPos(), 2.0) && (blockEntity = AntEntity.this.world.getBlockEntity(AntEntity.this.hivePos)) instanceof AntNestEntity) {
                AntNestEntity colonyBlockEntity = (AntNestEntity)blockEntity;
                if (colonyBlockEntity.isFullOfAnts()) {
                    AntEntity.this.hivePos = null;
                } else {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canAntContinue() {
            return false;
        }

        @Override
        public void start() {
            BlockEntity blockEntity = AntEntity.this.world.getBlockEntity(AntEntity.this.hivePos);
            if (blockEntity instanceof AntNestEntity) {
                AntNestEntity anthiveBlockEntity = (AntNestEntity)blockEntity;
                anthiveBlockEntity.tryEnterHive(AntEntity.this, AntEntity.this.hasClay(), AntEntity.this.getVariant(), AntEntity.this.isFromBottle());
            }
        }
    }
}
