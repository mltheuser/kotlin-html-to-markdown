package com.mltheuser.khtmlmarkdown.rules

import com.mltheuser.khtmlmarkdown.ConversionContext
import com.mltheuser.khtmlmarkdown.dom.KElement

/**
 * Converts `<p>` tags to Markdown paragraphs.
 *
 * Paragraphs are separated by blank lines (double newlines).
 */
public object ParagraphRule : ConversionRule {
    override fun convert(element: KElement, context: ConversionContext): String {
        val content = context.processChildren(element).trim()
        if (content.isEmpty()) return ""
        // Prepend AND append newlines. Builder handles deduplication.
        return "\n\n" + content + "\n\n"
    }
}

/**
 * Converts `<h1>` through `<h6>` tags to Markdown ATX headings.
 *
 * Example: `<h1>Title</h1>` -> `# Title`
 */
public object HeadingRule : ConversionRule {
    override fun convert(element: KElement, context: ConversionContext): String {
        val content = context.processChildren(element).trim()
        if (content.isEmpty()) return ""

        val level = element.tagName.last().digitToIntOrNull() ?: 1
        val prefix = "#".repeat(level) + " "

        return "\n\n" + prefix + content + "\n\n"
    }
}

/**
 * Converts `<br>` tags to Markdown line breaks.
 *
 * Uses two trailing spaces followed by a newline.
 */
public object BrRule : ConversionRule {
    override fun convert(element: KElement, context: ConversionContext): String {
        return "  \n"
    }
}

/**
 * Converts `<hr>` tags to Markdown horizontal rules.
 *
 * Uses `---` surrounded by blank lines.
 */
public object HrRule : ConversionRule {
    override fun convert(element: KElement, context: ConversionContext): String {
        return "\n\n---\n\n"
    }
}
