package com.example.obsicry;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.api.ModInitializer;

public class ObsiCryInitializer implements ModInitializer {
    @Override
    public void onInitialize() {
        // Initialize MixinExtras so that its annotations work correctly
        MixinExtrasBootstrap.initialize();
    }
}
