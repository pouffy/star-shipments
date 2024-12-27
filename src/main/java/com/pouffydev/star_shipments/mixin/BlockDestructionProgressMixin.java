package com.pouffydev.star_shipments.mixin;

import com.pouffydev.star_shipments.util.block.render.BlockDestructionProgressExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.BlockDestructionProgress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Set;

@Mixin(BlockDestructionProgress.class)
public class BlockDestructionProgressMixin implements BlockDestructionProgressExtension {
    @Unique
    private Set<BlockPos> create$extraPositions;

    @Override
    public Set<BlockPos> getExtraPositions() {
        return create$extraPositions;
    }

    @Override
    public void setExtraPositions(Set<BlockPos> positions) {
        create$extraPositions = positions;
    }
}
