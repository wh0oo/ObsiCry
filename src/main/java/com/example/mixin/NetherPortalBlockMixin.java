package com.example.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin {

    @Inject(
        method = "method_60990", // this is the real method name in 1.21.5
        at = @At("HEAD"),
        cancellable = true
    )
    private static void obsicry$allowMixedPortalFrame(
        World world,
        BlockState state,
        BlockPos pos,
        CallbackInfoReturnable<Boolean> cir
    ) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                for (int dy = -1; dy <= 4; dy++) {
                    BlockPos checkPos = pos.add(dx, dy, dz);
                    BlockState checkState = world.getBlockState(checkPos);
                    if (checkState.isOf(Blocks.CRYING_OBSIDIAN)) {
                        cir.setReturnValue(true);
                        return;
                    }
                }
            }
        }
    }
}
