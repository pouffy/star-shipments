package com.pouffydev.star_shipments.registry;

import com.pouffydev.star_shipments.StarShipments;
import com.pouffydev.star_shipments.common.block.landing_pad.LandingPadBlock;
import com.pouffydev.star_shipments.common.block.landing_pad.LandingPadBlockEntity;
import com.pouffydev.star_shipments.common.block.landing_pad.LandingPadBlockItem;
import com.pouffydev.star_shipments.common.block.landing_pad.LandingPadStructuralBlock;
import com.pouffydev.star_shipments.common.block.terminal.TerminalBlock;
import com.pouffydev.star_shipments.common.block.terminal.TerminalBlockEntity;
import com.pouffydev.star_shipments.common.block.terminal.TerminalBlockItem;
import com.pouffydev.star_shipments.util.ModUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;
import java.util.function.Supplier;

public class StarBlocks {
    private static final Supplier<Block> SIMPLE_SUPPLIER = () -> new Block(BlockBehaviour.Properties.of());
    public static final DeferredRegister<Block> BLOCKS = ModUtils.createRegister(DeferredRegister::createBlocks);


    public static final DeferredHolder<Block, LandingPadBlock<LandingPadBlockEntity>> landingPad = registerSpecialItem("landing_pad",
            () -> (LandingPadBlock<LandingPadBlockEntity>) new LandingPadBlock<>(BlockBehaviour.Properties.of()
                    .strength(3.5F)
                    .sound(SoundType.METAL)
                    .pushReaction(PushReaction.BLOCK))
                    .setBlockEntity(StarBlockEntities.landingPad),
                LandingPadBlockItem::new
            );

    public static final DeferredHolder<Block, LandingPadStructuralBlock> landingPadStructural = registerNoItem("landing_pad_structural",
            () -> new LandingPadStructuralBlock(BlockBehaviour.Properties.of()
                    .strength(3.5F)
                    .sound(SoundType.METAL)
                    .pushReaction(PushReaction.BLOCK)
            ));

    public static final DeferredHolder<Block, TerminalBlock<TerminalBlockEntity>> exportTerminal = registerSpecialItem("export_terminal",
            () -> (TerminalBlock<TerminalBlockEntity>) new TerminalBlock<>(BlockBehaviour.Properties.of()
                    .strength(3.5F)
                    .sound(SoundType.METAL)
                    .pushReaction(PushReaction.BLOCK))
                    .setBlockEntity(StarBlockEntities.exportTerminal),
            () -> new TerminalBlockItem(StarBlocks.exportTerminal.get(), new Item.Properties())
    );

    //Extra Cool shit :3
    private static <T extends Block> DeferredHolder<Block, T> register(String id, Supplier<T> block, Item.Properties pIProp) {
        DeferredHolder<Block, T> toReturn = BLOCKS.register(id.toLowerCase(), block);
        makeBlockItem(toReturn, pIProp);
        return toReturn;
    }
    private static <T extends Block> DeferredHolder<Block, T> registerSpecialItem(String id, Supplier<T> block, Supplier<? extends BlockItem> sup) {
        DeferredHolder<Block, T> toReturn = BLOCKS.register(id.toLowerCase(), block);
        makeSpecialBlockItem(id, sup);
        return toReturn;
    }
    private static <T extends Block> DeferredHolder<Block, T> registerNoItem(String id, Supplier<T> block) {
        return BLOCKS.register(id.toLowerCase(), block);
    }
    private static <T extends Block> void makeBlockItem(DeferredHolder<Block, T> block, Item.Properties pIProp) {
        StarItems.registerBlockItem(block, pIProp);
    }
    private static <T extends Block> void makeSpecialBlockItem(String name, Supplier<? extends BlockItem> sup) {
        StarItems.registerSpecialBlockItem(name, sup);
    }

    public static void staticInit() {

    }



    public static Collection<DeferredHolder<Block, ? extends Block>> getBlocks() {
        return BLOCKS.getEntries();
    }
}
