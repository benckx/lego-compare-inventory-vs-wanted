package dev.encelade.inventory.model

abstract class WantedListModification(
    val itemId: String,
    val color: Color,
    val neededQuantity: Int,
    val inventoryQuantity: Int,
) {

    abstract fun quantityToRemove(): Int
    abstract fun quantityMissing(): Int

    override fun toString(): String {
        return "${javaClass.simpleName}(itemId='$itemId', color=$color, neededQuantity=$neededQuantity, inventoryQuantity=$inventoryQuantity)"
    }

    class SufficientQuantity(itemId: String, color: Color, neededQuantity: Int, inventoryQuantity: Int) :
        WantedListModification(itemId, color, neededQuantity, inventoryQuantity) {

        override fun quantityToRemove(): Int = neededQuantity
        override fun quantityMissing(): Int = 0

    }

    class InsufficientQuantity(itemId: String, color: Color, neededQuantity: Int, inventoryQuantity: Int) :
        WantedListModification(itemId, color, neededQuantity, inventoryQuantity) {

        override fun quantityToRemove(): Int = inventoryQuantity
        override fun quantityMissing(): Int = neededQuantity - inventoryQuantity

    }

}
