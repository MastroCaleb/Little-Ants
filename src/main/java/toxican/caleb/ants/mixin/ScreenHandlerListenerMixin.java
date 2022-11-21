package toxican.caleb.ants.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import toxican.caleb.ants.AntsMain;
import toxican.caleb.ants.items.ant_bottle.AntBottleItem;

@Mixin(ServerPlayerEntity.class)
public class ScreenHandlerListenerMixin {
    @Shadow
    private final ScreenHandlerListener screenHandlerListener = new ScreenHandlerListener(){

        @Override
        public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
            Slot slot = handler.getSlot(slotId);
            if (slot instanceof CraftingResultSlot) {
                return;
            }
            if (slot.inventory == ((ServerPlayerEntity)(Object)(ScreenHandlerListenerMixin.this)).getInventory()) {
                Criteria.INVENTORY_CHANGED.trigger(((ServerPlayerEntity)(Object)(ScreenHandlerListenerMixin.this)), ((ServerPlayerEntity)(Object)(ScreenHandlerListenerMixin.this)).getInventory(), stack);
                if(stack.getItem() instanceof AntBottleItem){
                    AntBottleItem item = (AntBottleItem)stack.getItem();
                    AntsMain.ALL_VARIANTS.trigger(((ServerPlayerEntity)(Object)(ScreenHandlerListenerMixin.this)), item.getVariant(), stack);
                }
            }
        }

        @Override
        public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
        }
    };

}
