package com.example.obsicry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class ObsiCryInitializer implements ModInitializer {
    @Override
    public void onInitialize() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClient) return ActionResult.PASS;

            if (player.getStackInHand(hand).getItem() != Items.FLINT_AND_STEEL) return ActionResult.PASS;

            BlockPos clickedPos = hitResult.getBlockPos();
            BlockPos placePos = clickedPos.offset(hitResult.getSide());

            if (tryCreateCustomPortal(world, placePos)) {
                return ActionResult.SUCCESS;
            }

            return ActionResult.PASS;
        });
    }

    private boolean tryCreateCustomPortal(World world, BlockPos pos) {
        // Try both axis directions: X and Z
        return createPortalIfValid(world, pos, Direction.Axis.X) || createPortalIfValid(world, pos, Direction.Axis.Z);
    }

    private boolean createPortalIfValid(World world, BlockPos origin, Direction.Axis axis) {
        Direction right = axis == Direction.Axis.X ? Direction.EAST : Direction.NORTH;
        Direction up = Direction.UP;

        int width = 0;
        int height = 0;

        // Find left edge
        BlockPos frameStart = origin;
        while (isFrameBlock(world.getBlockState(frameStart.offset(right.getOpposite())))) {
            frameStart = frameStart.offset(right.getOpposite());
        }

        // Measure width
        BlockPos cursor = frameStart;
        while (isFrameBlock(world.getBlockState(cursor))) {
            width++;
            cursor = cursor.offset(right);
        }

        // Measure height
        cursor = frameStart.offset(up);
        while (isFrameBlock(world.getBlockState(cursor))) {
            height++;
            cursor = cursor.offset(up);
        }

        if (width < 2 || width > 21 || height < 3 || height > 21) {
            return false;
        }

        // Check full frame
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                BlockPos current = frameStart.offset(right, x).offset(up, y);
                boolean isEdge = (x == 0 || x == width - 1 || y == 0 || y == height - 1);
                BlockState state = world.getBlockState(current);

                if (isEdge) {
                    if (!isFrameBlock(state)) return false;
                } else {
                    if (!world.getBlockState(current).isAir()) return false;
                }
            }
        }

        // Fill interior with portal blocks
        Block portalBlock = Blocks.NETHER_PORTAL;
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                BlockPos inner = frameStart.offset(right, x).offset(up, y);
                world.setBlockState(inner, portalBlock.getDefaultState().with(net.minecraft.block.NetherPortalBlock.AXIS, axis));
            }
        }

        return true;
    }

    private boolean isFrameBlock(BlockState state) {
        return state.isOf(Blocks.OBSIDIAN) || state.isOf(Blocks.CRYING_OBSIDIAN);
    }
}
