package toxican.caleb.ants.more_ants_api;

import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import toxican.caleb.ants.AntsMain;

/**
 * A group of ant variants (think item tags, but for ants)
 * This way you can create recipes / advancements and more that matches on a specific group of ants
 * like the builtin ones, ones from a specific mod that adds integration, all ant variants that are living in plains, ...
 */
public class AntVariantTags {
	
	public static TagKey<AntVariant> BUILTIN;
	
	private static TagKey<AntVariant> of(Identifier id) {
		return TagKey.of(AntRegistry.ANT_VARIANT_KEY, id);
	}
	
	public static void init() {
		BUILTIN = of(new Identifier(AntsMain.MOD_ID, "builtin"));
	}
	
}
