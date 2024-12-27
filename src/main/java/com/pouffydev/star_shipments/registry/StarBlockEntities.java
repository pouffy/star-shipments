package com.pouffydev.star_shipments.registry;

import com.pouffydev.star_shipments.StarShipments;
import com.pouffydev.star_shipments.common.block.landing_pad.LandingPadBlockEntity;
import com.pouffydev.star_shipments.common.block.terminal.TerminalBlockEntity;
import com.pouffydev.star_shipments.util.ModUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class StarBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BE_TYPES = ModUtils.createRegister(Registries.BLOCK_ENTITY_TYPE);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, StarShipments.MODID);


    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LandingPadBlockEntity>> landingPad =
            BE_TYPES.register("landing_pad", () ->
                    BlockEntityType.Builder.of(LandingPadBlockEntity::new,
                            StarBlocks.landingPad.get()
                    ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TerminalBlockEntity>> exportTerminal =
            BE_TYPES.register("export_terminal", () ->
                    BlockEntityType.Builder.of(TerminalBlockEntity::new,
                            StarBlocks.exportTerminal.get()
                    ).build(null));

    public static void staticInit() {}
}
