package toxican.caleb.ants.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import toxican.caleb.ants.AntsMain;
import toxican.caleb.ants.entities.AntsEntities;
import toxican.caleb.ants.items.ant_bottle.AntBottleItem;
import toxican.caleb.ants.items.clay_bottle.ClayBottleItem;
import toxican.caleb.ants.items.clay_bottle.clay_food.ClayBottleFoodComponent;

//Items registry class

public class AntsItems {

    public static final Item BROWN_ANT_BOTTLE = new AntBottleItem(AntsEntities.BROWN_ANT, SoundEvents.ITEM_BOTTLE_EMPTY,new Item.Settings().maxCount(1).group(ItemGroup.MISC));
    public static final Item RED_ANT_BOTTLE = new AntBottleItem(AntsEntities.RED_ANT, SoundEvents.ITEM_BOTTLE_EMPTY,new Item.Settings().maxCount(1).group(ItemGroup.MISC));
    public static final Item BLACK_ANT_BOTTLE = new AntBottleItem(AntsEntities.BLACK_ANT, SoundEvents.ITEM_BOTTLE_EMPTY,new Item.Settings().maxCount(1).group(ItemGroup.MISC));
    public static final Item GOLD_ANT_BOTTLE = new AntBottleItem(AntsEntities.GOLD_ANT, SoundEvents.ITEM_BOTTLE_EMPTY,new Item.Settings().maxCount(1).group(ItemGroup.MISC));
    public static final Item MUDDY_ANT_BOTTLE = new AntBottleItem(AntsEntities.MUDDY_ANT, SoundEvents.ITEM_BOTTLE_EMPTY,new Item.Settings().maxCount(1).group(ItemGroup.MISC));

    public static final SpawnEggItem BROWN_ANT_SPAWN_EGG = new SpawnEggItem(AntsEntities.BROWN_ANT, 0x745434, 0x3b2717, new Item.Settings().maxCount(1).group(ItemGroup.MISC));
    public static final SpawnEggItem BLACK_ANT_SPAWN_EGG = new SpawnEggItem(AntsEntities.BLACK_ANT, 0x3b332c, 0x2c2c24, new Item.Settings().maxCount(1).group(ItemGroup.MISC));
    public static final SpawnEggItem RED_ANT_SPAWN_EGG = new SpawnEggItem(AntsEntities.RED_ANT, 0xcc543c, 0xa43c44, new Item.Settings().maxCount(1).group(ItemGroup.MISC));
    public static final SpawnEggItem GOLD_ANT_SPAWN_EGG = new SpawnEggItem(AntsEntities.GOLD_ANT, 0xfcb424, 0xfca404, new Item.Settings().maxCount(1).group(ItemGroup.MISC));
    public static final SpawnEggItem MUDDY_ANT_SPAWN_EGG = new SpawnEggItem(AntsEntities.MUDDY_ANT, 0x3b2717, 0x745434, new Item.Settings().maxCount(1).group(ItemGroup.MISC));

    public static final ClayBottleItem CLAY_BOTTLE = new ClayBottleItem(new Item.Settings().maxCount(16).group(ItemGroup.FOOD).food(ClayBottleFoodComponent.CLAY_BOTTLE_COMPONENT).recipeRemainder(Items.GLASS_BOTTLE));

    public static void init(){
        Registry.register(Registry.ITEM, new Identifier(AntsMain.MOD_ID, "brown_ant_bottle"), BROWN_ANT_BOTTLE);
        Registry.register(Registry.ITEM, new Identifier(AntsMain.MOD_ID, "red_ant_bottle"), RED_ANT_BOTTLE);
        Registry.register(Registry.ITEM, new Identifier(AntsMain.MOD_ID, "black_ant_bottle"), BLACK_ANT_BOTTLE);
        Registry.register(Registry.ITEM, new Identifier(AntsMain.MOD_ID, "gold_ant_bottle"), GOLD_ANT_BOTTLE);
        Registry.register(Registry.ITEM, new Identifier(AntsMain.MOD_ID, "muddy_ant_bottle"), MUDDY_ANT_BOTTLE);

        Registry.register(Registry.ITEM, new Identifier(AntsMain.MOD_ID, "brown_ant_egg"), BROWN_ANT_SPAWN_EGG);
        Registry.register(Registry.ITEM, new Identifier(AntsMain.MOD_ID, "red_ant_egg"), RED_ANT_SPAWN_EGG);
        Registry.register(Registry.ITEM, new Identifier(AntsMain.MOD_ID, "black_ant_egg"), BLACK_ANT_SPAWN_EGG);
        Registry.register(Registry.ITEM, new Identifier(AntsMain.MOD_ID, "gold_ant_egg"), GOLD_ANT_SPAWN_EGG);
        Registry.register(Registry.ITEM, new Identifier(AntsMain.MOD_ID, "muddy_ant_egg"), MUDDY_ANT_SPAWN_EGG);

        Registry.register(Registry.ITEM, new Identifier(AntsMain.MOD_ID, "clay_bottle"), CLAY_BOTTLE);
    }
    
}
