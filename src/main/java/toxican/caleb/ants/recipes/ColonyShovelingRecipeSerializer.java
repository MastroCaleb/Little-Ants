package toxican.caleb.ants.recipes;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import toxican.caleb.ants.more_ants_api.AntVariantPredicate;

public class ColonyShovelingRecipeSerializer implements RecipeSerializer<ColonyShovelingRecipe> {
	
	public final RecipeFactory<ColonyShovelingRecipe> recipeFactory;
	
	public ColonyShovelingRecipeSerializer(RecipeFactory<ColonyShovelingRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	@Override
	public ColonyShovelingRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = JsonHelper.getString(jsonObject, "group", "");
		
		AntVariantPredicate antVariantPredicate = AntVariantPredicate.ANY;
		if(jsonObject.has("ant_variant")) {
			antVariantPredicate = AntVariantPredicate.fromJson(JsonHelper.getObject(jsonObject, "ant_variant"));
		}
		
		Ingredient hiveIngredient = Ingredient.EMPTY;
		if(jsonObject.has("hive_ingredient")) {
			hiveIngredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "hive_ingredient"));
		}
		
		ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
		
		return this.recipeFactory.create(identifier, group, antVariantPredicate, hiveIngredient, output);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, ColonyShovelingRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		recipe.antVariantPredicate.write(packetByteBuf);
		recipe.hiveIngredient.write(packetByteBuf);
		packetByteBuf.writeItemStack(recipe.output);
	}
	
	@Override
	public ColonyShovelingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		AntVariantPredicate antVariantPredicate = AntVariantPredicate.fromPacket(packetByteBuf);
		Ingredient hiveIngredient = Ingredient.fromPacket(packetByteBuf);
		ItemStack output = packetByteBuf.readItemStack();
		
		return this.recipeFactory.create(identifier, group, antVariantPredicate, hiveIngredient, output);
	}
	
	public interface RecipeFactory<ColonyShovelingRecipe> {
		ColonyShovelingRecipe create(Identifier id, String group, AntVariantPredicate antVariantPredicate, Ingredient hiveIngredient, ItemStack output);
	}
	
}