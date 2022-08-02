package toxican.caleb.ants.recipes;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import toxican.caleb.ants.AntsMain;

public class AntsRecipeTypes {
	
	public static String COLONY_SHOVELING_ID = "colony_shoveling";
	public static RecipeSerializer<ColonyShovelingRecipe> COLONY_SHOVELING_SERIALIZER;
	public static RecipeType<ColonyShovelingRecipe> COLONY_SHOVELING;
	
	public static String COLONY_HARVESTING_ID = "colony_harvesting";
	public static RecipeSerializer<ColonyHarvestingRecipe> COLONY_HARVESTING_SERIALIZER;
	public static RecipeType<ColonyHarvestingRecipe> COLONY_HARVESTING;
	
	static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerSerializer(String id, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(AntsMain.MOD_ID, id), serializer);
	}
	
	static <S extends RecipeType<T>, T extends Recipe<?>> S registerRecipeType(String id, S serializer) {
		return Registry.register(Registry.RECIPE_TYPE, new Identifier(AntsMain.MOD_ID, id), serializer);
	}
	
	public static void init() {
		COLONY_SHOVELING_SERIALIZER = registerSerializer(COLONY_SHOVELING_ID, new ColonyShovelingRecipeSerializer(ColonyShovelingRecipe::new));
		COLONY_SHOVELING = registerRecipeType(COLONY_SHOVELING_ID, new RecipeType<ColonyShovelingRecipe>() {
			@Override
			public String toString() {
				return AntsMain.MOD_ID + ":" + COLONY_SHOVELING_ID;
			}
		});
		
		COLONY_HARVESTING_SERIALIZER = registerSerializer(COLONY_HARVESTING_ID, new ColonyHarvestingRecipeSerializer(ColonyHarvestingRecipe::new));
		COLONY_HARVESTING = registerRecipeType(COLONY_HARVESTING_ID, new RecipeType<ColonyHarvestingRecipe>() {
			@Override
			public String toString() {
				return AntsMain.MOD_ID + ":" + COLONY_HARVESTING_ID;
			}
		});		
	}
	
}