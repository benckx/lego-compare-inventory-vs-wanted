package dev.encelade.inventory.services

import dev.encelade.inventory.model.InventoryPart
import dev.encelade.inventory.model.InventoryPart.Companion.areEqualsByIdAndColor
import dev.encelade.inventory.model.WantedListModification

object InventoryFilterer {

    fun analyzePartsToFilterOut(
        inventory: List<InventoryPart>,
        wanted: List<InventoryPart>,
    ): List<WantedListModification> {
        val result = mutableListOf<WantedListModification>()

        wanted.forEach { wantedItem ->
            inventory
                .find { inventoryItem -> areEqualsByIdAndColor(inventoryItem, wantedItem) }
                ?.let { inventoryMatchingItem ->
                    if (inventoryMatchingItem.quantity >= wantedItem.quantity) {
                        result += WantedListModification.SufficientQuantity(
                            wantedItem.partId,
                            wantedItem.color,
                            wantedItem.quantity,
                            inventoryMatchingItem.quantity
                        )
                    } else {
                        result += WantedListModification.InsufficientQuantity(
                            wantedItem.partId,
                            wantedItem.color,
                            wantedItem.quantity,
                            inventoryMatchingItem.quantity
                        )
                    }
                }
        }
        return result
            .toList()
            .sortedBy { it.color.code }
            .sortedBy { it.itemId }
            .sortedBy {
                when (it) {
                    is WantedListModification.SufficientQuantity -> 0
                    is WantedListModification.InsufficientQuantity -> 1
                    else -> Int.MAX_VALUE
                }
            }

    }

}
