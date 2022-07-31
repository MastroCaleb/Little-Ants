package toxican.caleb.ants.enchantment.ant_resistance;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

//Enchant that helps you not getting clapped by the Colonies

public class AntResistanceEnchantment extends Enchantment{

    public AntResistanceEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public int getMinPower(int level) {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
    
}
