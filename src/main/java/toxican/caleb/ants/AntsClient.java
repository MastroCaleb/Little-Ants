package toxican.caleb.ants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import toxican.caleb.ants.blocks.AntsBlocks;
import toxican.caleb.ants.entities.AntRenderer;
import toxican.caleb.ants.entities.AntsEntities;
import toxican.caleb.ants.particles.AntsParticles;
import toxican.caleb.ants.particles.dandelion.WindyDandelionParticle;

@Environment(EnvType.CLIENT)
public class AntsClient implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(AntsEntities.ANT, AntRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(AntsBlocks.POTTED_WINDY_DANDELION, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AntsBlocks.WINDY_DANDELION, RenderLayer.getCutout());
        ParticleFactoryRegistry.getInstance().register(AntsParticles.WINDY_DANDELION_PARTICLE, WindyDandelionParticle.Factory::new);
    }
}