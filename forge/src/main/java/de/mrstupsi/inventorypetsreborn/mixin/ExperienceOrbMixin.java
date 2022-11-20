package de.mrstupsi.inventorypetsreborn.mixin;

import de.mrstupsi.inventorypetsreborn.item.InventoryPet;
import de.mrstupsi.inventorypetsreborn.item.PixiePet;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin {
    @Shadow private int count;

    @Inject(at = @At(value = "HEAD"), method = "playerTouch")
    public void onPlayerCollision(Player player, CallbackInfo ci) {
        int activePet = InventoryPet.getActivePet(player, PixiePet.INSTANCE);
        if (!player.level.isClientSide && activePet != -1) {
            if (!player.isCreative() && !player.isSpectator()) {
                ItemStack is = player.getInventory().getItem(activePet);
                is.setDamageValue(Math.min(Math.max(is.getDamageValue() + 1, 0), is.getMaxDamage()));
                player.getInventory().setItem(activePet, is);
            }
            count *= 2;
        }
    }
}