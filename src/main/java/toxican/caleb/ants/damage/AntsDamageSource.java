package toxican.caleb.ants.damage;

import net.minecraft.entity.damage.DamageSource;

public class AntsDamageSource extends DamageSource{

    protected AntsDamageSource(String name) {
        super(name);
    }

    public static DamageSource NEST = new AntsDamageSource("nest").setNeutral().setScaledWithDifficulty();

    public static DamageSource CLAY_BOTTLE = new AntsDamageSource("clay_bottle").setNeutral().setScaledWithDifficulty();

    public static final void init(){

        //Something's wrong i can feel it

    }
    
}
