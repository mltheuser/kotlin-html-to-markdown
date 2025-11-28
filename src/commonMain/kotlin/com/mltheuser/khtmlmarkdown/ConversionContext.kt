package com.mltheuser.khtmlmarkdown

import com.mltheuser.khtmlmarkdown.dom.KElement

/** Represents the type of list currently being processed. */
public enum class ListType {
    /** Not currently inside a list. */
    NONE,
    /** Inside an unordered list (`<ul>`). */
    UNORDERED, // ul
    /** Inside an ordered list (`<ol>`). */
    ORDERED // ol
}

/**
 * Key for storing custom data in the [ConversionContext].
 *
 * @param T The type of data stored with this key.
 * @property name A descriptive name for the key, used for debugging and equality checks.
 */
public class ContextDataKey<T>(public val name: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as ContextDataKey<*>
        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

/**
 * Context provided to [com.mltheuser.khtmlmarkdown.rules.ConversionRule]s during the conversion
 * process.
 *
 * It holds the current state of the conversion, such as configuration options, nesting level, and
 * whether the processing is currently inside a specific structure like a table or list.
 *
 * It also supports storing custom data via [ContextDataKey]s, allowing for extensible state
 * management in custom rules.
 */
public interface ConversionContext {
    /** Global configuration options for the converter. */
    val options: ConverterOptions // New property

    /** The type of list currently being processed, if any. */
    val listType: ListType

    /** The current indentation level (depth of nesting). */
    val indentLevel: Int

    /** Whether the processing is currently inside a table. */
    val inTable: Boolean

    /**
     * Processes the children of the given element and returns their combined Markdown
     * representation.
     *
     * @param element The element whose children should be processed.
     * @return The resulting Markdown string.
     */
    fun processChildren(element: KElement): String

    /**
     * Creates a new context based on the current one, but with modified state.
     *
     * @param listType The new list type, or null to keep the current one.
     * @param incrementIndent Whether to increment the indentation level.
     * @param inTable The new table state, or null to keep the current one.
     * @return A new [ConversionContext] instance.
     */
    fun subContext(
            listType: ListType? = null,
            incrementIndent: Boolean = false,
            inTable: Boolean? = null
    ): ConversionContext

    /**
     * Retrieves custom data associated with the given key.
     *
     * @param key The key to look up.
     * @return The value associated with the key, or null if not found.
     */
    fun <T> get(key: ContextDataKey<T>): T?

    /**
     * Creates a new context with the given key-value pair added or updated.
     *
     * @param key The key to associate the value with.
     * @param value The value to store.
     * @return A new [ConversionContext] instance with the updated data.
     */
    fun <T> with(key: ContextDataKey<T>, value: T): ConversionContext
}
