package com.pouffydev.star_shipments.util.block.render;

import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;
import java.util.Set;

public interface BlockDestructionProgressExtension {
    @Nullable
    Set<BlockPos> getExtraPositions();

    void setExtraPositions(@Nullable Set<BlockPos> positions);
}
