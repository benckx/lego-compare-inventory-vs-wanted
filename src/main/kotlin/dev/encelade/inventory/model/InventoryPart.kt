package dev.encelade.inventory.model

data class InventoryPart(
    val itemType: ItemType,
    val partId: String,
    val quantity: Int,
    val color: Color,
) {

    companion object {

        fun areEqualsByIdAndColor(part1: InventoryPart, part2: InventoryPart): Boolean {
            return part1.partId == part2.partId && part1.color.code == part2.color.code
        }

    }

}
