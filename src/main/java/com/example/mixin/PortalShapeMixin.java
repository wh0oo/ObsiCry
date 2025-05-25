package com.example.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.dimension.NetherPortal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NetherPortal.class)
public abstract class PortalShapeMixin {

    @ModifyReturnValue(method = "method_30487", at = @At("RETURN"), remap = false)
    private static boolean allowCryingObsidian(boolean original, @Local(argsOnly = true) BlockState state) {
        return original || state.isOf(Blocks.CRYING_OBSIDIAN);
    }
}
