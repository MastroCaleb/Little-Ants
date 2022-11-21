package toxican.caleb.ants.blocks.nest;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.util.Identifier;
import toxican.caleb.ants.items.criteria.AllVariantsCriterion;

import org.jetbrains.annotations.Nullable;

//I dont think i even use this class but might be helpful in the near future

public class CriteriaAnt {
    private static final Map<Identifier, Criterion<?>> VALUES = Maps.newHashMap();
    public static final AntNestDestoyedCriterion ANT_NEST_DESTROYED = CriteriaAnt.register(new AntNestDestoyedCriterion());

    private static <T extends Criterion<?>> T register(T object) {
        if (VALUES.containsKey(object.getId())) {
            throw new IllegalArgumentException("Duplicate criterion id " + object.getId());
        }
        VALUES.put(object.getId(), object);
        return object;
    }

    @Nullable
    public static <T extends CriterionConditions> Criterion<?> getById(Identifier id) {
        return VALUES.get(id);
    }

    public static Iterable<? extends Criterion<?>> getCriteria() {
        return VALUES.values();
    }
}

