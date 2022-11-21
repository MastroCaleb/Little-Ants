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

public class AntsEntities {

    public static final EntityType<AntEntity> ANT = Registry.register(
        Registry.ENTITY_TYPE, new Identifier(AntsMain.MOD_ID, "ant"), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, AntEntity::new).dimensions(AntEntity.STANDING_DIMENSIONS).specificSpawnBlocks(Blocks.DIRT, Blocks.GRASS).build()
    );

    public final static TagKey<EntityType<?>> ANTS = AntsEntities.register("ants");

    private static TagKey<EntityType<?>> register(String id) {
        return TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier("ants", id));
    }

    public static void init(){
        FabricDefaultAttributeRegistry.register(ANT, AbstractAntEntity.createAntAttributes());
    }
}