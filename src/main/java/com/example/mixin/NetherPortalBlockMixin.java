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
        method = "trySpawnPortal",
        at = @At(value = "STORE"),
        ordinal = 0
    )
    private static boolean allowCryingObsidian(boolean original, World world, BlockPos pos) {
        if (original) return true;

        // Try checking for crying obsidian manually
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                for (int dy = -1; dy <= 4; dy++) {
                    BlockPos checkPos = pos.add(dx, dy, dz);
                    BlockState state = world.getBlockState(checkPos);
                    if (state.isOf(Blocks.CRYING_OBSIDIAN)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
