package toxican.caleb.ants.entities.black_ant;

import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import toxican.caleb.ants.entities.AntsEntities;
import toxican.caleb.ants.entities.AntEntity;
import toxican.caleb.ants.items.AntsItems;

public class BlackAntEntity extends AntEntity{

    public BlackAntEntity(EntityType<? extends AnimalEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (player.getStackInHand(hand).getItem() == Items.GLASS_BOTTLE) {
            Item item = Items.GLASS_BOTTLE;
            EntityType<BlackAntEntity> ant = (EntityType<BlackAntEntity>) this.getType();

            if(ant == this.getType()){
                item=AntsItems.BLACK_ANT_BOTTLE;
            }

            ItemStack itemStack = new ItemStack(item);
            if (this.hasCustomName()) {
                itemStack.setCustomName(this.getCustomName());
            }

            if (!player.getAbilities().creativeMode) {
                if (player.getStackInHand(hand).getCount() > 1) {
                    player.getStackInHand(hand).decrement(1);
                    if (!player.getInventory().insertStack(itemStack)) {
                        player.dropItem(itemStack, true);
                    }
                } else {
                    player.setStackInHand(hand, itemStack);
                }
            } else {
                if (!player.getInventory().insertStack(itemStack)) {
                    player.dropItem(itemStack, true);
                }
            }

            this.getWorld().playSound(player, player.getBlockPos(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0f, 1.0f);
            this.discard();
            return ActionResult.SUCCESS;
        }

        return super.interactMob(player, hand);
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

}
