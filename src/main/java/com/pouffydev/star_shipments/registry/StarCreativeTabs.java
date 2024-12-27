package com.pouffydev.star_shipments.registry;

import com.pouffydev.star_shipments.util.ModUtils;
import com.pouffydev.star_shipments.util.NoTab;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;

public class StarCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = ModUtils.createRegister(Registries.CREATIVE_MODE_TAB);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> STAR_TAB;

    static {
        STAR_TAB = registerTabWithBlockIcon("star_shipments", StarBlocks.landingPad, output -> {

            for (DeferredHolder<Item, ? extends Item> registry : StarItems.getItems()) {
                if (registry.get() instanceof BlockItem || registry.get().getClass().isAnnotationPresent(NoTab.class))
                    continue;
                output.accept(registry.get());
            }
            for (DeferredHolder<Block, ? extends Block> registry : StarBlocks.getBlocks()) {
                if (registry.get().getClass().isAnnotationPresent(NoTab.class)) continue;
                output.accept(registry.get());
            }
            //for (DeferredHolder<Block, ? extends Block> registry : ModDecorativeBlocks.getBlocks()) {
            //    if (registry.get().getClass().isAnnotationPresent(NoTab.class)) continue;
            //    output.accept(registry.get());
            //}

        }, builder -> builder.withTabsBefore(CreativeModeTabs.COMBAT));
    }

    private static DeferredHolder<CreativeModeTab, CreativeModeTab> registerTab(String name, Holder<Item> icon, Consumer<CreativeModeTab.Output> displayItems) {
        return registerTab(name, icon, displayItems, ModUtils.noAction());
    }

    private static DeferredHolder<CreativeModeTab, CreativeModeTab> registerTab(String name, Holder<Item> icon, Consumer<CreativeModeTab.Output> displayItems, Consumer<CreativeModeTab.Builder> additionalProperties) {
        return CREATIVE_MODE_TABS.register(name, id -> {
            final CreativeModeTab.Builder builder = CreativeModeTab.builder();
            builder.title(Component.translatable(id.toLanguageKey("itemGroup")))
                    .icon(() -> new ItemStack(icon))
                    .displayItems((pParameters, pOutput) -> displayItems.accept(pOutput));
            additionalProperties.accept(builder);
            return builder.build();
        });
    }
    private static DeferredHolder<CreativeModeTab, CreativeModeTab> registerTabSearchBar(String name, Holder<Item> icon, Consumer<CreativeModeTab.Output> displayItems, Consumer<CreativeModeTab.Builder> additionalProperties) {
        return CREATIVE_MODE_TABS.register(name, id -> {
            final CreativeModeTab.Builder builder = CreativeModeTab.builder();
            builder.title(Component.translatable(id.toLanguageKey("itemGroup")))
                    .icon(() -> new ItemStack(icon))
                    .withSearchBar()
                    .displayItems((pParameters, pOutput) -> displayItems.accept(pOutput));
            additionalProperties.accept(builder);
            return builder.build();
        });
    }
    private static DeferredHolder<CreativeModeTab, CreativeModeTab> registerTabWithBlockIcon(String name, Holder<Block> icon, Consumer<CreativeModeTab.Output> displayItems, Consumer<CreativeModeTab.Builder> additionalProperties) {
        return CREATIVE_MODE_TABS.register(name, id -> {
            final CreativeModeTab.Builder builder = CreativeModeTab.builder();
            builder.title(Component.translatable(id.toLanguageKey("itemGroup")))
                    .icon(() -> new ItemStack((ItemLike) icon))
                    .displayItems((pParameters, pOutput) -> displayItems.accept(pOutput));
            additionalProperties.accept(builder);
            return builder.build();
        });
    }

    public static void staticInit() {

    }
}
