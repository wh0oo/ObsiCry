package com.example.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.world.portal.PortalShape$Frame")
public abstract class PortalShapeMixin {
    @Redirect(
        method = "isValidFrameBlock",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"
        )
    )
    private boolean allowCryingObsidian(BlockState state, Block block) {
        return state.isOf(Blocks.OBSIDIAN) || state.isOf(Blocks.CRYING_OBSIDIAN);
    }
}
