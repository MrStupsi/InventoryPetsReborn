package de.mrstupsi.inventorypetsreborn.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;

public class CloudBlock extends Block {
    public static CloudBlock INSTANCE = new CloudBlock();

    public CloudBlock() {
        super(Properties.of(Material.SNOW).instabreak());
    }
}