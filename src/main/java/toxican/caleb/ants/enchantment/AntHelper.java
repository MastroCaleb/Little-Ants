package toxican.caleb.ants.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;

//Method to check if Ant Resistance is applied

public class AntHelper {

    public static boolean hasAntResistance(LivingEntity entity) {
        return EnchantmentHelper.getEquipmentLevel(AntsEnchantments.ANT_RESISTANCE, entity) > 0;
    }
    
}
