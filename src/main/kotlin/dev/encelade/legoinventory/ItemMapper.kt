package dev.encelade.legoinventory

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

import dev.encelade.legoinventory.model.Item
import kotlin.text.Charsets.UTF_8

class ItemMapper {

    private val delegate =
        XmlMapper()
            .registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)


    private val collectionType = delegate.typeFactory.constructCollectionType(List::class.java, Item::class.java)!!

    fun parseItems(resourceFileName: String): List<Item> {
        val fileContent = ItemMapper::class.java.getResource("/$resourceFileName")!!.readText(UTF_8)
        return delegate.readValue<List<Item>>(fileContent, collectionType)!!
    }

}
