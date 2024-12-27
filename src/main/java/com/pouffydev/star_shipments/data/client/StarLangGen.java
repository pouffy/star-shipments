package com.pouffydev.star_shipments.data.client;

import com.pouffydev.star_shipments.registry.StarBlocks;
import com.pouffydev.star_shipments.registry.StarCreativeTabs;
import com.pouffydev.star_shipments.registry.StarItems;
import com.pouffydev.star_shipments.util.CustomName;
import com.pouffydev.star_shipments.util.NoTab;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.NoSuchElementException;

import static com.pouffydev.star_shipments.StarShipments.MODID;

public class StarLangGen extends LanguageProvider {
    public StarLangGen(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        for (DeferredHolder<Item, ? extends Item> registry : StarItems.getItems()) {
            if (registry.get() instanceof BlockItem) continue;
            if (registry.get().getClass().isAnnotationPresent(CustomName.class)) {
                CustomName customName = registry.get().getClass().getAnnotation(CustomName.class);
                this.item(registry, customName.name());
            } else {
                this.item(registry);
            }
        }
        for (DeferredHolder<Block, ? extends Block> registry : StarBlocks.getBlocks()) {
            if (registry.get().getClass().isAnnotationPresent(CustomName.class)) {
                CustomName customName = registry.get().getClass().getAnnotation(CustomName.class);
                this.block(registry, customName.name());
            } else {
                this.block(registry);
            }
        }
        this.tab(StarCreativeTabs.STAR_TAB, "S.T.A.R. Shipments");
    }

    private void tab(Holder<CreativeModeTab> tabHolder) {
        this.add(tabHolder, "itemGroup");
    }
    private void tab(Holder<CreativeModeTab> tabHolder, String translation) {
        this.add(tabHolder, "itemGroup", translation);
    }

    private void block(Holder<Block> blockHolder) {
        this.add(blockHolder, "block");
    }
    private void block(Holder<Block> blockHolder, String translation) {
        this.add(blockHolder, "block", translation);
    }

    private void item(Holder<Item> itemHolder) {
        this.add(itemHolder, "item");
    }
    private void item(Holder<Item> itemHolder, String translation) {
        this.add(itemHolder, "item", translation);
    }

    private void string(String key, String value) {
        super.add(key, value);
    }
    private void trimMaterial(String material) {
        String translated = transform(material) + " Material";
        super.add("trim_material.%s.%s".formatted(MODID, material), translated);
    }
    private void enchantment(Holder<Enchantment> holder) {
        this.add(holder, "enchantment");
    }
    private void effect(Holder<MobEffect> holder) {
        this.add(holder, "effect");
    }

    private void container(String containerName){
        String translated = transform(containerName);
        super.add("container.%s".formatted(containerName), translated);
    }
    private void add(Holder<?> holder, String type) {
        ResourceKey<?> resourceKey = holder.unwrapKey().orElseThrow(() -> new NoSuchElementException("No respective key. Check log"));
        ResourceLocation path = resourceKey.location();
        super.add(path.toLanguageKey(type), this.transform(path));
    }
    private void add(Holder<?> holder, String type, String translation) {
        ResourceKey<?> resourceKey = holder.unwrapKey().orElseThrow(() -> new NoSuchElementException("No respective key. Check log"));
        ResourceLocation path = resourceKey.location();
        super.add(path.toLanguageKey(type), translation);
    }

    /**
     * Use to transform a ResourceLocation-form text into a spaced-form text.
     * e.g. example_transform_text -> Example Transform Text
     */
    private String transform(ResourceLocation id) {
        return this.transform(id.getPath());
    }


    /**
     * Use to transform a ResourceLocation-form text into a spaced-form text.
     * e.g. example_transform_text -> Example Transform Text
     */
    private String transform(String path) {
        int pathLength = path.length();
        StringBuilder stringBuilder = new StringBuilder(pathLength).append(Character.toUpperCase(path.charAt(0)));
        for (int i = 1; i < pathLength; i++) {
            char posChar = path.charAt(i);
            if (posChar == '_') {
                stringBuilder.append(' ');
            } else if (path.charAt(i - 1) == '_') {
                stringBuilder.append(Character.toUpperCase(posChar));
            } else stringBuilder.append(posChar);
        }
        return stringBuilder.toString();
    }
}
