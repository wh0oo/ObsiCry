package com.example.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.class_5819") // Verified PortalShape in 1.21.5
public abstract class PortalShapeMixin {
    @Redirect(
        method = "method_33574", // Verified frame check method in 1.21.5
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/class_2680;method_11646(Lnet/minecraft/class_2248;)Z" // BlockState.isOf
        )
    )
    private boolean allowCryingObsidian(BlockState state, Block block) {
        return state.isOf(Blocks.OBSIDIAN) || state.isOf(Blocks.CRYING_OBSIDIAN);
    }
}
