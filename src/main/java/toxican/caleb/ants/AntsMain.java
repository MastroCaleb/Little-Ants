package toxican.caleb.ants;

import net.fabricmc.api.ModInitializer;
import software.bernie.geckolib3.GeckoLib;
import toxican.caleb.ants.blocks.AntsBlocks;
import toxican.caleb.ants.damage.AntsDamageSource;
import toxican.caleb.ants.enchantment.AntsEnchantments;
import toxican.caleb.ants.entities.AntsEntities;
import toxican.caleb.ants.features.bushes.AntsBushes;
import toxican.caleb.ants.features.bushes.nest_decorator.AntsDecorations;
import toxican.caleb.ants.items.AntsItems;
import toxican.caleb.ants.particles.AntsParticles;
import toxican.caleb.ants.recipes.AntsRecipeTypes;
import toxican.caleb.ants.sounds.AntsSounds;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AntsMain implements ModInitializer {
	public static final String MOD_ID = "ants";
	public static final Logger LOGGER = LoggerFactory.getLogger("Little Ants");
	public static PointOfInterestType NEST;

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
		NEST = PointOfInterestHelper.register(new Identifier("ants", "nest"), 0, 1, AntsBlocks.DIRT_ANT_NEST, AntsBlocks.SAND_ANT_NEST);
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

		LOGGER.info("Loaded!");
	}
}
