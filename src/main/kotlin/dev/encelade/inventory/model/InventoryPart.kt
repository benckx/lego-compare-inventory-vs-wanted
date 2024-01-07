package dev.encelade.inventory.model

data class InventoryPart(
    val partId: String,
    val quantity: Int,
    val colorCode: Int,
    val colorName: String?,
) {

    companion object {

        fun areEqualsByIdAndColor(part1: InventoryPart, part2: InventoryPart): Boolean {
            return part1.partId == part2.partId && part1.colorCode == part2.colorCode
        }

    }

}
