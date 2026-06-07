package com.example.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PortalShape.class)
public abstract class PortalShapeMixin {

    @ModifyReturnValue(
        method = "lambda$static$0",
        at = @At("RETURN")
    )
    private static boolean obsicry$allowCryingObsidian(
        boolean original,
        BlockState state,
        BlockGetter level,
        BlockPos pos
    ) {
        return original || state.is(Blocks.CRYING_OBSIDIAN);
    }
}