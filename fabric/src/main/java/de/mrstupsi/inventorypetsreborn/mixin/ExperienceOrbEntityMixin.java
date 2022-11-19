package de.mrstupsi.inventorypetsreborn.mixin;

import de.mrstupsi.inventorypetsreborn.item.InventoryPet;
import de.mrstupsi.inventorypetsreborn.item.LootPet;
import de.mrstupsi.inventorypetsreborn.item.PixiePet;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin {
    @Shadow private int amount;

    @Inject(at = @At(value = "HEAD"), method = "onPlayerCollision")
    public void onPlayerCollision(PlayerEntity player, CallbackInfo ci) {
        int activePet = InventoryPet.getActivePet(player, PixiePet.INSTANCE);
        if (!player.world.isClient && activePet != -1) {
            if (!player.isCreative() && !player.isSpectator()) {
                ItemStack is = player.getInventory().getStack(activePet);
                is.setDamage(MathHelper.clamp(is.getDamage() + 1, 0, PixiePet.INSTANCE.getMaxDamage()));
                player.getInventory().setStack(activePet, is);
            }
            amount *= 2;
        }
    }
}