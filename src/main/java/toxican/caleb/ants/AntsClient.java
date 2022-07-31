package toxican.caleb.ants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import toxican.caleb.ants.blocks.AntsBlocks;
import toxican.caleb.ants.entities.AntsEntities;
import toxican.caleb.ants.entities.black_ant.BlackAntRenderer;
import toxican.caleb.ants.entities.brown_ant.BrownAntRenderer;
import toxican.caleb.ants.entities.gold_ant.GoldAntRenderer;
import toxican.caleb.ants.entities.red_ant.RedAntRenderer;
import toxican.caleb.ants.particles.AntsParticles;
import toxican.caleb.ants.particles.dandelion.WindyDandelionParticle;

@Environment(EnvType.CLIENT)
public class AntsClient implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(AntsEntities.BROWN_ANT, BrownAntRenderer::new);
        EntityRendererRegistry.register(AntsEntities.RED_ANT, RedAntRenderer::new);
        EntityRendererRegistry.register(AntsEntities.BLACK_ANT, BlackAntRenderer::new);
        EntityRendererRegistry.register(AntsEntities.GOLD_ANT, GoldAntRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(AntsBlocks.POTTED_WINDY_DANDELION, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AntsBlocks.WINDY_DANDELION, RenderLayer.getCutout());
        ParticleFactoryRegistry.getInstance().register(AntsParticles.WINDY_DANDELION_PARTICLE, WindyDandelionParticle.Factory::new);
    }
    
}
