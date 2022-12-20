package de.mrstupsi.inventorypetsreborn;

import de.mrstupsi.inventorypetsreborn.block.CloudBlock;
import de.mrstupsi.inventorypetsreborn.block.CloudSpawn;
import de.mrstupsi.inventorypetsreborn.item.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.Structure;
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
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "inventorypetsreborn");
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "inventorypetsreborn");
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, "inventorypetsreborn");

    public InventoryPetsReborn() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register("cloud_block", () -> CloudBlock.INSTANCE);
        BLOCKS.register("cloud_spawn", () -> CloudSpawn.INSTANCE);
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
        ITEMS.register("house_pet", () -> HousePet.INSTANCE);
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
        ITEMS.register("slime_pet", () -> SlimePet.INSTANCE);
        ITEMS.register("spider_pet", () -> SpiderPet.INSTANCE);
        ITEMS.register("squid_pet", () -> SquidPet.INSTANCE);
        ITEMS.register("wolf_pet", () -> WolfPet.INSTANCE);
        ITEMS.register("coal_nugget", () -> CoalNugget.INSTANCE);
        ITEMS.register("diamond_nugget", () -> DiamondNugget.INSTANCE);
        ITEMS.register("emerald_nugget", () -> EmeraldNugget.INSTANCE);
        ITEMS.register("ender_nugget", () -> EnderNugget.INSTANCE);
        ITEMS.register("lapis_nugget", () -> LapisNugget.INSTANCE);
        ITEMS.register("obsidian_nugget", () -> ObsidianNugget.INSTANCE);
        //FEATURES.register("cloud_dungeon", () -> null);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        FEATURES.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        Tick.TICK++;
    }
}
