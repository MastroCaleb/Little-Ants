package toxican.caleb.ants.features.bushes;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import toxican.caleb.ants.features.bushes.nest_decorator.BlackNestTreeDecorator;
import toxican.caleb.ants.features.bushes.nest_decorator.BrownNestTreeDecorator;
import toxican.caleb.ants.features.bushes.nest_decorator.RedNestTreeDecorator;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;

public class AntsBushes {

    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> OAK_BUSH =
        ConfiguredFeatures.register("oak_bush", Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(Blocks.OAK_LOG),
            new StraightTrunkPlacer(2, 0, 0),
            BlockStateProvider.of(Blocks.OAK_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 1),
            new TwoLayersFeatureSize(1, 0, 1)).decorators(Collections.singletonList(RedNestTreeDecorator.INSTANCE)).build());

    public static final RegistryEntry<PlacedFeature> OAK_CHECKED =
            PlacedFeatures.register("oak_bush_checked", OAK_BUSH,
            PlacedFeatures.wouldSurvive(Blocks.OAK_SAPLING));
        
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> OAK_SPAWN =
        ConfiguredFeatures.register("oak_bush_spawn", Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(new RandomFeatureEntry(OAK_CHECKED, 0.5f)),
            OAK_CHECKED));

    public static final RegistryEntry<PlacedFeature> OAK_BUSH_PLACED = PlacedFeatures.register("oak_bush_placed",
        OAK_SPAWN, VegetationPlacedFeatures.modifiers(
        PlacedFeatures.createCountExtraModifier(1, 0.0001f, 2)));

    //------

    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> SPRUCE_BUSH =
        ConfiguredFeatures.register("spruce_bush", Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(Blocks.SPRUCE_LOG),
            new StraightTrunkPlacer(2, 0, 0),
            BlockStateProvider.of(Blocks.SPRUCE_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 1),
            new TwoLayersFeatureSize(1, 0, 1)).decorators(Collections.singletonList(BrownNestTreeDecorator.INSTANCE)).build());

    public static final RegistryEntry<PlacedFeature> SPRUCE_CHECKED =
            PlacedFeatures.register("spruce_bush_checked", SPRUCE_BUSH,
            PlacedFeatures.wouldSurvive(Blocks.SPRUCE_SAPLING));
        
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> SPRUCE_SPAWN =
        ConfiguredFeatures.register("spruce_bush_spawn", Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(new RandomFeatureEntry(SPRUCE_CHECKED, 0.5f)),
            SPRUCE_CHECKED));

    public static final RegistryEntry<PlacedFeature> SPRUCE_BUSH_PLACED = PlacedFeatures.register("spruce_bush_placed",
        SPRUCE_SPAWN, VegetationPlacedFeatures.modifiers(
        PlacedFeatures.createCountExtraModifier(1, 0.0001f, 2)));

    //------

    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> ACACIA_BUSH =
        ConfiguredFeatures.register("acacia_bush", Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(Blocks.ACACIA_LOG),
            new StraightTrunkPlacer(2, 0, 0),
            BlockStateProvider.of(Blocks.ACACIA_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 1),
            new TwoLayersFeatureSize(1, 0, 1)).decorators(Collections.singletonList(BlackNestTreeDecorator.INSTANCE)).build());

    public static final RegistryEntry<PlacedFeature> ACACIA_CHECKED =
            PlacedFeatures.register("acacia_bush_checked", ACACIA_BUSH,
            PlacedFeatures.wouldSurvive(Blocks.ACACIA_SAPLING));
        
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> ACACIA_SPAWN =
        ConfiguredFeatures.register("acacia_bush_spawn", Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(new RandomFeatureEntry(ACACIA_CHECKED, 0.5f)),
        ACACIA_CHECKED));

    public static final RegistryEntry<PlacedFeature> ACACIA_BUSH_PLACED = PlacedFeatures.register("acacia_bush_placed",
        ACACIA_SPAWN, VegetationPlacedFeatures.modifiers(
        PlacedFeatures.createCountExtraModifier(1, 0.0001f, 2)));

    public static void init(){

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.PLAINS, BiomeKeys.FOREST),
        GenerationStep.Feature.VEGETAL_DECORATION, OAK_BUSH_PLACED.getKey().get());

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.TAIGA, BiomeKeys.SNOWY_TAIGA),
        GenerationStep.Feature.VEGETAL_DECORATION, SPRUCE_BUSH_PLACED.getKey().get());

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.SAVANNA, BiomeKeys.SAVANNA_PLATEAU, BiomeKeys.WINDSWEPT_SAVANNA),
        GenerationStep.Feature.VEGETAL_DECORATION, ACACIA_BUSH_PLACED.getKey().get());

    }
}