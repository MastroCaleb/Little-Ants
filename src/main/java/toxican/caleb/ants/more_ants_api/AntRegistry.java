package toxican.caleb.ants.more_ants_api;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import toxican.caleb.ants.AntsMain;

public class AntRegistry{

    public static final RegistryKey<Registry<AntVariant>> ANT_VARIANT_KEY = AntRegistry.createRegistryKey("ant_variant");
    public static final Registry<AntVariant> ANT_VARIANT = Registry.create(ANT_VARIANT_KEY, registry -> AntVariant.BROWN);
    
    private static <T> RegistryKey<Registry<T>> createRegistryKey(String registryId) {
        return RegistryKey.ofRegistry(new Identifier(AntsMain.MOD_ID, registryId));
    }
}
