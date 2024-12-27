package com.pouffydev.star_shipments.common.block.landing_pad;

import com.mojang.serialization.MapCodec;
import com.pouffydev.star_shipments.registry.StarBlocks;
import com.pouffydev.star_shipments.util.CustomName;
import com.pouffydev.star_shipments.util.NoTab;
import com.pouffydev.star_shipments.util.block.render.MultiPosDestructionHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;
import team.lodestar.lodestone.systems.block.LodestoneDirectionalBlock;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@SuppressWarnings("removal")
@NoTab
@CustomName(name = "Landing Pad")
public class LandingPadStructuralBlock extends LodestoneDirectionalBlock {
    public static final BooleanProperty hasTerminal = BooleanProperty.create("has_terminal");

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 3, 16);

    public LandingPadStructuralBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(hasTerminal, false).setValue(FACING, Direction.NORTH));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(hasTerminal, FACING);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return context.getPlayer() != null && context.getPlayer().isShiftKeyDown() ? this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection()).setValue(hasTerminal, false) : this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(hasTerminal, false);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.INVISIBLE;
    }

    protected VoxelShape getVisualShape(BlockState p_309057_, BlockGetter p_308936_, BlockPos p_308956_, CollisionContext p_309006_) {
        return Shapes.empty();
    }

    protected boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        return adjacentBlockState.is(this) || super.skipRendering(state, adjacentBlockState, side);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return StarBlocks.landingPad.get().asItem().getDefaultInstance();
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (stillValid(pLevel, pPos, pState, false))
            pLevel.destroyBlock(getMaster(pLevel, pPos, pState), true);
    }

    public BlockState playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (stillValid(pLevel, pPos, pState, false)) {
            BlockPos masterPos = getMaster(pLevel, pPos, pState);
            pLevel.destroyBlockProgress(masterPos.hashCode(), masterPos, -1);
            if (!pLevel.isClientSide() && pPlayer.isCreative())
                pLevel.destroyBlock(masterPos, false);
        }
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
        return pState;
    }

    public static BlockPos getMaster(BlockGetter level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        BlockPos targetedPos = pos.relative(direction);
        BlockState targetedState = level.getBlockState(targetedPos);
        if (targetedState.is(StarBlocks.landingPadStructural.get()))
            return getMaster(level, targetedPos, targetedState);
        return targetedPos;
    }

    public boolean stillValid(BlockGetter level, BlockPos pos, BlockState state, boolean directlyAdjacent) {
        if (!state.is(this))
            return false;

        Direction direction = state.getValue(FACING);
        BlockPos targetedPos = pos.relative(direction);
        BlockState targetedState = level.getBlockState(targetedPos);

        if (!directlyAdjacent && stillValid(level, targetedPos, targetedState, true))
            return true;
        return targetedState.getBlock() instanceof LandingPadBlock<?>;
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!stillValid(pLevel, pPos, pState, false))
            pLevel.setBlockAndUpdate(pPos, Blocks.AIR.defaultBlockState());
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientBlockExtensions> consumer) {
        consumer.accept(new RenderProperties());
    }

    @Override
    public boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2,
                                     LivingEntity entity, int numberOfParticles) {
        return true;
    }

    public static class RenderProperties implements IClientBlockExtensions, MultiPosDestructionHandler {

        @Override
        public boolean addDestroyEffects(BlockState state, Level Level, BlockPos pos, ParticleEngine manager) {
            return true;
        }

        @Override
        public boolean addHitEffects(BlockState state, Level level, HitResult target, ParticleEngine manager) {
            if (target instanceof BlockHitResult bhr) {
                BlockPos targetPos = bhr.getBlockPos();
                LandingPadStructuralBlock landingPadStructuralBlock = StarBlocks.landingPadStructural.get();
                if (landingPadStructuralBlock.stillValid(level, targetPos, state, false))
                    manager.crack(LandingPadStructuralBlock.getMaster(level, targetPos, state), bhr.getDirection());
                return true;
            }
            return IClientBlockExtensions.super.addHitEffects(state, level, target, manager);
        }

        @Override
        @Nullable
        public Set<BlockPos> getExtraPositions(ClientLevel level, BlockPos pos, BlockState blockState, int progress) {
            LandingPadStructuralBlock landingPadStructuralBlock = StarBlocks.landingPadStructural.get();
            if (!landingPadStructuralBlock.stillValid(level, pos, blockState, false))
                return null;
            HashSet<BlockPos> set = new HashSet<>();
            set.add(LandingPadStructuralBlock.getMaster(level, pos, blockState));
            return set;
        }
    }

    public BlockState setHasTerminal(BlockState state, boolean hasTerminal) {
        return state.setValue(LandingPadStructuralBlock.hasTerminal, hasTerminal);
    }
}
