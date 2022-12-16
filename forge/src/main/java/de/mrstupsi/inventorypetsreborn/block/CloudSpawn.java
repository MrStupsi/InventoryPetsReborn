package de.mrstupsi.inventorypetsreborn.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class CloudSpawn extends Block {
    public static CloudSpawn INSTANCE = new CloudSpawn();

    public CloudSpawn() {
        super(Properties.of(Material.SNOW).instabreak());
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {
        if (world.hasChunksAt(pos.getX(), pos.getY(), pos.getZ(), 4) && !world.isClientSide) {
            AABB range = new AABB((pos.getX() - 2), (pos.getY() - 2), (pos.getZ() - 2), (pos.getX() + 2), (pos.getY() + 2), (pos.getZ() + 2));
            List<Blaze> entities = world.getEntitiesOfClass(Blaze.class, range);
            int esize = entities.size();
            if (esize > 0)
                return;
            Blaze blaze = EntityType.BLAZE.create(world);
            blaze.teleportTo(pos.getX(), (pos.getY() + 2), pos.getZ());
            world.addFreshEntity(blaze);
            blaze.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 1, false, false));
            blaze.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200, 1, false, false));
            String[] entnames = {
                    "Scout", "Firebrand", "Hot Pocket", "Great Scott", "Filibuster", "Grumm", "Roderick", "Smokey", "Stoked", "Heat Miser",
                    "Bernie", "Hottie", "Rodney", "Fred", "Soot" };
            int nameChk = rand.nextInt(15);
            String randName = entnames[nameChk];
            blaze.setCustomName(Component.literal(randName));
            blaze.setAggressive(true);
            world.setBlock(pos, defaultBlockState(), 5);
        } else {
            world.setBlock(pos, defaultBlockState(), 5);
        }
    }
}