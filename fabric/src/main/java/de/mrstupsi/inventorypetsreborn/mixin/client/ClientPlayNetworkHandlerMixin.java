package de.mrstupsi.inventorypetsreborn.mixin.client;

import de.mrstupsi.inventorypetsreborn.item.InventoryPet;
import de.mrstupsi.inventorypetsreborn.item.SlimePet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow private ClientWorld world;

    @Shadow @Final private MinecraftClient client;

    @Inject(at = @At("HEAD"), method = "onEntityStatus", cancellable = true)
    public void onEntityStatus(EntityStatusS2CPacket packet, CallbackInfo ci) {
        if (packet.getStatus() == 99) {
            ci.cancel();
            NetworkThreadUtils.forceMainThread(packet, (ClientPlayNetworkHandler) ((Object) this), client);
            Entity entity = packet.getEntity(world);
            if (entity != null) {
                client.particleManager.addEmitter(entity, ParticleTypes.ITEM_SLIME, 30);
                world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0F, 1.0F, false);
                if (entity == client.player) {
                    client.gameRenderer.showFloatingItem(findSlimePet(client.player));
                }
            }
        }
    }

    private static ItemStack findSlimePet(PlayerEntity p) {
        int activePet = InventoryPet.getActivePet(p, SlimePet.INSTANCE);
        if (activePet != -1) {
            return p.getInventory().getStack(activePet);
        } else return new ItemStack(SlimePet.INSTANCE);
    }
}