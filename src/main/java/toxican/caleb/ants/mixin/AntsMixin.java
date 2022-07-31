package toxican.caleb.ants.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import toxican.caleb.ants.blocks.AntsBlocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public class AntsMixin {
	@Inject(at = @At("RETURN"), method = "Lnet/minecraft/item/BoneMealItem;useOnFertilizable(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", cancellable = true)
	private static void init(ItemStack stack, World world, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
		BlockState blockState = world.getBlockState(pos);
		if(blockState.getBlock() == Blocks.DANDELION && world instanceof ServerWorld){
			world.setBlockState(pos, AntsBlocks.WINDY_DANDELION.getDefaultState(), Block.NOTIFY_ALL);
		}
		stack.decrement(1);
	}
}
