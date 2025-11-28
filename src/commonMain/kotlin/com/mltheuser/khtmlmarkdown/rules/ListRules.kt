package com.mltheuser.khtmlmarkdown.rules

import com.mltheuser.khtmlmarkdown.ConversionContext
import com.mltheuser.khtmlmarkdown.ListType
import com.mltheuser.khtmlmarkdown.dom.KElement

/**
 * Converts `<ul>` and `<ol>` tags to Markdown lists.
 *
 * Handles nested lists by creating a sub-context with increased indentation.
 */
public object ListRule : ConversionRule {
    override fun convert(element: KElement, context: ConversionContext): String {
        val type = if (element.tagName == "ol") ListType.ORDERED else ListType.UNORDERED

        // If we are currently inside a list (context.listType != NONE),
        // then THIS list is a nested list.
        val isNested = context.listType != ListType.NONE

        // We always increment indent for children of this list if we are already in a list logic,
        // or if we are starting a list.
        // Actually, logic: Root UL (Indent 0). LI (Indent 0). Nested UL (Indent 1). LI (Indent 1).
        // So if 'isNested' is true, it means we are deeper.

        val newContext =
                context.subContext(
                        listType = type,
                        incrementIndent = isNested // Only increment indent if we are nested
                )

        val content = newContext.processChildren(element)
        if (content.isBlank()) return ""

        return if (isNested) {
            // Nested list: No surrounding newlines.
            // The items inside provide their own '\n' prefix.
            content
        } else {
            // Root list: Block element behavior.
            "\n\n" + content + "\n\n"
        }
    }
}

/**
 * Converts `<li>` tags to Markdown list items.
 *
 * Handles indentation and list markers (bullets or numbers).
 */
public object ListItemRule : ConversionRule {
    override fun convert(element: KElement, context: ConversionContext): String {
        val indent = "    ".repeat(context.indentLevel)

        val marker =
                if (context.listType == ListType.ORDERED) {
                    "1. "
                } else {
                    // Use configured bullet character
                    "${context.options.bulletCharacter} "
                }

        val content = context.processChildren(element).trim()
        return "\n$indent$marker$content"
    }
}
