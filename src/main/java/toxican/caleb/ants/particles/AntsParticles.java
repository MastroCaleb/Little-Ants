package toxican.caleb.ants.particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AntsParticles {

    //Particle registry class

    public static final DefaultParticleType WINDY_DANDELION_PARTICLE = FabricParticleTypes.simple();
    

    public static void init(){

        Registry.register(Registry.PARTICLE_TYPE, new Identifier("ants", "windy_dandelion_particle"), WINDY_DANDELION_PARTICLE);
        
    }
}
