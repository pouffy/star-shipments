package com.pouffydev.star_shipments.common.block.terminal;

import com.pouffydev.star_shipments.common.block.landing_pad.LandingPadStructuralBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;

public class TerminalBlockItem extends BlockItem {

    public TerminalBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult place(BlockPlaceContext ctx) {
        InteractionResult result = super.place(ctx);
        if (result != InteractionResult.FAIL)
            return result;

        BlockPos pos = ctx.getClickedPos();
        Direction facing = ctx.getHorizontalDirection();
        BlockPos behind = pos.relative(facing.getOpposite());
        //check if the block behind is a landing pad
        //check if the block behind has a terminal
        if (ctx.getLevel().getBlockState(behind).getBlock() instanceof LandingPadStructuralBlock && ctx.getLevel().getBlockState(behind).hasProperty(LandingPadStructuralBlock.hasTerminal)) {
            return ctx.getLevel().getBlockState(behind).getValue(LandingPadStructuralBlock.hasTerminal) ? InteractionResult.FAIL : super.place(ctx);
        } else {
            return InteractionResult.FAIL;
        }
    }
}
