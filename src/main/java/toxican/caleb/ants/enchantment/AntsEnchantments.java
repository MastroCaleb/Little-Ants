package toxican.caleb.ants.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import toxican.caleb.ants.enchantment.ant_resistance.AntResistanceEnchantment;

//Enchantment registry class

public class AntsEnchantments {

    public static final Enchantment ANT_RESISTANCE = new AntResistanceEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.ARMOR_FEET, new EquipmentSlot[] {EquipmentSlot.FEET});


    public static void init(){

        Registry.register(Registry.ENCHANTMENT, new Identifier("ants", "ant_resistance"), ANT_RESISTANCE);

    }
    
}
