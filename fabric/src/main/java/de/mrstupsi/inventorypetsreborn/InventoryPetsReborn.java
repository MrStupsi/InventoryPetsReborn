package de.mrstupsi.inventorypetsreborn;

import de.mrstupsi.inventorypetsreborn.item.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.PlacedFeatures;

public class InventoryPetsReborn implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerTickEvents.START_SERVER_TICK.register(server -> Tick.TICK++);
        BlazePet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "blaze_pet"), new BlazePet());
        ChestPet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "chest_pet"), new ChestPet());
        ChickenPet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "chicken_pet"), new ChickenPet());
        CowPet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "cow_pet"), new CowPet());
        CraftingTablePet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "craftingtable_pet"), new CraftingTablePet());
        CreeperPet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "creeper_pet"), new CreeperPet());
        DoubleChestPet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "doublechest_pet"), new DoubleChestPet());
        EnchantingTablePet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "enchantingtable_pet"), new EnchantingTablePet());
        EnderChestPet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "enderchest_pet"), new EnderChestPet());
        EndermanPet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "enderman_pet"), new EndermanPet());
        FurnacePet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "furnace_pet"), new FurnacePet());
        GhastPet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "ghast_pet"), new GhastPet());
        HousePet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "house_pet"), new HousePet());
        IlluminatiPet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "illuminati_pet"), new IlluminatiPet());
        IronGolemPet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "irongolem_pet"), new IronGolemPet());
        LootPet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "loot_pet"), new LootPet());
        MagmaCubePet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "magmacube_pet"), new MagmaCubePet());
        MilkaPet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "milka_pet"), new MilkaPet());
        MooshroomPet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "mooshroom_pet"), new MooshroomPet());
        PigPet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "pig_pet"), new PigPet());
        PixiePet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "pixie_pet"), new PixiePet());
        SheepPet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "sheep_pet"), new SheepPet());
        SilverfishPet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "silverfish_pet"), new SilverfishPet());
        SlimePet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "slime_pet"), new SlimePet());
        SpiderPet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "spider_pet"), new SpiderPet());
        SquidPet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "squid_pet"), new SquidPet());
        WolfPet.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "wolf_pet"), new WolfPet());
        CoalNugget.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "coal_nugget"), new CoalNugget());
        DiamondNugget.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "diamond_nugget"), new DiamondNugget());
        EmeraldNugget.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "emerald_nugget"), new EmeraldNugget());
        EnderNugget.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "ender_nugget"), new EnderNugget());
        LapisNugget.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "lapis_nugget"), new LapisNugget());
        ObsidianNugget.INSTANCE = Registry.register(Registry.ITEM, new Identifier("inventorypetsreborn", "obsidian_nugget"), new ObsidianNugget());
    }
}