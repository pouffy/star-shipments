package com.pouffydev.star_shipments.common.block.terminal;

import com.pouffydev.star_shipments.common.block.landing_pad.LandingPadStructuralBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import team.lodestar.lodestone.systems.block.LodestoneEntityBlock;

public class TerminalBlock<T extends TerminalBlockEntity> extends LodestoneEntityBlock<T> {
    public static final DirectionProperty FACING;
    public static final BooleanProperty ACTIVE;

    public TerminalBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(ACTIVE, false).setValue(FACING, Direction.NORTH));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return context.getPlayer() != null && context.getPlayer().isShiftKeyDown() ? this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection()).setValue(ACTIVE, false) : this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(ACTIVE, false);
    }

    static {
        FACING = BlockStateProperties.HORIZONTAL_FACING;
        ACTIVE = BooleanProperty.create("active");
    }
}
