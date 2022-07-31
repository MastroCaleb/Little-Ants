package toxican.caleb.ants.blocks.dandelion;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import toxican.caleb.ants.particles.AntsParticles;

//This was fun to make

public class DandelionFlowerBlockItem extends BlockItem{

    public DandelionFlowerBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if(world.isClient()) {
            for(int i = 0; i <= user.getY() + 64; i++) {
                
                spawnFoundParticles(world, user);
            }
        }
        return TypedActionResult.pass(itemStack);
    }

    private void spawnFoundParticles(World world, PlayerEntity playerEntity) {
        for(int i = 0; i < 360; i++) {
            if(i % 20 == 0) {
                world.addParticle(AntsParticles.WINDY_DANDELION_PARTICLE,
                playerEntity.getX(), playerEntity.getY() + 1, playerEntity.getZ(),
                        Math.cos(i) * 0.15d, 0.25d, Math.sin(i) * 0.15d);
            }
        }
    }
    
}
