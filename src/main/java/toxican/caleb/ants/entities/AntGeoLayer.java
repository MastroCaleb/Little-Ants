package toxican.caleb.ants.entities;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import toxican.caleb.ants.AntsMain;
import toxican.caleb.ants.blocks.NestTag;

public class AntGeoLayer extends GeoLayerRenderer<AntEntity> {
    private static final Identifier MODEL = new Identifier(AntsMain.MOD_ID, "geo/ants.geo.json");

    public AntGeoLayer(IGeoRenderer<AntEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, AntEntity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderLayer cameo =  RenderLayer.getArmorCutoutNoCull(this.getLeafTexture(entityLivingBaseIn));
        matrixStackIn.push();
        //Move or scale the model as you see fit
        matrixStackIn.scale(1.0f, 1.0f, 1.0f);
        matrixStackIn.translate(0.0d, 0.0d, 0.0d);
        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, cameo, matrixStackIn, bufferIn,
                bufferIn.getBuffer(cameo), packedLightIn, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrixStackIn.pop();
    }

    public Identifier getLeafTexture(AntEntity ant){
        if(ant.hasClay() && ant.getLeaf() != null){
            if(ant.getLeaf().isIn(NestTag.FLOWERING_AZALEA_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/flowering.png");
            }
            else if(ant.getLeaf().isIn(NestTag.AZALEA_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/azalea.png");
            }
            else if(ant.getLeaf().isIn(NestTag.OAK_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/oak.png");
            }
            else if(ant.getLeaf().isIn(NestTag.DARK_OAK_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/dark.png");
            }
            else if(ant.getLeaf().isIn(NestTag.BIRCH_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/birch.png");
            }
            else if(ant.getLeaf().isIn(NestTag.ACACIA_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/acacia.png");
            }
            else if(ant.getLeaf().isIn(NestTag.SPRUCE_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/spruce.png");
            }
            else if(ant.getLeaf().isIn(NestTag.JUNGLE_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/jungle.png");
            }
            else if(ant.getLeaf().isIn(NestTag.MANGROVE_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/mangrove.png");
            }
            else if(ant.getLeaf().isIn(NestTag.OAK_BLACK_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/black_oak.png");
            }
            else if(ant.getLeaf().isIn(NestTag.OAK_BLUE_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/blue_oak.png");
            }
            else if(ant.getLeaf().isIn(NestTag.OAK_BROWN_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/brown_oak.png");
            }
            else if(ant.getLeaf().isIn(NestTag.OAK_CYAN_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/cyan_oak.png");
            }
            else if(ant.getLeaf().isIn(NestTag.OAK_GRAY_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/gray_oak.png");
            }
            else if(ant.getLeaf().isIn(NestTag.OAK_GREEN_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/green_oak.png");
            }
            else if(ant.getLeaf().isIn(NestTag.OAK_LIGHT_BLUE_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/light_blue_oak.png");
            }
            else if(ant.getLeaf().isIn(NestTag.OAK_LIGHT_GRAY_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/light_gray_oak.png");
            }
            else if(ant.getLeaf().isIn(NestTag.OAK_LIME_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/lime_oak.png");
            }
            else if(ant.getLeaf().isIn(NestTag.OAK_MAGENTA_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/magenta_oak.png");
            }
            else if(ant.getLeaf().isIn(NestTag.OAK_ORANGE_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/orange_oak.png");
            }
            else if(ant.getLeaf().isIn(NestTag.OAK_PINK_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/pink_oak.png");
            }
            else if(ant.getLeaf().isIn(NestTag.OAK_PURPLE_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/purple_oak.png");
            }
            else if(ant.getLeaf().isIn(NestTag.OAK_RED_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/red_oak.png");
            }
            else if(ant.getLeaf().isIn(NestTag.OAK_WHITE_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/white_oak.png");
            }
            else if(ant.getLeaf().isIn(NestTag.OAK_YELLOW_LEAF_FOOD)){
                return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/yellow_oak.png");
            }
            return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/"+Registry.BLOCK.getId(ant.getLeaf().getBlock()).getNamespace()+"_"+Registry.BLOCK.getId(ant.getLeaf().getBlock()).getPath()+".png");
        }
        return new Identifier(AntsMain.MOD_ID, "textures/entity/leaves/none.png");
    }
}
