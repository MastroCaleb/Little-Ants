package toxican.caleb.ants.items.ant_bottle;

import net.minecraft.item.Item;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import toxican.caleb.ants.entities.AntsEntities;
import toxican.caleb.ants.entities.AntEntity;
import toxican.caleb.ants.entities.black_ant.BlackAntEntity;
import toxican.caleb.ants.entities.brown_ant.BrownAntEntity;
import toxican.caleb.ants.entities.gold_ant.GoldAntEntity;
import toxican.caleb.ants.entities.red_ant.RedAntEntity;
import toxican.caleb.ants.items.AntsItems;

public class RandomAntBottle extends Item {

    public RandomAntBottle(Settings settings) {
        super(settings);
    }

    public static EntityType<? extends AntEntity> getAntType(Item item) {
        Random rand = new Random();
        int antType = rand.nextInt(4);
        EntityType<? extends AntEntity> type;
        if(antType==0){
            type = AntsEntities.RED_ANT;
        }
        else if(antType==1){
            type = AntsEntities.BROWN_ANT;
        }
        else if(antType==2){
            type = AntsEntities.BLACK_ANT;
        }
        else{
            type = AntsEntities.GOLD_ANT;
        }

        if(type!=null){
            item=AntsItems.RANDOM_ANT_BOTTLE;
        }
        return type;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText("item.ants.random_ant_bottle.tooltip").formatted(Formatting.DARK_RED));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        EntityType<? extends AntEntity> type = getAntType(context.getStack().getItem());
        if (type != null) {
            AntEntity antEntity;

            if(type == AntsEntities.RED_ANT){
                antEntity = new RedAntEntity(AntsEntities.RED_ANT, context.getWorld());
            }
            else if(type == AntsEntities.BROWN_ANT){
                antEntity = new BrownAntEntity(AntsEntities.BROWN_ANT, context.getWorld());
            }
            else if(type == AntsEntities.BLACK_ANT){
                antEntity = new BlackAntEntity(AntsEntities.BLACK_ANT, context.getWorld());
            }
            else{
                antEntity = new GoldAntEntity(AntsEntities.GOLD_ANT, context.getWorld());
            }

            BlockHitResult blockHitResult = BucketItem.raycast(context.getWorld(), context.getPlayer(), RaycastContext.FluidHandling.SOURCE_ONLY);
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getSide();
            BlockPos blockPos2 = blockPos.offset(direction);

            antEntity.setPosition(blockPos2.getX() + .5f, blockPos2.getY(), blockPos2.getZ() + .5f);
            antEntity.fromBottle = true;

            if (context.getStack().hasCustomName()) {
                antEntity.setCustomName(context.getStack().getName());
            }

            context.getWorld().playSound(context.getPlayer(), context.getBlockPos(), SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.NEUTRAL, 1.0f, 1.4f);
            context.getWorld().spawnEntity(antEntity);

            if (context.getPlayer() != null && !context.getPlayer().getAbilities().creativeMode) {
                context.getPlayer().setStackInHand(context.getHand(), new ItemStack(Items.GLASS_BOTTLE));
            }

            return ActionResult.SUCCESS;
        }

        return super.useOnBlock(context);
    }
}
