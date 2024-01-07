package dev.encelade.legoinventory.model

data class Item(
    val itemId: String,
    val color: Int,
    val minQty: Int,
) {

    companion object {

        fun areEqualsByIdAndColor(item1: Item, item2: Item): Boolean {
            return item1.itemId == item2.itemId && item1.color == item2.color
        }

    }

}
