package toxican.caleb.ants.items.ant_bottle;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import toxican.caleb.ants.blocks.interfaces.Bottleable;
import net.minecraft.world.RaycastContext;

import java.util.List;

import org.jetbrains.annotations.Nullable;

public class AntBottleItem
extends Item {
    private final EntityType<?> entityType;
    private final SoundEvent emptyingSound;

    public AntBottleItem(EntityType<?> type, SoundEvent emptyingSound, Item.Settings settings) {
        super(settings);
        this.entityType = type;
        this.emptyingSound = emptyingSound;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult blockHitResult = BucketItem.raycast(world, user, RaycastContext.FluidHandling.NONE);
        if (blockHitResult.getType() == HitResult.Type.MISS) {
            return TypedActionResult.pass(itemStack);
        }
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getSide();
            BlockPos blockPos2 = blockPos.offset(direction);
            if (!world.canPlayerModifyAt(user, blockPos) || !user.canPlaceOn(blockPos2, direction, itemStack)) {
                return TypedActionResult.fail(itemStack);
            }
            else{
                this.onEmptied(user, world, itemStack, blockPos);
                return TypedActionResult.success(itemStack, true);
            }
        }
        return TypedActionResult.pass(itemStack);
    }

    public void onEmptied(@Nullable PlayerEntity player, World world, ItemStack stack, BlockPos pos) {
        if (world instanceof ServerWorld) {
            if (player != null && !player.getAbilities().creativeMode) {
                player.setStackInHand(player.getActiveHand(), new ItemStack(Items.GLASS_BOTTLE));
            }
            this.spawnEntity((ServerWorld)world, stack, pos);
            world.emitGameEvent((Entity)player, GameEvent.ENTITY_PLACE, pos);
        }
    }

    protected void playEmptyingSound(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos) {
        world.playSound(player, pos, this.emptyingSound, SoundCategory.NEUTRAL, 1.0f, 1.0f);
    }

    private void spawnEntity(ServerWorld world, ItemStack stack, BlockPos pos) {
        Entity entity = this.entityType.spawnFromItemStack(world, stack, null, pos, SpawnReason.BUCKET, true, false);
        if (entity instanceof Bottleable) {
            Bottleable bottleable = (Bottleable)((Object)entity);
            if (stack.hasCustomName()) {
                entity.setCustomName(stack.getName());
            }
            bottleable.copyDataFromNbt(stack.getOrCreateNbt());
            bottleable.setFromBottle(true);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbtCompound;
        BlockState blockState = null;
        if ((nbtCompound = stack.getNbt()) != null && nbtCompound.contains("HasClay")) {
            tooltip.add(Text.literal("Has Clay: true").formatted(Formatting.DARK_GRAY));
            if (nbtCompound.contains("carriedLeaf", 10)) {
                blockState = NbtHelper.toBlockState(nbtCompound.getCompound("carriedLeaf"));
                tooltip.add(Text.literal("Carrying Leaf of: " + I18n.translate(blockState.getBlock().asItem().getTranslationKey())).formatted(Formatting.DARK_GRAY));
            }
        }
    }
}
