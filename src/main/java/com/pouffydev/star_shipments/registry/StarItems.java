package com.pouffydev.star_shipments.registry;

import com.pouffydev.star_shipments.StarShipments;
import com.pouffydev.star_shipments.util.ModUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;
import java.util.function.Supplier;

public class StarItems {
    private static final Supplier<Item> SIMPLE_SUPPLIER = () -> new Item(new Item.Properties());
    public static final DeferredRegister.Items ITEMS = ModUtils.createRegister(DeferredRegister::createItems);


    private static DeferredItem<Item> registerSimple(String name, Item.Properties itemProperties) {
        return register(name, () -> new Item(itemProperties));
    }

    private static DeferredItem<Item> registerSimple(String name) {
        return register(name, SIMPLE_SUPPLIER);
    }

    private static <T extends Item> DeferredItem<T> register(String id, Supplier<T> pIProp) {
        return ITEMS.register(id.toLowerCase(), pIProp);
    }

    public static void staticInit() {
        StarShipments.LOGGER.info("Registering Items");
    }

    //private-package so block register class can use
    static void registerBlockItem(Holder<Block> blockHolder) {
        registerBlockItem(blockHolder, new Item.Properties());
    }

    //private-package so block register class can use
    static void registerBlockItem(Holder<Block> blockHolder, Item.Properties properties) {
        ITEMS.registerSimpleBlockItem(blockHolder, properties);
    }

    static DeferredItem<BlockItem> registerBlockItem(String name, Supplier<? extends Block> blockHolder, Item.Properties properties) {
        return ITEMS.registerSimpleBlockItem(name, blockHolder, properties);
    }

    static DeferredItem<? extends BlockItem> registerSpecialBlockItem(String name, Supplier<? extends BlockItem> sup) {
        return register(name, sup);
    }

    static DeferredItem<DoubleHighBlockItem> registerDoubleBlockItem(String name, Supplier<DoubleHighBlockItem> sup) {
        return ITEMS.register(name, sup);
    }

    static void registerSimpleItem(String name) {
        ITEMS.registerSimpleItem(name);
    }

    public static Collection<DeferredHolder<Item, ? extends Item>> getItems() {
        return ITEMS.getEntries();
    }
}
