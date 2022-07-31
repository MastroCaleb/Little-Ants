package toxican.caleb.ants.items.ant_bottle;

import net.minecraft.item.Item;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import toxican.caleb.ants.entities.AntsEntities;
import toxican.caleb.ants.entities.red_ant.RedAntEntity;
import toxican.caleb.ants.items.AntsItems;

public class RedAntBottle extends Item {

    public RedAntBottle(Settings settings) {
        super(settings);
    }

    public static EntityType<RedAntEntity> getAntType(Item item) {
        EntityType<RedAntEntity> type = AntsEntities.RED_ANT;
        if(type==AntsEntities.RED_ANT){
            item=AntsItems.RED_ANT_BOTTLE;
        }
        return type;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        EntityType<RedAntEntity> type = getAntType(context.getStack().getItem());
        if (type != null) {
            RedAntEntity antEntity = new RedAntEntity(AntsEntities.RED_ANT, context.getWorld());

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
