package de.mrstupsi.inventorypetsreborn.mixin.client;

import de.mrstupsi.inventorypetsreborn.item.InventoryPet;
import de.mrstupsi.inventorypetsreborn.item.PixiePet;
import de.mrstupsi.inventorypetsreborn.item.SlimePet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Shadow @Final private Minecraft minecraft;

    @Shadow private ClientLevel level;

    @Inject(at = @At("HEAD"), method = "handleEntityEvent", cancellable = true)
    public void handleEntityEvent(ClientboundEntityEventPacket packet, CallbackInfo ci) {
        if (packet.getEventId() == 99) {
            ci.cancel();
            PacketUtils.ensureRunningOnSameThread(packet, (ClientGamePacketListener) this, minecraft);
            Entity entity = packet.getEntity(level);
            if (entity != null) {
                minecraft.particleEngine.createTrackingEmitter(entity, ParticleTypes.ITEM_SLIME, 30);
                level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, entity.getSoundSource(), 1.0F, 1.0F, false);
                if (entity == minecraft.player) {
                    minecraft.gameRenderer.displayItemActivation(findSlimePet(minecraft.player));
                }
            }
        }
    }

    private static ItemStack findSlimePet(Player p) {
        int activePet = InventoryPet.getActivePet(p, SlimePet.INSTANCE);
        if (activePet != -1) {
            return p.getInventory().getItem(activePet);
        } else return new ItemStack(SlimePet.INSTANCE);
    }
}