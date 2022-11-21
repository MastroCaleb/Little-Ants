package toxican.caleb.ants.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import toxican.caleb.ants.AntsMain;
import toxican.caleb.ants.entities.AntsEntities;
import toxican.caleb.ants.items.clay_bottle.ClayBottleItem;
import toxican.caleb.ants.items.clay_bottle.clay_food.ClayBottleFoodComponent;

//Items registry class

public class AntsItems {

    public static final SpawnEggItem ANT_SPAWN_EGG = new SpawnEggItem(AntsEntities.ANT, 0x745434, 0x3b2717, new Item.Settings().maxCount(1).group(ItemGroup.MISC));

    public static final ClayBottleItem CLAY_BOTTLE = new ClayBottleItem(new Item.Settings().maxCount(16).group(ItemGroup.FOOD).food(ClayBottleFoodComponent.CLAY_BOTTLE_COMPONENT).recipeRemainder(Items.GLASS_BOTTLE));

    public static void init(){

        Registry.register(Registry.ITEM, new Identifier(AntsMain.MOD_ID, "ant_egg"), ANT_SPAWN_EGG);

        Registry.register(Registry.ITEM, new Identifier(AntsMain.MOD_ID, "clay_bottle"), CLAY_BOTTLE);
    }
    
}
