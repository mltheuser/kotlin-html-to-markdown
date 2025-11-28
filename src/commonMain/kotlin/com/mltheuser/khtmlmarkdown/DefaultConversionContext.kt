package com.mltheuser.khtmlmarkdown

import com.mltheuser.khtmlmarkdown.dom.HtmlConstants
import com.mltheuser.khtmlmarkdown.dom.KElement
import com.mltheuser.khtmlmarkdown.dom.KNode
import com.mltheuser.khtmlmarkdown.dom.KTextNode
import com.mltheuser.khtmlmarkdown.registry.RuleRegistry
import com.mltheuser.khtmlmarkdown.utils.MarkdownEscaper
import com.mltheuser.khtmlmarkdown.utils.MarkdownStringBuilder

/**
 * The default implementation of [ConversionContext].
 *
 * It manages the state of the conversion (nesting, list type, table context) and orchestrates the
 * processing of nodes using the [RuleRegistry].
 */
internal data class DefaultConversionContext(
        private val registry: RuleRegistry,
        override val options: ConverterOptions,
        override val listType: ListType = ListType.NONE,
        override val indentLevel: Int = 0,
        override val inTable: Boolean = false,
        private val customData: Map<ContextDataKey<*>, Any> = emptyMap()
) : ConversionContext {

    override fun subContext(
            listType: ListType?,
            incrementIndent: Boolean,
            inTable: Boolean?
    ): ConversionContext {
        return this.copy(
                listType = listType ?: this.listType,
                indentLevel = if (incrementIndent) this.indentLevel + 1 else this.indentLevel,
                inTable = inTable ?: this.inTable,
                customData = this.customData
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(key: ContextDataKey<T>): T? {
        return customData[key] as? T
    }

    override fun <T> with(key: ContextDataKey<T>, value: T): ConversionContext {
        val newMap = customData.toMutableMap()
        newMap[key] = value as Any
        return this.copy(customData = newMap)
    }

    /**
     * Iterates through the children of the given element, converting each one and appending the
     * result.
     *
     * It handles whitespace trimming around block elements to ensure clean Markdown output.
     */
    override fun processChildren(element: KElement): String {
        val builder = MarkdownStringBuilder()
        val children = element.children

        for (i in children.indices) {
            val child = children[i]
            var result = processNode(child)

            if (child is KTextNode) {
                val prev = children.getOrNull(i - 1)
                val next = children.getOrNull(i + 1)
                val isPrevBlock = prev != null && HtmlConstants.isBlock(prev)
                val isNextBlock = next != null && HtmlConstants.isBlock(next)

                // Trim whitespace if adjacent to a block element to avoid extra spaces
                if (isPrevBlock) result = result.trimStart()
                if (isNextBlock) result = result.trimEnd()
            }

            builder.append(result)
        }
        return builder.toString()
    }

    /**
     * Converts a single node (Element or Text).
     *
     * If it's an Element, it looks up a rule in the registry. If no rule is found, it falls back to
     * processing the children (treating the tag as transparent). If it's a Text node, it cleans and
     * escapes the text.
     */
    private fun processNode(node: KNode): String {
        return when (node) {
            is KTextNode -> cleanText(node.text)
            is KElement -> {
                val rule = registry.getRule(node.tagName.lowercase())
                if (rule != null) {
                    rule.convert(node, this)
                } else {
                    processChildren(node)
                }
            }
        }
    }

    /**
     * Collapses multiple whitespace characters into a single space and escapes Markdown characters.
     */
    private fun cleanText(text: String): String {
        val collapsed = text.replace("\\s+".toRegex(), " ")
        return MarkdownEscaper.escape(collapsed, inTable = inTable)
    }
}
