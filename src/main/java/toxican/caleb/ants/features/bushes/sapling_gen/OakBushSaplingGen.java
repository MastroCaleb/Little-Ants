package toxican.caleb.ants.features.bushes.sapling_gen;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import toxican.caleb.ants.features.bushes.AntsBushes;

import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class OakBushSaplingGen extends SaplingGenerator {
    @Nullable
    @Override
    protected RegistryEntry<? extends ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
        return AntsBushes.OAK_BUSH;
    }
}
