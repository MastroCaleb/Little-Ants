package toxican.caleb.ants.recipes;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import toxican.caleb.ants.blocks.AntsBlocks;
import toxican.caleb.ants.more_ants_api.AntVariant;
import toxican.caleb.ants.more_ants_api.AntVariantPredicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Specifies the drop a player gets when using a shovel on a completely filled colony
 * id: the recipes identifier
 * group: internal group string (mainly used for the recipe book or other mechanisms that group recipes
 * hiveIngredient: the last food the ants collected in that colony, or one per random
 * output: the item stack the player gets when shoveling
**/
public class ColonyShovelingRecipe implements Recipe<Inventory> {

	// specifying a recipe with an empty ingredient and ant variant marks it as the default recipe
	// if no special recipe matches, this one is used as default
	protected static ColonyShovelingRecipe defaultRecipe;
	protected static List<ColonyShovelingRecipe> conditionalRecipes = new ArrayList<>();

	protected final Identifier id;
	protected final String group;
	
	protected final AntVariantPredicate antVariantPredicate;
	protected final Ingredient hiveIngredient;
	protected final ItemStack output;
	
	public ColonyShovelingRecipe(Identifier id, String group, AntVariantPredicate antVariantPredicate, Ingredient hiveIngredient, ItemStack output) {
		this.id = id;
		this.group = group;
		this.antVariantPredicate = antVariantPredicate;
		this.hiveIngredient = hiveIngredient;
		this.output = output;
		
		if(this.hiveIngredient == Ingredient.EMPTY && this.antVariantPredicate == AntVariantPredicate.ANY) {
			defaultRecipe = this;
		} else {
			conditionalRecipes.add(this);
		}
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof ColonyShovelingRecipe colonyShovelingRecipe) {
			return colonyShovelingRecipe.getId().equals(this.getId());
		}
		return false;
	}
	
	// we are using a custom match logic (the colonies are not inventories in the classical sense)
	// => getRecipeFor()
	@Override
	public boolean matches(Inventory inv, World world) {
		return false;
	}
	
	@Override
	public ItemStack craft(Inventory inv) {
		return output;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getOutput() {
		return output;
	}
	
	@Override
	public boolean isIgnoredInRecipeBook() {
		return true;
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(AntsBlocks.DIRT_ANT_NEST);
	}
	
	@Override
	public Identifier getId() {
		return this.id;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return AntsRecipeTypes.COLONY_SHOVELING_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return AntsRecipeTypes.COLONY_SHOVELING;
	}
	
	public static ColonyShovelingRecipe getRecipeFor(AntVariant antVariant, ItemStack hiveStack) {
		for(ColonyShovelingRecipe recipe : conditionalRecipes) {
			if(recipe.antVariantPredicate.test(antVariant) && (recipe.hiveIngredient.isEmpty() || recipe.hiveIngredient.test(hiveStack))) {
				return recipe;
			}
		}
		return defaultRecipe;
	}
	
}