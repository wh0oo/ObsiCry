// Mojang mappings 1.21.11
package com.example.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PortalShape.class)
public abstract class PortalShapeMixin {

    @ModifyReturnValue(method = "method_30487", at = @At("RETURN"), remap = false)
    private static boolean allowCryingObsidian(boolean original, @Local(argsOnly = true) BlockState state) {
        return original || state.is(Blocks.CRYING_OBSIDIAN);
    }
}
