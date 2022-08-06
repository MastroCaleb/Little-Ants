package toxican.caleb.ants.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import toxican.caleb.ants.blocks.dandelion.DandelionFlowerBlock;
import toxican.caleb.ants.blocks.dandelion.DandelionFlowerBlockItem;
import toxican.caleb.ants.blocks.nest.AntNestBlock;
import toxican.caleb.ants.blocks.nest.AntNestEntity;

//Block registry class

public class AntsBlocks {

    public static AntNestBlock DIRT_ANT_NEST;
    public static AntNestBlock SAND_ANT_NEST;
    public static BlockEntityType<AntNestEntity> NEST_BLOCK_ENTITY;
    public static final DandelionFlowerBlock WINDY_DANDELION = new DandelionFlowerBlock(StatusEffects.LEVITATION, 120, FabricBlockSettings.copyOf(Blocks.DANDELION));
    public static final FlowerPotBlock POTTED_WINDY_DANDELION = new FlowerPotBlock(WINDY_DANDELION, FabricBlockSettings.copyOf(Blocks.POTTED_DANDELION));

    public static void init(){
        DIRT_ANT_NEST = Registry.register(Registry.BLOCK, new Identifier("ants", "dirt_ant_nest"), new AntNestBlock(FabricBlockSettings.copyOf(Blocks.DIRT)));
        Registry.register(Registry.ITEM, new Identifier("ants", "dirt_ant_nest"), new BlockItem(DIRT_ANT_NEST, new Item.Settings().group(ItemGroup.DECORATIONS)));
        SAND_ANT_NEST = Registry.register(Registry.BLOCK, new Identifier("ants", "sand_ant_nest"), new AntNestBlock(FabricBlockSettings.copyOf(Blocks.SAND)));
        Registry.register(Registry.ITEM, new Identifier("ants", "sand_ant_nest"), new BlockItem(SAND_ANT_NEST, new Item.Settings().group(ItemGroup.DECORATIONS)));
        Registry.register(Registry.BLOCK, new Identifier("ants", "windy_dandelion"), WINDY_DANDELION);
        Registry.register(Registry.ITEM, new Identifier("ants", "windy_dandelion"), new DandelionFlowerBlockItem(WINDY_DANDELION, new Item.Settings().group(ItemGroup.DECORATIONS)));
        Registry.register(Registry.BLOCK, new Identifier("ants", "potted_windy_dandelion"), POTTED_WINDY_DANDELION);
        NEST_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("ants", "nest"), FabricBlockEntityTypeBuilder.create(AntNestEntity::new, DIRT_ANT_NEST, SAND_ANT_NEST).build(null));
    }
    
}
