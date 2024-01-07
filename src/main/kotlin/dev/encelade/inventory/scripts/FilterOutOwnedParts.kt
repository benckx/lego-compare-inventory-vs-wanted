package dev.encelade.inventory.scripts

import dev.encelade.inventory.services.InventoryFilterer

fun main() {
    InventoryFilterer().filterOutInventoryParts("data/wanted-razor-crest-75331.xml")
}
