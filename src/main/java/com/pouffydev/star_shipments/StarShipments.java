package com.pouffydev.star_shipments;

import com.pouffydev.star_shipments.client.ClientProxy;
import com.pouffydev.star_shipments.data.StarDataGen;
import com.pouffydev.star_shipments.registry.StarBlockEntities;
import com.pouffydev.star_shipments.registry.StarBlocks;
import com.pouffydev.star_shipments.registry.StarCreativeTabs;
import com.pouffydev.star_shipments.registry.StarItems;
import foundry.veil.api.client.util.Easings;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(StarShipments.MODID)
public class StarShipments
{
    public static final String MODID = "star_shipments";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static StarShipments INSTANCE;
    public static CommonProxy COMMON_PROXY = new CommonProxy();
    public static ClientProxy CLIENT_PROXY = new ClientProxy();
    public static final String NAME = "S.T.A.R. Shipments";
    private final IEventBus modEventBus;

    public StarShipments(IEventBus modEventBus) {
        this.modEventBus = modEventBus;
        INSTANCE = this;

        StarItems.staticInit();
        StarBlocks.staticInit();
        StarBlockEntities.staticInit();
        StarCreativeTabs.staticInit();

        this.modEventBus.addListener(StarDataGen::gatherDataEvent);
        this.modEventBus.register(this);
    }

    @SubscribeEvent
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        LOGGER.info("[S.T.A.R.] Load Complete");
    }

    public static IEventBus getEventBus() {
        return INSTANCE.modEventBus;
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
