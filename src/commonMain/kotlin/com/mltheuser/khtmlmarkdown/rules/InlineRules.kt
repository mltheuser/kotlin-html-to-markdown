package com.mltheuser.khtmlmarkdown.rules

import com.mltheuser.khtmlmarkdown.ConversionContext
import com.mltheuser.khtmlmarkdown.dom.KElement
import com.mltheuser.khtmlmarkdown.dom.KTextNode

/**
 * Helper function to wrap content with delimiters while ensuring valid whitespace handling.
 *
 * Markdown requires that delimiters (like `**` or `_`) are directly adjacent to the content they
 * wrap, without intervening spaces. This function moves leading/trailing spaces outside the
 * delimiters.
 *
 * Example: `<b> foo </b>` -> ` **foo** ` (instead of `** foo **`)
 */
private fun wrapWithValidWhitespace(content: String, delimiter: String): String {
    if (content.isEmpty()) return ""

    var text = content
    var prefix = ""
    var suffix = ""

    // Move leading space out
    if (text.startsWith(" ")) {
        prefix = " "
        text = text.trimStart()
    }
    // Move trailing space out
    if (text.endsWith(" ")) {
        suffix = " "
        text = text.trimEnd()
    }

    if (text.isEmpty()) return "$prefix$suffix" // contained only space

    return "$prefix$delimiter$text$delimiter$suffix"
}

/**
 * Converts `<b>` and `<strong>` tags to Markdown bold text.
 *
 * Uses the configured strong delimiter (default `**`).
 */
public object StrongRule : ConversionRule {
    override fun convert(element: KElement, context: ConversionContext): String {
        val content = context.processChildren(element)
        return wrapWithValidWhitespace(content, context.options.strongDelimiter)
    }
}

/**
 * Converts `<i>` and `<em>` tags to Markdown italic text.
 *
 * Uses the configured emphasis delimiter (default `*`).
 */
public object EmphasisRule : ConversionRule {
    override fun convert(element: KElement, context: ConversionContext): String {
        val content = context.processChildren(element)
        return wrapWithValidWhitespace(content, context.options.emDelimiter)
    }
}

/**
 * Converts `<s>`, `<del>`, and `<strike>` tags to Markdown strikethrough text.
 *
 * Uses `~~` as the delimiter.
 */
public object StrikethroughRule : ConversionRule {
    override fun convert(element: KElement, context: ConversionContext): String {
        val content = context.processChildren(element)
        return wrapWithValidWhitespace(content, "~~")
    }
}

/**
 * Converts `<code>` tags to Markdown inline code.
 *
 * Wraps content in backticks (` ` `). Handles escaping of backticks inside the code by using double
 * backticks if necessary.
 */
public object CodeRule : ConversionRule {
    override fun convert(element: KElement, context: ConversionContext): String {
        // Extract content raw (no escaping)
        val sb = StringBuilder()
        fun extractText(element: KElement) {
            for (child in element.children) {
                when (child) {
                    is KTextNode -> sb.append(child.text)
                    is KElement -> extractText(child)
                }
            }
        }
        extractText(element)
        val content = sb.toString()

        if (content.isEmpty()) return ""

        // Handle backticks inside the code
        val delimiter = if (content.contains("`")) "``" else "`"
        // If the content starts or ends with a backtick, we need spaces
        val padding = if (content.startsWith("`") || content.endsWith("`")) " " else ""

        return "$delimiter$padding$content$padding$delimiter"
    }
}

/**
 * Converts `<a>` tags to Markdown links.
 *
 * Format: `[text](href "title")`
 */
public object LinkRule : ConversionRule {
    override fun convert(element: KElement, context: ConversionContext): String {
        val content = context.processChildren(element)
        val href = element.attributes["href"] ?: ""
        val title = element.attributes["title"]

        if (href.isEmpty()) return content

        val titlePart = if (title.isNullOrEmpty()) "" else " \"$title\""
        return "[$content]($href$titlePart)"
    }
}

/**
 * Converts `<img>` tags to Markdown images.
 *
 * Format: `![alt](src "title")`
 */
public object ImageRule : ConversionRule {
    override fun convert(element: KElement, context: ConversionContext): String {
        val src = element.attributes["src"] ?: ""
        val alt = element.attributes["alt"] ?: ""
        val title = element.attributes["title"]

        if (src.isEmpty()) return "" // Markdown images must have a source

        val titlePart = if (title.isNullOrEmpty()) "" else " \"$title\""
        return "![$alt]($src$titlePart)"
    }
}
