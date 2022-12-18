package toxican.caleb.ants.more_ants_api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BiomeTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import toxican.caleb.ants.AntsMain;
import toxican.caleb.ants.items.ant_bottle.AntBottleItem;

public class AntVariant {
	
	public static final AntVariant BROWN = AntVariant.register(AntsMain.MOD_ID, "brown", BiomeTags.IS_TAIGA, Rarity.COMMON);
	public static final AntVariant BLACK = AntVariant.register(AntsMain.MOD_ID, "black", BiomeTags.IS_SAVANNA, Rarity.COMMON);
	public static final AntVariant RED = AntVariant.register(AntsMain.MOD_ID, "red", BiomeTags.IS_FOREST, Rarity.COMMON);
	public static final AntVariant GOLD = AntVariant.register(AntsMain.MOD_ID, "gold", null, Rarity.UNCOMMON); //GOLD ANTS CANT SPAWN NATURALLY SO NO BIOME TAG
	public static final AntVariant MUDDY = AntVariant.register(AntsMain.MOD_ID, "muddy", AntsMain.IS_SWAMP, Rarity.COMMON);
	
	public static void init() {
	
	}
	
	public static AntVariant register(String namespace, String path, TagKey<Biome> spawnBiomeTag, Rarity rarity) {
		AntVariant variant = new AntVariant(spawnBiomeTag, rarity);
		AntBottleItem bottleItem = new AntBottleItem(variant, SoundEvents.ITEM_BOTTLE_EMPTY, new Item.Settings().maxCount(1).group(ItemGroup.MISC).rarity(rarity));
		Registry.register(Registry.ITEM, new Identifier(namespace, path + "_ant_in_a_bottle"), bottleItem);
		return Registry.register(AntRegistry.ANT_VARIANT, new Identifier(namespace, path), variant);
	}
	
	private final TagKey<Biome> spawnBiomeTag;
	private final Rarity rarity;
	private final RegistryEntry.Reference<AntVariant> registryEntry;
	
	public AntVariant(TagKey<Biome> spawnBiomeTag, Rarity rarity) {
		this.spawnBiomeTag = spawnBiomeTag;
		this.rarity = rarity;
		this.registryEntry = AntRegistry.ANT_VARIANT.createEntry(this);
	}
	
	public TagKey<Biome> getSpawnBiomeTag() {
		return spawnBiomeTag;
	}

	public Rarity getRarity(){
		return rarity;
	}
	
	public RegistryEntry.Reference<AntVariant> getRegistryEntry() {
		return this.registryEntry;
	}
	
	public boolean isIn(TagKey<AntVariant> tag) {
		return this.getRegistryEntry().isIn(tag);
	}
	
	
}
