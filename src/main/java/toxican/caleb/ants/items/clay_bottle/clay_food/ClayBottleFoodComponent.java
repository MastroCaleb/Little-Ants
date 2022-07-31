package toxican.caleb.ants.items.clay_bottle.clay_food;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponent.Builder;

public class ClayBottleFoodComponent {

    private static final Builder HUNGER = (new FoodComponent.Builder()).saturationModifier(0.2f).hunger(0).alwaysEdible();
    public static final FoodComponent CLAY_BOTTLE_COMPONENT = HUNGER.build();

}