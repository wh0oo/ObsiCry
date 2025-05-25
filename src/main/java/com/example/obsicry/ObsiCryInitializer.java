package com.example.obsicry;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.api.ModInitializer;

public class ObsiCryInitializer implements ModInitializer {
    @Override
    public void onInitialize() {
        // Initialize MixinExtras (required for @ModifyReturnValue to work)
        MixinExtrasBootstrap.init();

        // You can add other initialization code here if needed
    }
}
