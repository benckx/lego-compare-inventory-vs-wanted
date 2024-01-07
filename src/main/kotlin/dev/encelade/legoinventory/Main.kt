package dev.encelade.legoinventory

fun main() {
    val mapper = ItemMapper()
    val inventory = mapper.parseItems(  "inventory.xml")
    inventory.forEach { println(it) }
}
