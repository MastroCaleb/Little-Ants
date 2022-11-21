package toxican.caleb.ants.items.criteria;

import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import toxican.caleb.ants.items.ant_bottle.AntBottleItem;
import toxican.caleb.ants.more_ants_api.AntRegistry;
import toxican.caleb.ants.more_ants_api.AntVariant;
import toxican.caleb.ants.items.criteria.AllVariantsCriterion.Conditions;

public class AllVariantsCriterion
extends AbstractCriterion<Conditions> {
    static final Identifier ID = new Identifier("all_variants");
    public static final List<AntVariant> variants = AntRegistry.ANT_VARIANT.stream().toList();
    static int test = 0;

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        ItemPredicate itemPredicate = ItemPredicate.fromJson(jsonObject.get("item"));
        AntVariant antVariant = AllVariantsCriterion.getAntVariant(jsonObject);
        return new Conditions(extended, antVariant, itemPredicate);
    }

    @Nullable
    private static AntVariant getAntVariant(JsonObject root) {
        if (root.has("item")){
            AntBottleItem antBottleItem = (AntBottleItem) JsonHelper.getItem(root, "item");
            Identifier variantId = AntRegistry.ANT_VARIANT.getId(antBottleItem.getVariant());
            return AntRegistry.ANT_VARIANT.getOrEmpty(variantId).orElseThrow(() -> new JsonSyntaxException("Unknown variant type '" + variantId + "'"));
        }
        return null;
    }

    public void trigger(ServerPlayerEntity player, AntVariant antVariant, ItemStack stack) {
        this.trigger(player, conditions -> conditions.test(antVariant, stack));
    }

    public static class Conditions
    extends AbstractCriterionConditions {
        @Nullable
        private final AntVariant antVariant;
        private final ItemPredicate item;

        public Conditions(EntityPredicate.Extended player, AntVariant antVariant, ItemPredicate item) {
            super(ID, player);
            this.antVariant = antVariant;
            this.item = item;
        }

        public static Conditions create(AntVariant antVariant, ItemPredicate.Builder itemPredicateBuilder) {
            return new Conditions(EntityPredicate.Extended.EMPTY, antVariant, itemPredicateBuilder.build());
        }

        public boolean test(AntVariant antVariant, ItemStack stack) {
            if (this.antVariant != null){
                for(int i = 0; i<variants.size(); i++){
                    if(variants.get(i) != null){
                        if(antVariant == variants.get(i)){
                            test++;
                        }
                    }
                }
            }
            if (!this.item.test(stack)){
                return false;
            }
            return test==variants.size();
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            if (this.antVariant != null) {
                jsonObject.addProperty("variant", AntRegistry.ANT_VARIANT.getId(this.antVariant).toString());
            }
            jsonObject.add("item", this.item.toJson());
            return jsonObject;
        }
    }
}
