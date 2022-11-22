package toxican.caleb.ants.entities;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import toxican.caleb.ants.AntsMain;

public class AntAngryGeoLayer extends GeoLayerRenderer<AntEntity> {
    private static final Identifier MODEL = new Identifier(AntsMain.MOD_ID, "geo/ants.geo.json");

    public AntAngryGeoLayer(IGeoRenderer<AntEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, AntEntity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderLayer cameo =  RenderLayer.getArmorCutoutNoCull(this.getAngryTexture(entityLivingBaseIn));
        matrixStackIn.push();
        //Move or scale the model as you see fit
        matrixStackIn.scale(1.0f, 1.0f, 1.0f);
        matrixStackIn.translate(0.0d, 0.0d, 0.0d);
        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, cameo, matrixStackIn, bufferIn,
                bufferIn.getBuffer(cameo), packedLightIn, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrixStackIn.pop();
    }

    public Identifier getAngryTexture(AntEntity ant){
        if(ant.hasAngerTime()){
            return new Identifier(AntsMain.MOD_ID, "textures/entity/angry.png");
        }
        return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/none.png");
    }
}
