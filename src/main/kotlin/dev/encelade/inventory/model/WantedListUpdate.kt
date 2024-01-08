package dev.encelade.inventory.model

abstract class WantedListUpdate(
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

    /**
     * When you already have enough of this part in your inventory
     */
    class SufficientQuantity(itemId: String, color: Color, neededQuantity: Int, inventoryQuantity: Int) :
        WantedListUpdate(itemId, color, neededQuantity, inventoryQuantity) {

        override fun quantityToRemove(): Int = neededQuantity
        override fun quantityMissing(): Int = 0

    }

    /**
     * When you have the part in your inventory, but not in sufficient quantity; and therefore you can order fewer parts
     */
    class InsufficientQuantity(itemId: String, color: Color, neededQuantity: Int, inventoryQuantity: Int) :
        WantedListUpdate(itemId, color, neededQuantity, inventoryQuantity) {

        override fun quantityToRemove(): Int = inventoryQuantity
        override fun quantityMissing(): Int = neededQuantity - inventoryQuantity

    }

}
