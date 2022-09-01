package toxican.caleb.ants.features.bushes.sapling_gen;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import toxican.caleb.ants.features.bushes.AntsBushes;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class MangroveBushSaplingGen extends SaplingGenerator {
    @Nullable
    @Override
    protected RegistryEntry<? extends ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
        return AntsBushes.MANGROVE_BUSH;
    }
}
