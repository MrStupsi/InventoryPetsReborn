package de.mrstupsi.inventorypetsreborn;

import de.mrstupsi.inventorypetsreborn.item.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod("inventorypetsreborn")
public class InventoryPetsReborn {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "inventorypetsreborn");

    public InventoryPetsReborn() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register("blaze_pet", () -> BlazePet.INSTANCE);
        ITEMS.register("chest_pet", () -> ChestPet.INSTANCE);
        ITEMS.register("chicken_pet", () -> ChickenPet.INSTANCE);
        ITEMS.register("cow_pet", () -> CowPet.INSTANCE);
        ITEMS.register("craftingtable_pet", () -> CraftingTablePet.INSTANCE);
        ITEMS.register("creeper_pet", () -> CreeperPet.INSTANCE);
        ITEMS.register("doublechest_pet", () -> DoubleChestPet.INSTANCE);
        ITEMS.register("enchantingtable_pet", () -> EnchantingTablePet.INSTANCE);
        ITEMS.register("enderchest_pet", () -> EnderChestPet.INSTANCE);
        ITEMS.register("enderman_pet", () -> EndermanPet.INSTANCE);
        ITEMS.register("furnace_pet", () -> FurnacePet.INSTANCE);
        ITEMS.register("ghast_pet", () -> GhastPet.INSTANCE);
        ITEMS.register("illuminati_pet", () -> IlluminatiPet.INSTANCE);
        ITEMS.register("irongolem_pet", () -> IronGolemPet.INSTANCE);
        ITEMS.register("loot_pet", () -> LootPet.INSTANCE);
        ITEMS.register("magmacube_pet", () -> MagmaCubePet.INSTANCE);
        ITEMS.register("milka_pet", () -> MilkaPet.INSTANCE);
        ITEMS.register("mooshroom_pet", () -> MooshroomPet.INSTANCE);
        ITEMS.register("pig_pet", () -> PigPet.INSTANCE);
        ITEMS.register("pixie_pet", () -> PixiePet.INSTANCE);
        ITEMS.register("sheep_pet", () -> SheepPet.INSTANCE);
        ITEMS.register("silverfish_pet", () -> SilverfishPet.INSTANCE);
        ITEMS.register("spider_pet", () -> SpiderPet.INSTANCE);
        ITEMS.register("squid_pet", () -> SquidPet.INSTANCE);
        ITEMS.register("coal_nugget", () -> CoalNugget.INSTANCE);
        ITEMS.register("diamond_nugget", () -> DiamondNugget.INSTANCE);
        ITEMS.register("emerald_nugget", () -> EmeraldNugget.INSTANCE);
        ITEMS.register("ender_nugget", () -> EnderNugget.INSTANCE);
        ITEMS.register("lapis_nugget", () -> LapisNugget.INSTANCE);
        ITEMS.register("obsidian_nugget", () -> ObsidianNugget.INSTANCE);
        ITEMS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        Tick.TICK++;
    }
}
