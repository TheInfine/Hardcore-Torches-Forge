package com.github.wolfiewaffle.hardcore_torches.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class TorchTools {

    public static void displayParticle(SimpleParticleType particle, BlockState state, Level world, BlockPos pos, float spread) {
        double d = (double)pos.getX() + 0.5;
        double e = (double)pos.getY() + 0.7;
        double f = (double)pos.getZ() + 0.5;

        if (particle != null) {
            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                Direction dir2 = dir.getOpposite();

                world.addParticle(particle, d + 0.27D * (double)dir2.getStepX(), e + 0.22D, f + 0.27D * (double)dir2.getStepZ(), 0.0D, 0.0D, 0.0D);
            } else {
                world.addParticle(particle, d, e, f, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public static void displayParticle(SimpleParticleType particle, BlockState state, Level world, BlockPos pos) {
        displayParticle(particle, state, world, pos, 0f);
    }
}
