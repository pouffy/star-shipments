package com.pouffydev.star_shipments.data.client;

import com.pouffydev.star_shipments.common.block.terminal.TerminalBlock;
import com.pouffydev.star_shipments.registry.StarBlocks;
import com.pouffydev.star_shipments.registry.StarItems;
import com.pouffydev.star_shipments.util.ModUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Set;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class StarItemModelGen extends ItemModelProvider {
    public StarItemModelGen(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        Set<Holder<Item>> overrides = Set.of();

        for (DeferredHolder<Item, ? extends Item> item : StarItems.getItems()) {
            if (overrides.contains(item) || item.get() instanceof BlockItem) {
                continue;
            }
            basicItem(item);
        }

        terminalTransform(StarBlocks.exportTerminal);
    }

    private void separateTransform(DeferredHolder<Item, ? extends Item> item) {
        item.unwrapKey().ifPresent(
                itemName -> {
                    ResourceLocation itemModelLoc = itemName.location().withPrefix("item/");
                    ItemModelBuilder gui = super.nested().parent(new ModelFile.UncheckedModelFile(itemModelLoc.withSuffix("_gui")));
                    ItemModelBuilder twoDim = super.nested().parent(new ModelFile.UncheckedModelFile(itemModelLoc.withSuffix("_handheld")));
                    super.withExistingParent(itemModelLoc.getPath(), mcLoc("item/handheld"))
                            .customLoader(SeparateTransformsModelBuilder::begin)
                            .perspective(ItemDisplayContext.GUI, gui)
                            .perspective(ItemDisplayContext.FIXED, twoDim)
                            .base(twoDim);
                });
    }

    private void terminalTransform(DeferredHolder<Block, ? extends TerminalBlock<?>> item) {
        item.unwrapKey().ifPresent(
                itemName -> {
                    ResourceLocation blockModelLoc = itemName.location().withPrefix("block/");
                    ResourceLocation itemModelLoc = itemName.location().withPrefix("item/");
                    ItemModelBuilder gui = super.nested().parent(new ModelFile.UncheckedModelFile(blockModelLoc.withSuffix("/gui")));
                    ItemModelBuilder twoDim = super.nested().parent(new ModelFile.UncheckedModelFile(blockModelLoc.withSuffix("/inactive")));
                    super.withExistingParent(itemModelLoc.getPath(), mcLoc("item/handheld"))
                            .customLoader(SeparateTransformsModelBuilder::begin)
                            .perspective(ItemDisplayContext.GUI, gui)
                            .base(twoDim);
                });
    }

    private ItemModelBuilder customModel(DeferredHolder<Item, ? extends Item> item) {
        return getBuilder(item.getKey().location().getPath()).parent(new ModelFile.UncheckedModelFile(ModUtils.location("item/" + item.getKey().location().getPath() + "/item")));
    }

    private ItemModelBuilder customBlockItemModel(DeferredHolder<Item, ? extends Item> item) {
        return getBuilder(item.getKey().location().getPath()).parent(new ModelFile.UncheckedModelFile(ModUtils.location("block/" + item.getKey().location().getPath() + "/item")));
    }

    private void basicItem(Supplier<? extends Item> item) {
        super.basicItem(item.get());
    }

    private ItemModelBuilder basicItem(DeferredHolder<Item, ? extends Item> item, UnaryOperator<ResourceLocation> modelLocationModifier) {
        ResourceLocation name = item.getKey().location().withPrefix("item/");

        return getBuilder(modelLocationModifier.apply(name).getPath())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ModUtils.location(modelLocationModifier.apply(name).getPath()));
    }

    private ResourceLocation handheld32(DeferredHolder<Item, ? extends Item> item) {
        return handheld(item, 32);
    }

    private ResourceLocation handheld48(DeferredHolder<Item, ? extends Item> item) {
        return handheld(item, 48);
    }

    private ResourceLocation handheld64(DeferredHolder<Item, ? extends Item> item) {
        return handheld(item, 64);
    }

    private ResourceLocation handheld32(DeferredHolder<Item, ? extends Item> item, String guiLocationModifier, String handheldLocationModifier) {
        return handheld(item, 32, loc -> loc.withSuffix("_" + guiLocationModifier), loc -> loc.withSuffix("_" + handheldLocationModifier));
    }

    private ResourceLocation handheld48(DeferredHolder<Item, ? extends Item> item, String guiLocationModifier, String handheldLocationModifier) {
        return handheld(item, 48, loc -> loc.withSuffix("_" + guiLocationModifier), loc -> loc.withSuffix("_" + handheldLocationModifier));
    }

    private ResourceLocation handheld64(DeferredHolder<Item, ? extends Item> item, String guiLocationModifier, String handheldLocationModifier) {
        return handheld(item, 64, loc -> loc.withSuffix("_" + guiLocationModifier), loc -> loc.withSuffix("_" + handheldLocationModifier));
    }

    private ResourceLocation handheld(DeferredHolder<Item, ? extends Item> item, int x) {
        return handheld(item, x, UnaryOperator.identity(), UnaryOperator.identity());
    }

    private ResourceLocation handheld(DeferredHolder<Item, ? extends Item> item, int x, UnaryOperator<ResourceLocation> guiLocationModifier, UnaryOperator<ResourceLocation> handheldLocationModifier) {
        ResourceLocation name = item.getKey().location().withPrefix("item/");
        super.withExistingParent(handheldLocationModifier.apply(name).getPath(), ModUtils.location("item/templates/handheld%sx".formatted(x)))
                .texture("layer0", name);
        separateTransform(item);
        basicItem(item, guiLocationModifier);
        return name;
    }

    private ItemModelBuilder doorItem(Supplier<? extends Block> block) {
        String name = BuiltInRegistries.BLOCK.getKey(block.get()).getPath();
        return getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ModUtils.location("item/" + name + "_item"));
    }
}
