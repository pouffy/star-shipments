package com.pouffydev.star_shipments.common.block.terminal;

import com.pouffydev.star_shipments.registry.StarBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

public class TerminalBlockEntity extends LodestoneBlockEntity {

    public TerminalBlockEntity(BlockPos pos, BlockState state) {
        super(StarBlockEntities.exportTerminal.get(), pos, state);
    }


}
