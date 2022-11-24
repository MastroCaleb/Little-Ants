package toxican.caleb.ants.more_ants_api;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import toxican.caleb.ants.AntsMain;

import javax.annotation.Nullable;

/**
 * A predicate that tests if a given variant either matches, or is in the predicates tag
 * Think an ItemPredicate/FluidPredicate for ant variants
 * Can be used to match ant variants, like for recipes (is it this type of ant hill the player harvests?), or advancements (does the player breed a special type of ant?)
 */
public class AntVariantPredicate {
    
    public static final AntVariantPredicate ANY = new AntVariantPredicate(null, null);
    
    @Nullable private final TagKey<AntVariant> tag;
    @Nullable private final AntVariant variant;

    public AntVariantPredicate(@Nullable TagKey<AntVariant> tag, @Nullable AntVariant variant) {
        this.tag = tag;
        this.variant = variant;
    }
    
    /**
     * Tests if an ant variant matches this predicate
     * @param variant the ant variant to match
     * @return true if the variant matches the predicate
     */
    public boolean test(AntVariant variant) {
        if (this == ANY) {
            return true;
        }
        if (this.tag != null && !variant.isIn(this.tag)) {
            return false;
        }
        return this.variant == null || this.variant == variant;
    }
    
    /**
     * Reads an AntVariantPredicate from json
     * @param json the json element to read
     * @return the AntVariantPredicate
     */
    public static AntVariantPredicate fromJson(@Nullable JsonElement json) {
        if (json != null && !json.isJsonNull()) {
            JsonObject jsonObject = JsonHelper.asObject(json, "ant_variant");
            AntVariant variant = null;
            if (jsonObject.has("variant")) {
                Identifier identifier = new Identifier(JsonHelper.getString(jsonObject, "variant"));
                variant = AntRegistry.ANT_VARIANT.get(identifier);
            }

            TagKey<AntVariant> tagKey = null;
            if (jsonObject.has("tag")) {
                Identifier identifier2 = new Identifier(JsonHelper.getString(jsonObject, "tag"));
                tagKey = TagKey.of(AntRegistry.ANT_VARIANT_KEY, identifier2);
            }
            return new AntVariantPredicate(tagKey, variant);
        } else {
            return ANY;
        }
    }
    
    /**
     * Writes this AntVariantPredicate to json
     * @return the json
     */
    public JsonElement toJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject jsonObject = new JsonObject();
            if (this.variant != null) {
                jsonObject.addProperty("ant_variant", AntRegistry.ANT_VARIANT.getId(this.variant).toString());
            }
            if (this.tag != null) {
                jsonObject.addProperty("tag", this.tag.id().toString());
            }
            return jsonObject;
        }
    }
    
    /**
     * Writes this AntVariantPredicate to a packetByteBuf to sync over network (usually server => client)
     * @param packetByteBuf the buffer to write to
     */
    public void write(PacketByteBuf packetByteBuf) {
        if (this.tag == null) {
            packetByteBuf.writeBoolean(false);
        } else {
            packetByteBuf.writeBoolean(true);
            packetByteBuf.writeIdentifier(this.tag.id());
        }
        if (this.variant == null) {
            packetByteBuf.writeBoolean(false);
        } else {
            packetByteBuf.writeBoolean(true);
            packetByteBuf.writeIdentifier(AntRegistry.ANT_VARIANT.getId(this.variant));
        }
    }
    
    /**
     * Reads an AntVariantPredicate from a packet byte buffer
     * @param packetByteBuf the buffer to read
     * @return the AntVariantPredicate
     */
    public static AntVariantPredicate fromPacket(PacketByteBuf packetByteBuf) {
        TagKey<AntVariant> tag = null;
        AntVariant variant = null;
        
        boolean hasTag = packetByteBuf.readBoolean();
        if(hasTag) {
            tag = TagKey.of(AntRegistry.ANT_VARIANT_KEY, packetByteBuf.readIdentifier());
        }
        boolean hasVariant = packetByteBuf.readBoolean();
        if(hasVariant) {
            variant = AntRegistry.ANT_VARIANT.get(packetByteBuf.readIdentifier());
        }
        return new AntVariantPredicate(tag, variant);
    }
    
    /**
     * Builder to create an AntVariantPredicate
     */
    public static class Builder {
        @Nullable
        private AntVariant variant;
        @Nullable
        private TagKey<AntVariant> tag;

        private Builder() { }

        public static Builder create() {
            return new Builder();
        }

        public Builder variant(AntVariant variant) {
            this.variant = variant;
            return this;
        }

        public Builder tag(TagKey<AntVariant> tag) {
            this.tag = tag;
            return this;
        }

        public AntVariantPredicate build() {
            return new AntVariantPredicate(this.tag, this.variant);
        }
    }
    
}
