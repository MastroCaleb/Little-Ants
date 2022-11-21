package toxican.caleb.ants.entities;

import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import toxican.caleb.ants.AntsMain;
import toxican.caleb.ants.more_ants_api.AntRegistry;

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
        String variant = AntRegistry.ANT_VARIANT.getId(object.getVariant()).getPath();
        return new Identifier(AntsMain.MOD_ID, "textures/entity/"+variant+"_ant/"+variant+"_ant.png");
    }
}