package toxican.caleb.ants.entities;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.AbstractTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;

public class AntTagProvider
extends AbstractTagProvider<EntityType<?>> {
    public AntTagProvider(DataGenerator root) {
        super(root, Registry.ENTITY_TYPE);
    }

    @Override
    protected void configure() {
        this.getOrCreateTagBuilder(AntsEntities.ANTS).add(AntsEntities.BROWN_ANT, AntsEntities.RED_ANT, AntsEntities.BLACK_ANT, AntsEntities.GOLD_ANT);
    }
}
