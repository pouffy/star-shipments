package com.pouffydev.star_shipments.common.block.landing_pad;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import team.lodestar.lodestone.systems.block.LodestoneEntityBlock;

public class LandingPadBlock<T extends LandingPadBlockEntity> extends LodestoneEntityBlock<T> {

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 3, 16);

    public LandingPadBlock(Properties properties) {
        super(properties);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos target = pPos.offset(x, 0, z);
                BlockState targetState = pLevel.getBlockState(target);
                if (targetState.getBlock() instanceof LandingPadStructuralBlock) {
                    pLevel.destroyBlock(target, false);
                }
            }
        }
    }
}
