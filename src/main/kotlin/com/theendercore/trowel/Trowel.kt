package com.theendercore.trowel

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.*
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult

class Trowel : Item(FabricItemSettings().maxCount(1)) {
    private var count = 0
    override fun useOnBlock(c: ItemUsageContext): ActionResult {
//        return this.useOnBlockRecursive(c, 0)
//        if (!c.world.isClient) {
//            count++
//            val player: PlayerEntity? = c.player
//            if (player != null) {
//                val pool = LinkedList<ItemStack>()
//                for (i in 0..8) {
//                    val itemStack = player.inventory!!.getStack(i)
//                    if (itemStack.item !is AirBlockItem && itemStack.item is BlockItem) pool.add(itemStack)
//                }
//                if (pool.size > 0) {
//                    val item = pool[Random.create().nextBetween(0, pool.size - 1)]
//                    val iCtx = ItemPlacementContext(
//                        player, c.hand, item, BlockHitResult(c.hitPos, c.side, c.blockPos, c.hitsInsideBlock())
//                    )
//                    val actionResult = item.useOnBlock(iCtx)
//
//                    if (actionResult.shouldIncrementStat()) {
//                        count = 0
//                        val state: BlockState = c.world.getBlockState(iCtx.blockPos)
//                        val blockSoundGroup: BlockSoundGroup = state.soundGroup
//                        c.world.playSound(
//                            null,
//                            iCtx.blockPos,
//                            getPlaceSound(state),
//                            SoundCategory.BLOCKS,
//                            (blockSoundGroup.getVolume() + 1.0f) / 2.0f,
//                            blockSoundGroup.getPitch() * 0.8f
//                        )
//                        return ActionResult.SUCCESS
//                    } else if (count < pool.size) {
//                        this.useOnBlock(c)
//                    }
//                }
//            }
//        }


        if (c.world.isClient) return ActionResult.PASS

        val player = c.player ?: return ActionResult.PASS
        val inv = player.inventory ?: return ActionResult.PASS

        val placeable = (0..8).mapNotNull {
            val stack = inv.getStack(it)
            if (isPlaceable(stack) && stack != null) (it to stack)
            else null
        }

        if (placeable.isEmpty()) return ActionResult.PASS
//        val random = Random(Clock.System.now().nanosecondsOfSecond)
        return place(placeable, inv, player, c, player.inventory.selectedSlot)
    }


    private fun place(
        placeable: List<Pair<Int, ItemStack>>, inv: PlayerInventory,
        player: PlayerEntity, c: ItemUsageContext, originalSlot: Int,
    ): ActionResult {
        return if (placeable.isEmpty()) {
            inv.selectedSlot = originalSlot
            ActionResult.FAIL
        } else placeable.random().let { (slot, stack) ->
            inv.selectedSlot = slot
            val placeCtx = newPlacementContext(player, stack, c)
            if (placeCtx.canPlace()) {
                if (stack.useOnBlock(placeCtx).shouldIncrementStat()) {
                    placeSound(placeCtx)
                    inv.selectedSlot = originalSlot
                    return@let ActionResult.SUCCESS
                }
            }
            return@let this.place(placeable.filter { it.first != slot }, inv, player, c, originalSlot)
        }
    }

    private fun placeSound(c: ItemUsageContext) {
        c.world.getBlockState(c.blockPos).let {
            c.world.playSound(
                null, c.blockPos, it.soundGroup.placeSound,
                SoundCategory.BLOCKS,
                (it.soundGroup.getVolume() + 1.0f) / 2.0f,
                it.soundGroup.getPitch() * 0.8f
            )
        }
    }

    private fun newPlacementContext(player: PlayerEntity, stack: ItemStack, context: ItemUsageContext) =
        ItemPlacementContext(
            player, context.hand, stack,
            BlockHitResult(context.hitPos, context.side, context.blockPos, context.hitsInsideBlock())
        )

    private fun isPlaceable(stack: ItemStack): Boolean = stack.item !is AirBlockItem && stack.item is BlockItem
    private fun getPlaceSound(state: BlockState): SoundEvent = state.soundGroup.placeSound

}