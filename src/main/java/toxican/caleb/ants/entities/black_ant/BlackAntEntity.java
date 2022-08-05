package toxican.caleb.ants.entities.black_ant;

import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import toxican.caleb.ants.entities.AntsEntities;
import toxican.caleb.ants.blocks.interfaces.Bottleable;
import toxican.caleb.ants.entities.AntEntity;
import toxican.caleb.ants.items.AntsItems;

public class BlackAntEntity extends AntEntity implements Bottleable{

    private static final TrackedData<Boolean> FROM_BOTTLE = DataTracker.registerData(BlackAntEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public BlackAntEntity(EntityType<? extends AnimalEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(FROM_BOTTLE, false);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("FromBottle", this.isFromBottle());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        nbt.putBoolean("FromBottle", this.isFromBottle());
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        return Bottleable.tryBottle(player, hand, this).orElse(super.interactMob(player, hand));
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
    public AntEntity createChild(ServerWorld world, PassiveEntity passiveEntity) {
        Random rand = new Random();
        int goldChance = rand.nextInt(200);
        if(goldChance==46){
            return AntsEntities.GOLD_ANT.create(world);
        }
        return AntsEntities.BLACK_ANT.create(world);
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
        if (blockState != null) {
            nbtCompound.put("carriedLeaf", NbtHelper.fromBlockState(blockState));
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
        this.setLeaf(blockState);
    }

    @Override
    public ItemStack getBottleItem() {
        return AntsItems.BLACK_ANT_BOTTLE.getDefaultStack();
    }

    @Override
    public SoundEvent getBottledSound() {
        return SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH;
    }

}
