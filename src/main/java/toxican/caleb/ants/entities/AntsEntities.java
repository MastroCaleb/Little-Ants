package toxican.caleb.ants.entities;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import toxican.caleb.ants.AntsMain;
import toxican.caleb.ants.entities.black_ant.BlackAntEntity;
import toxican.caleb.ants.entities.brown_ant.BrownAntEntity;
import toxican.caleb.ants.entities.gold_ant.GoldAntEntity;
import toxican.caleb.ants.entities.muddy_ant.MuddyAntEntity;
import toxican.caleb.ants.entities.red_ant.RedAntEntity;

public class AntsEntities {

    public static final EntityType<BrownAntEntity> BROWN_ANT = Registry.register(
        Registry.ENTITY_TYPE, new Identifier(AntsMain.MOD_ID, "brown_ant"), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BrownAntEntity::new).dimensions(BrownAntEntity.STANDING_DIMENSIONS).specificSpawnBlocks(Blocks.DIRT, Blocks.GRASS).build()
    );

    public static final EntityType<RedAntEntity> RED_ANT = Registry.register(
        Registry.ENTITY_TYPE, new Identifier(AntsMain.MOD_ID, "red_ant"), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, RedAntEntity::new).dimensions(RedAntEntity.STANDING_DIMENSIONS).specificSpawnBlocks(Blocks.DIRT, Blocks.GRASS).build()
    );

    public static final EntityType<BlackAntEntity> BLACK_ANT = Registry.register(
        Registry.ENTITY_TYPE, new Identifier(AntsMain.MOD_ID, "black_ant"), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BlackAntEntity::new).dimensions(BlackAntEntity.STANDING_DIMENSIONS).specificSpawnBlocks(Blocks.DIRT, Blocks.GRASS).build()
    );

    public static final EntityType<GoldAntEntity> GOLD_ANT = Registry.register(
        Registry.ENTITY_TYPE, new Identifier(AntsMain.MOD_ID, "gold_ant"), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, GoldAntEntity::new).dimensions(GoldAntEntity.STANDING_DIMENSIONS).specificSpawnBlocks(Blocks.DIRT, Blocks.GRASS).build()
    );

    public static final EntityType<MuddyAntEntity> MUDDY_ANT = Registry.register(
        Registry.ENTITY_TYPE, new Identifier(AntsMain.MOD_ID, "muddy_ant"), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MuddyAntEntity::new).dimensions(BrownAntEntity.STANDING_DIMENSIONS).specificSpawnBlocks(Blocks.DIRT, Blocks.GRASS).build()
    );

    public final static TagKey<EntityType<?>> ANTS = AntsEntities.register("ants");

    private static TagKey<EntityType<?>> register(String id) {
        return TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier("ants", id));
    }

    public static void init(){
        FabricDefaultAttributeRegistry.register(BROWN_ANT, AntEntity.createAntAttributes());
        FabricDefaultAttributeRegistry.register(RED_ANT, AntEntity.createAntAttributes());
        FabricDefaultAttributeRegistry.register(BLACK_ANT, AntEntity.createAntAttributes());
        FabricDefaultAttributeRegistry.register(GOLD_ANT, AntEntity.createAntAttributes());
        FabricDefaultAttributeRegistry.register(MUDDY_ANT, AntEntity.createAntAttributes());
    }
    
}
