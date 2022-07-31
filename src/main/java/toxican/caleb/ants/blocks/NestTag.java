package toxican.caleb.ants.blocks;

import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class NestTag {

    public static final String MOD_ID = "ants";

    public static final TagKey<Block> NEST = NestTag.registerBlock("nest"); //This is the tag for nests. Do not touch unless you want to add one

    //From here start every tag for compatibility: See the Wiki on the Git Hub repository for documentation

    public static final TagKey<Block> ANT_FOOD = NestTag.registerBlock("food/ant_food");
    /*
        In this block tag we add every other block tag that decides what type
        of food the block and what to display in the ant's mouth.
    */

    //Vanilla Leaves: (Maybe you want to use a vanilla one idk)

    public static final TagKey<Block> OAK_LEAF_FOOD = NestTag.registerBlock("food/vanilla/oak_leaf_food"); //Every block that should display an Oak Leaf in the ant's mouth
    public static final TagKey<Block> DARK_OAK_LEAF_FOOD = NestTag.registerBlock("food/vanilla/dark_oak_leaf_food"); //Every block that should display an Dark Oak Leaf in the ant's mouth
    public static final TagKey<Block> ACACIA_LEAF_FOOD = NestTag.registerBlock("food/vanilla/acacia_leaf_food"); //Every block that should display an Acacia Leaf in the ant's mouth
    public static final TagKey<Block> SPRUCE_LEAF_FOOD = NestTag.registerBlock("food/vanilla/spruce_leaf_food"); //Every block that should display an Spruce Leaf in the ant's mouth
    public static final TagKey<Block> BIRCH_LEAF_FOOD = NestTag.registerBlock("food/vanilla/birch_leaf_food"); //Every block that should display an Birch Leaf in the ant's mouth
    public static final TagKey<Block> JUNGLE_LEAF_FOOD = NestTag.registerBlock("food/vanilla/jungle_leaf_food"); //Every block that should display an Jungle Leaf in the ant's mouth
    public static final TagKey<Block> FLOWERING_AZALEA_LEAF_FOOD = NestTag.registerBlock("food/vanilla/flowering_azalea_leaf_food"); //Every block that should display an Flowring Azalea Leaf in the ant's mouth
    public static final TagKey<Block> AZALEA_LEAF_FOOD = NestTag.registerBlock("food/vanilla/azalea_leaf_food"); //Every block that should display an Azalea Leaf in the ant's mouth

    //Colored Leaves: (Mainly for Spectrum compatibility[love ya DaFuqs] but also for other Mods with colored leaves)

    //Colored Oak Leaves:

    public static final TagKey<Block> OAK_BLACK_LEAF_FOOD = NestTag.registerBlock("food/colored/oak_black_leaf_food");
    public static final TagKey<Block> OAK_BLUE_LEAF_FOOD = NestTag.registerBlock("food/colored/oak_blue_leaf_food");
    public static final TagKey<Block> OAK_BROWN_LEAF_FOOD = NestTag.registerBlock("food/colored/oak_brown_leaf_food");
    public static final TagKey<Block> OAK_CYAN_LEAF_FOOD = NestTag.registerBlock("food/colored/oak_cyan_leaf_food");
    public static final TagKey<Block> OAK_GRAY_LEAF_FOOD = NestTag.registerBlock("food/colored/oak_gray_leaf_food");
    public static final TagKey<Block> OAK_GREEN_LEAF_FOOD = NestTag.registerBlock("food/colored/oak_green_leaf_food");
    public static final TagKey<Block> OAK_LIGHT_BLUE_LEAF_FOOD = NestTag.registerBlock("food/colored/oak_light_blue_leaf_food");
    public static final TagKey<Block> OAK_LIGHT_GRAY_LEAF_FOOD = NestTag.registerBlock("food/colored/oak_light_gray_leaf_food");
    public static final TagKey<Block> OAK_LIME_LEAF_FOOD = NestTag.registerBlock("food/colored/oak_lime_leaf_food");
    public static final TagKey<Block> OAK_MAGENTA_LEAF_FOOD = NestTag.registerBlock("food/colored/oak_magenta_leaf_food");
    public static final TagKey<Block> OAK_ORANGE_LEAF_FOOD = NestTag.registerBlock("food/colored/oak_orange_leaf_food");
    public static final TagKey<Block> OAK_PINK_LEAF_FOOD = NestTag.registerBlock("food/colored/oak_pink_leaf_food");
    public static final TagKey<Block> OAK_PURPLE_LEAF_FOOD = NestTag.registerBlock("food/colored/oak_purple_leaf_food");
    public static final TagKey<Block> OAK_RED_LEAF_FOOD = NestTag.registerBlock("food/colored/oak_red_leaf_food");
    public static final TagKey<Block> OAK_WHITE_LEAF_FOOD = NestTag.registerBlock("food/colored/oak_white_leaf_food");
    public static final TagKey<Block> OAK_YELLOW_LEAF_FOOD = NestTag.registerBlock("food/colored/oak_yellow_leaf_food");



    //Other Blocks: For this you'll need to wait a while.
    /*
        These are the next features for the next update:
        
        Simple .json implementation that will go something like this:

        This in: data/[modid]/ant_compat/ant_compat.json

        {
            textures:{
                "black_ant_[food]": "[modid]:ant_compat/black_ant_[food]", 
                "red_ant_[food]": "[modid]:ant_compat/red_ant_[food]",
                "brown_ant_[food]": "[modid]:ant_compat/brown_ant_[food]",
                "gold_ant_[food]": "[modid]:ant_compat/gold_ant_[food]"
            }
        }

    */
    
    private static TagKey<Block> registerBlock(String id) {
        return TagKey.of(Registry.BLOCK_KEY, new Identifier(MOD_ID, id));
    }
}
