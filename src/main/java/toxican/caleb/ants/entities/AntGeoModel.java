package toxican.caleb.ants.entities;

import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import toxican.caleb.ants.AntsMain;
import toxican.caleb.ants.more_ants_api.AntRegistry;
import toxican.caleb.ants.more_ants_api.AntVariant;

public class AntGeoModel extends AnimatedGeoModel<AntEntity>{

    @Override
    public Identifier getAnimationResource(AntEntity animatable) {
        return new Identifier(AntsMain.MOD_ID, "animations/ant.animation.json");
    }

    @Override
    public Identifier getModelResource(AntEntity object) {
        return new Identifier(AntsMain.MOD_ID, "geo/ants.geo.json");
    }

    @Override
    public Identifier getTextureResource(AntEntity object) {
        AntVariant variant = object.getVariant();
        String namespace = AntRegistry.ANT_VARIANT.getId(object.getVariant()).getNamespace();
        String path = AntRegistry.ANT_VARIANT.getId(variant).getPath();
        return new Identifier(namespace, "textures/entity/"+path+"_ant/"+path+"_ant.png");
    }
}