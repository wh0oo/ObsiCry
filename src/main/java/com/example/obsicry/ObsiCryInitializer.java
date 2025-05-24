package com.example.obsicry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ObsiCryInitializer implements ModInitializer {
    @Override
    public void onInitialize() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClient) return ActionResult.PASS;

            BlockPos clickedPos = hitResult.getBlockPos();
            BlockPos placePos = clickedPos.offset(hitResult.getSide());

            boolean isFlintOrFireCharge = player.getStackInHand(hand).getItem() == Items.FLINT_AND_STEEL
                    || player.getStackInHand(hand).getItem() == Items.FIRE_CHARGE;

            if (isFlintOrFireCharge && tryCreateCustomPortal((ServerWorld) world, placePos)) {
                world.playSound(null, placePos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return ActionResult.SUCCESS;
            }

            return ActionResult.PASS;
        });
    }

    private boolean tryCreateCustomPortal(World world, BlockPos pos) {
        return createPortalIfValid(world, pos, Direction.Axis.X) || createPortalIfValid(world, pos, Direction.Axis.Z);
    }

    private boolean createPortalIfValid(World world, BlockPos origin, Direction.Axis axis) {
        Direction right = axis == Direction.Axis.X ? Direction.EAST : Direction.NORTH;
        Direction up = Direction.UP;

        BlockPos base = origin;

        // Scan left to find edge
        while (isFrameBlock(world.getBlockState(base.offset(right.getOpposite())))) {
            base = base.offset(right.getOpposite());
        }

        // Measure width
        int width = 0;
        BlockPos cursor = base;
        while (isFrameBlock(world.getBlockState(cursor))) {
            width++;
            cursor = cursor.offset(right);
            if (width > 21) return false;
        }

        // Measure height
        int height = 0;
        cursor = base.offset(up);
        while (isFrameBlock(world.getBlockState(cursor))) {
            height++;
            cursor = cursor.offset(up);
            if (height > 21) return false;
        }

        if (width < 2 || height < 3) return false;

        // Validate full frame
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                BlockPos current = base.offset(right, x).offset(up, y);
                BlockState state = world.getBlockState(current);
                boolean edge = x == 0 || x == width - 1 || y == 0 || y == height - 1;

                if (edge) {
                    if (!isFrameBlock(state)) return false;
                } else {
                    if (!state.isAir() && !state.isOf(Blocks.FIRE)) return false;
                }
            }
        }

        // Place portal blocks
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                BlockPos current = base.offset(right, x).offset(up, y);
                world.setBlockState(current, Blocks.NETHER_PORTAL.getDefaultState().with(net.minecraft.block.NetherPortalBlock.AXIS, axis));
            }
        }

        return true;
    }

    private boolean isFrameBlock(BlockState state) {
        return state.isOf(Blocks.OBSIDIAN) || state.isOf(Blocks.CRYING_OBSIDIAN);
    }
}
