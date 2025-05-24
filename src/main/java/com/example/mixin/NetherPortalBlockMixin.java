package com.example.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin {

    @ModifyVariable(
        method = "method_60990", // Obfuscated name of portal-creation method in 1.21.5
        at = @At(value = "STORE"),
        ordinal = 0
    )
    private static boolean allowCryingObsidian(
        boolean original,
        World world,
        BlockState state,
        BlockPos pos
    ) {
        if (original) return true;

        // Scan surrounding blocks for Crying Obsidian
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                for (int dy = -1; dy <= 4; dy++) {
                    BlockPos checkPos = pos.add(dx, dy, dz);
                    BlockState checkState = world.getBlockState(checkPos);
                    if (checkState.isOf(Blocks.CRYING_OBSIDIAN)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
