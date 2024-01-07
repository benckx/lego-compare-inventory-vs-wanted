package dev.encelade.inventory.model

abstract class WantedListModification(
    val itemId: String,
    val color: Color,
    val neededQuantity: Int,
    val inventoryQuantity: Int,
) {

    abstract fun quantityToRemove(): Int

    class SufficientQuantity(itemId: String, color: Color, neededQuantity: Int, inventoryQuantity: Int) :
        WantedListModification(itemId, color, neededQuantity, inventoryQuantity) {

        override fun quantityToRemove(): Int {
            return neededQuantity
        }

        override fun toString(): String {
            return "you already have the part in quantity -> $itemId (you have $inventoryQuantity and you need $neededQuantity)"
        }
    }

    class InsufficientQuantity(itemId: String, color: Color, neededQuantity: Int, inventoryQuantity: Int) :
        WantedListModification(itemId, color, neededQuantity, inventoryQuantity) {

        override fun quantityToRemove(): Int {
            return neededQuantity - inventoryQuantity
        }

        override fun toString(): String {
            return "you already have the part, but in insufficient quantity -> $itemId (you have $inventoryQuantity but need $neededQuantity)"
        }
    }

}
