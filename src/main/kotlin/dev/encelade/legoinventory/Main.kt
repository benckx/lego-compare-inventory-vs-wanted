package dev.encelade.legoinventory

fun main() {
    val mapper = ItemMapper()
    val inventory = mapper.parseItems("inventory.xml")
    val wanted = mapper.parseItems("wanted-razor-crest-75331.xml")
    ItemComparator.analyze(inventory, wanted)
}
