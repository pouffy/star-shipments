package com.pouffydev.star_shipments.common.block.landing_pad;

import com.pouffydev.star_shipments.registry.StarBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;

public class LandingPadBlockItem extends BlockItem {

    public LandingPadBlockItem() {
        super(StarBlocks.landingPad.get(), new Properties());
    }

    @Override
    public InteractionResult place(BlockPlaceContext ctx) {
        InteractionResult result = super.place(ctx);
        if (result != InteractionResult.FAIL)
            return result;
        BlockPos pos = ctx.getClickedPos();
        //the block is 3x3 plane centered on the clicked position. Check if the space is free before placing
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos target = pos.offset(x, 0, z);
                if(!ctx.getLevel().getBlockState(target).canBeReplaced(ctx)) {
                    return InteractionResult.FAIL;
                }
            }
        }

        return result;
    }
}
