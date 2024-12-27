package com.pouffydev.star_shipments.common.block.landing_pad;

import com.pouffydev.star_shipments.registry.StarBlockEntities;
import com.pouffydev.star_shipments.registry.StarBlocks;
import com.pouffydev.star_shipments.util.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

import javax.annotation.Nonnull;
import java.util.*;

public class LandingPadBlockEntity extends LodestoneBlockEntity {

    public static final Map<Direction.Axis, Set<BlockPos>> OFFSETS = new EnumMap<>(Direction.Axis.class);
    private int waitTime;
    private boolean shipInTransit;
    private boolean hasShip;
    private boolean hasTerminal;

    public LandingPadBlockEntity(BlockPos pos, BlockState state) {
        super(StarBlockEntities.landingPad.get(), pos, state);
    }

    static {
        for (Direction.Axis axis : Iterate.axes) {
            HashSet<BlockPos> offsets = new HashSet<>();
            for (Direction d : Iterate.directions) {
                if (d.getAxis() == axis)
                    continue;
                BlockPos centralOffset = BlockPos.ZERO.relative(d, 2);
                offsets.add(centralOffset);
                for (Direction d2 : Iterate.directions) {
                    if (d2.getAxis() == axis)
                        continue;
                    if (d2.getAxis() == d.getAxis())
                        continue;
                    offsets.add(centralOffset.relative(d2));
                }
            }
            OFFSETS.put(axis, offsets);
        }

    }

    @Override
    public void tick() {
        super.tick();
        if (level == null)
            return;
        Direction.Axis axis = Direction.Axis.Y;
        for (Direction side : Iterate.directions) {
            if (side.getAxis() == axis)
                continue;
            for (boolean secondary : Iterate.falseAndTrue) {
                Direction targetSide = secondary ? side.getClockWise(axis) : side;
                BlockPos structurePos = (secondary ? getBlockPos().relative(side) : getBlockPos()).relative(targetSide);
                BlockState occupiedState = getLevel().getBlockState(structurePos);
                BlockState requiredStructure = StarBlocks.landingPadStructural.get().defaultBlockState()
                        .setValue(LandingPadStructuralBlock.FACING, targetSide.getOpposite());
                if (occupiedState == requiredStructure)
                    continue;
                if (!occupiedState.canBeReplaced()) {
                    getLevel().destroyBlock(getBlockPos(), false);
                    return;
                }
                getLevel().setBlockAndUpdate(structurePos, requiredStructure);
            }
        }

        for (BlockPos offset : OFFSETS.get(axis)) {
            if (!hasTerminal && level.getBlockState(offset).hasProperty(LandingPadStructuralBlock.hasTerminal) && level.getBlockState(offset).getValue(LandingPadStructuralBlock.hasTerminal)) {
                hasTerminal = true;
            }
            if (hasTerminal) {
                level.getBlockState(offset).setValue(LandingPadStructuralBlock.hasTerminal, true);
            }
        }

        if (shipInTransit) {
            hasShip = false;
        }

        if (hasShip) {
            waitTime--;
        }
    }

    public void setHasShip(boolean hasShip) {
        this.hasShip = hasShip;
    }



    @Override
    protected void loadAdditional(@NotNull CompoundTag compound, HolderLookup.Provider lookupProvider) {
        super.loadAdditional(compound, lookupProvider);
        waitTime = compound.getInt("waitTime");
        hasShip = compound.getBoolean("hasShip");
        shipInTransit = compound.getBoolean("shipInTransit");
        hasTerminal = compound.getBoolean("hasTerminal");
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound, HolderLookup.@NotNull Provider lookupProvider) {
        super.saveAdditional(compound, lookupProvider);
        compound.putInt("waitTime", waitTime);
        compound.putBoolean("hasShip", hasShip);
        compound.putBoolean("shipInTransit", shipInTransit);
        compound.putBoolean("hasTerminal", hasTerminal);
    }
}
