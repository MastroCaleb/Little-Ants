package toxican.caleb.ants.more_ants_api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BiomeTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import toxican.caleb.ants.AntsMain;
import toxican.caleb.ants.items.ant_bottle.AntBottleItem;

public record AntVariant(TagKey<Biome> spawnBiomeTag) {
    public static final AntVariant BROWN = AntVariant.register(AntsMain.MOD_ID, "brown", BiomeTags.IS_TAIGA);
    public static final AntVariant BLACK = AntVariant.register(AntsMain.MOD_ID, "black", BiomeTags.IS_SAVANNA);
    public static final AntVariant RED = AntVariant.register(AntsMain.MOD_ID, "red", BiomeTags.IS_FOREST);
    public static final AntVariant GOLD = AntVariant.register(AntsMain.MOD_ID, "gold", null); //GOLD ANTS CANT SPAWN NATURALLY
    public static final AntVariant MUDDY = AntVariant.register(AntsMain.MOD_ID,  "muddy", AntsMain.IS_SWAMP);

    public static AntVariant register(String namespace, String path, TagKey<Biome> spawnBiomeTag) {
        AntVariant variant = new AntVariant(spawnBiomeTag);
        AntBottleItem bottleItem = new AntBottleItem(variant, SoundEvents.ITEM_BOTTLE_EMPTY, new Item.Settings().maxCount(1).group(ItemGroup.MISC));
        Registry.register(Registry.ITEM, new Identifier(namespace, path+"_ant_in_a_bottle"), bottleItem);
        return Registry.register(AntRegistry.ANT_VARIANT, new Identifier(namespace, path), variant);
    }
}
