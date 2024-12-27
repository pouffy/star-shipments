package com.pouffydev.star_shipments.util.block.render;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Set;

public interface MultiPosDestructionHandler {
    /**
     * Returned set must be mutable and must not be changed after it is returned.
     */
    @Nullable
    Set<BlockPos> getExtraPositions(ClientLevel level, BlockPos pos, BlockState blockState, int progress);
}
