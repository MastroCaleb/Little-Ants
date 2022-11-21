package toxican.caleb.ants.blocks.nest;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import toxican.caleb.ants.blocks.nest.AntNestDestoyedCriterion.Conditions;

public class AntNestDestoyedCriterion
extends AbstractCriterion<Conditions> {
    static final Identifier ID = new Identifier("ant_nest_destroyed");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        Block block = AntNestDestoyedCriterion.getBlock(jsonObject);
        ItemPredicate itemPredicate = ItemPredicate.fromJson(jsonObject.get("item"));
        NumberRange.IntRange intRange = NumberRange.IntRange.fromJson(jsonObject.get("num_ants_inside"));
        return new Conditions(extended, block, itemPredicate, intRange);
    }

    @Nullable
    private static Block getBlock(JsonObject root) {
        if (root.has("block")) {
            Identifier identifier = new Identifier(JsonHelper.getString(root, "block"));
            return Registry.BLOCK.getOrEmpty(identifier).orElseThrow(() -> new JsonSyntaxException("Unknown block type '" + identifier + "'"));
        }
        return null;
    }

    public void trigger(ServerPlayerEntity player, BlockState state, ItemStack stack, int antCount) {
        this.trigger(player, conditions -> conditions.test(state, stack, antCount));
    }

    public static class Conditions
    extends AbstractCriterionConditions {
        @Nullable
        private final Block block;
        private final ItemPredicate item;
        private final NumberRange.IntRange antCount;

        public Conditions(EntityPredicate.Extended player, @Nullable Block block, ItemPredicate item, NumberRange.IntRange antCount) {
            super(ID, player);
            this.block = block;
            this.item = item;
            this.antCount = antCount;
        }

        public static Conditions create(Block block, ItemPredicate.Builder itemPredicateBuilder, NumberRange.IntRange antCountRange) {
            return new Conditions(EntityPredicate.Extended.EMPTY, block, itemPredicateBuilder.build(), antCountRange);
        }

        public boolean test(BlockState state, ItemStack stack, int count) {
            if (this.block != null && !state.isOf(this.block)) {
                return false;
            }
            if (!this.item.test(stack)) {
                return false;
            }
            return this.antCount.test(count);
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            if (this.block != null) {
                jsonObject.addProperty("block", Registry.BLOCK.getId(this.block).toString());
            }
            jsonObject.add("item", this.item.toJson());
            jsonObject.add("num_ants_inside", this.antCount.toJson());
            return jsonObject;
        }
    }
}
