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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Specifies the drop a player gets when using a shovel on a completely filled colony
 * id: the recipes identifier
 * group: internal group string (mainly used for the recipe book or other mechanisms that group recipes
 * hiveIngredient: the last food the ants collected in that colony, or one per random
 * handIngredient: the item the player has to hold in their hand
 * int handIngredientCount: the amount of items the player has to hold in their hand. This amount gets consumed
 * output: the item stack the player gets when harvesting
**/
public class ColonyHarvestingRecipe implements Recipe<Inventory> {

	// specifying a recipe with an empty ingredient marks it as the default recipe
	// if no special recipe matches, this one is used as default
	protected static ColonyHarvestingRecipe defaultRecipe;
	protected static List<ColonyHarvestingRecipe> specialRecipes = new ArrayList<>();

	protected final Identifier id;
	protected final String group;
	
	protected final Ingredient hiveIngredient;
	protected final Ingredient handIngredient;
	protected final int handIngredientCount;
	protected final ItemStack output;
	
	public ColonyHarvestingRecipe(Identifier id, String group, Ingredient hiveIngredient, Ingredient handIngredient, int handIngredientCount, ItemStack output) {
		this.id = id;
		this.group = group;
		this.hiveIngredient = hiveIngredient;
		this.handIngredient = handIngredient;
		this.handIngredientCount = handIngredientCount;
		this.output = output;
		
		if(this.hiveIngredient == Ingredient.EMPTY) {
			defaultRecipe = this;
		} else {
			specialRecipes.add(this);
		}
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof ColonyHarvestingRecipe colonyHarvestingRecipe) {
			return colonyHarvestingRecipe.getId().equals(this.getId());
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
		return AntsRecipeTypes.COLONY_HARVESTING_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return AntsRecipeTypes.COLONY_HARVESTING;
	}
	
	public static Optional<ColonyHarvestingRecipe> getRecipeFor(ItemStack hiveStack, ItemStack handStack) {
		for(ColonyHarvestingRecipe recipe : specialRecipes) {
			if(recipe.hiveIngredient.test(hiveStack) && (recipe.handIngredient.isEmpty() || recipe.handIngredient.test(handStack))) {
				return Optional.of(recipe);
			}
		}
		if(defaultRecipe.handIngredient.isEmpty() || defaultRecipe.handIngredient.test(handStack)) {
			return Optional.of(defaultRecipe);
		} else {
			return Optional.empty();
		}
	}
	
	public int getHandIngredientCount() {
		return this.handIngredientCount;
	}
	
}