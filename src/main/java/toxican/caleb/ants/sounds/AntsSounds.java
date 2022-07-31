package toxican.caleb.ants.sounds;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AntsSounds {

    public static final Identifier ANT_SOUND = new Identifier("ants:ant_sound");
    public static SoundEvent ANT_SOUND_EVENT = new SoundEvent(ANT_SOUND);

    public static final Identifier ANT_HURT = new Identifier("ants:ant_hurt");
    public static SoundEvent ANT_HURT_EVENT = new SoundEvent(ANT_HURT);

    public static final Identifier ENTER_NEST = new Identifier("ants:enter_nest");
    public static SoundEvent ENTER_NEST_EVENT = new SoundEvent(ENTER_NEST);

    public static final Identifier EXIT_NEST = new Identifier("ants:exit_nest");
    public static SoundEvent EXIT_NEST_EVENT = new SoundEvent(EXIT_NEST);
    
    public static void init(){
        Registry.register(Registry.SOUND_EVENT, ANT_SOUND, ANT_SOUND_EVENT);
        Registry.register(Registry.SOUND_EVENT, ANT_HURT, ANT_HURT_EVENT);
        Registry.register(Registry.SOUND_EVENT, ENTER_NEST, ENTER_NEST_EVENT);
        Registry.register(Registry.SOUND_EVENT, EXIT_NEST, EXIT_NEST_EVENT);
    }

}
