package dev.encelade.inventory.services

import dev.encelade.inventory.model.InventoryPart
import dev.encelade.inventory.model.InventoryPart.Companion.areEqualsByIdAndColor

object InventoryFilterer {

    fun analyzeToFilterOut(inventory: List<InventoryPart>, wanted: List<InventoryPart>) {
        wanted.forEach { wantedItem ->
            inventory.find { inventoryItem -> areEqualsByIdAndColor(inventoryItem, wantedItem) }?.let { matchingItem ->
                if (matchingItem.quantity >= wantedItem.quantity) {
                    println("you already have the part in quantity -> $wantedItem (you have ${matchingItem.quantity} and you need ${wantedItem.quantity})")
                } else {
                    println("you already have the part, but in insufficient quantity -> $wantedItem (you have ${matchingItem.quantity} but need ${wantedItem.quantity})")
                }
            }
        }
    }

}
