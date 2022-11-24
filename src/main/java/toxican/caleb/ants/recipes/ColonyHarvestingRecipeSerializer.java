package toxican.caleb.ants.recipes;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import toxican.caleb.ants.AntsMain;
import toxican.caleb.ants.more_ants_api.AntVariantPredicate;

public class ColonyHarvestingRecipeSerializer implements RecipeSerializer<ColonyHarvestingRecipe> {
	
	public final RecipeFactory<ColonyHarvestingRecipe> recipeFactory;
	
	public ColonyHarvestingRecipeSerializer(RecipeFactory<ColonyHarvestingRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	@Override
	public ColonyHarvestingRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = JsonHelper.getString(jsonObject, "group", "");
		
		AntVariantPredicate antVariantPredicate = AntVariantPredicate.ANY;
		if(jsonObject.has("ant_variant")) {
			antVariantPredicate = AntVariantPredicate.fromJson(JsonHelper.getObject(jsonObject, "ant_variant"));
		}
		
		Ingredient hiveIngredient = Ingredient.EMPTY;
		if(jsonObject.has("hive_ingredient")) {
			hiveIngredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "hive_ingredient"));
		}
		
		Ingredient handIngredient = Ingredient.EMPTY;
		if(jsonObject.has("hand_ingredient")) {
			handIngredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "hand_ingredient"));
		}
		int handIngredientCount = JsonHelper.getInt(jsonObject, "hand_ingredient_count", 1);
		ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
		
		return this.recipeFactory.create(identifier, group, antVariantPredicate, hiveIngredient, handIngredient, handIngredientCount, output);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, ColonyHarvestingRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		recipe.antVariantPredicate.write(packetByteBuf);
		recipe.hiveIngredient.write(packetByteBuf);
		recipe.handIngredient.write(packetByteBuf);
		packetByteBuf.writeInt(recipe.handIngredientCount);
		packetByteBuf.writeItemStack(recipe.output);
	}
	
	@Override
	public ColonyHarvestingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		AntVariantPredicate antVariantPredicate = AntVariantPredicate.fromPacket(packetByteBuf);
		Ingredient hiveIngredient = Ingredient.fromPacket(packetByteBuf);
		Ingredient handIngredient = Ingredient.fromPacket(packetByteBuf);
		int handIngredientCount = packetByteBuf.readInt();
		ItemStack output = packetByteBuf.readItemStack();
		
		return this.recipeFactory.create(identifier, group, antVariantPredicate, hiveIngredient, handIngredient, handIngredientCount, output);
	}
	
	public interface RecipeFactory<ColonyHarvestingRecipe> {
		ColonyHarvestingRecipe create(Identifier id, String group, AntVariantPredicate antVariantPredicate, Ingredient hiveIngredient, Ingredient handIngredient, int handIngredientCount, ItemStack output);
	}
}