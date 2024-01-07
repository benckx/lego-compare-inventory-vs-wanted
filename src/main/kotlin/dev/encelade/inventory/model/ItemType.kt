package dev.encelade.inventory.model

enum class ItemType {

    PART,
    MINI_FIGURE;

    fun asXml(): String {
        return when (this) {
            PART -> "P"
            MINI_FIGURE -> "M"
        }
    }

    companion object {

        fun fromXml(xmlItemType: String): ItemType {
            return when (xmlItemType) {
                "P" -> PART
                "M" -> MINI_FIGURE
                else -> throw IllegalArgumentException("unknown item type: $xmlItemType")
            }
        }

    }

}
