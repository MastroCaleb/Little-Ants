package toxican.caleb.ants.poi;

import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.poi.PointOfInterestType;

public class POITags {
    public static final TagKey<PointOfInterestType> ANT_HOME = POITags.of("ant_home");

    private POITags() {
    }

    private static TagKey<PointOfInterestType> of(String id) {
        return TagKey.of(Registry.POINT_OF_INTEREST_TYPE_KEY, new Identifier("ants", id));
    }
}
