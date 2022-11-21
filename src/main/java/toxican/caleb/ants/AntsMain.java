package toxican.caleb.ants;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.poi.PointOfInterestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib3.GeckoLib;
import toxican.caleb.ants.blocks.AntsBlocks;
import toxican.caleb.ants.damage.AntsDamageSource;
import toxican.caleb.ants.enchantment.AntsEnchantments;
import toxican.caleb.ants.entities.AntsEntities;
import toxican.caleb.ants.features.bushes.AntsBushes;
import toxican.caleb.ants.features.bushes.nest_decorator.AntsDecorations;
import toxican.caleb.ants.items.AntsItems;
import toxican.caleb.ants.items.criteria.AllVariantsCriterion;
import toxican.caleb.ants.more_ants_api.AntRegistry;
import toxican.caleb.ants.more_ants_api.AntVariant;
import toxican.caleb.ants.particles.AntsParticles;
import toxican.caleb.ants.recipes.AntsRecipeTypes;
import toxican.caleb.ants.sounds.AntsSounds;

public class AntsMain implements ModInitializer {
	public static final TagKey<Biome> IS_SWAMP = of("is_swamp");
	public static final String MOD_ID = "ants";
	public static final Logger LOGGER = LoggerFactory.getLogger("Little Ants");
	public static final TrackedDataHandler<AntVariant> ANT_VARIANT = TrackedDataHandler.of(AntRegistry.ANT_VARIANT);
	public static final AllVariantsCriterion ALL_VARIANTS = Criteria.register(new AllVariantsCriterion());
	public static PointOfInterestType NEST;

	private static TagKey<Biome> of(String id) {
        return TagKey.of(Registry.BIOME_KEY, new Identifier(MOD_ID ,id));
    }

	@Override
	public void onInitialize() {
		LOGGER.info("Loading models...");
		GeckoLib.initialize();
		LOGGER.info("Loading ants...");
		AntsEntities.init();
		LOGGER.info("Loading items...");
		AntsItems.init();
		LOGGER.info("Loading blocks...");
		AntsBlocks.init();
		LOGGER.info("Loading particles...");
		AntsParticles.init();
		LOGGER.info("Loading points of interest...");
		NEST = PointOfInterestHelper.register(new Identifier("ants", "nest"), 0, 1, AntsBlocks.DIRT_ANT_NEST, AntsBlocks.SAND_ANT_NEST, AntsBlocks.MUD_ANT_NEST);
		LOGGER.info("Loading sounds...");
		AntsSounds.init();
		LOGGER.info("Loading bushes...");
		AntsBushes.init();
		LOGGER.info("Loading decorations...");
		AntsDecorations.init();
		LOGGER.info("Loading enchantments...");
		AntsEnchantments.init();
		LOGGER.info("Loading damage sources...");
		AntsDamageSource.init();
		LOGGER.info("Loading recipe types...");
		AntsRecipeTypes.init();
		LOGGER.info("Loading More Ants API...");
		TrackedDataHandlerRegistry.register(ANT_VARIANT);

		LOGGER.info("Loaded!");
	}
}
