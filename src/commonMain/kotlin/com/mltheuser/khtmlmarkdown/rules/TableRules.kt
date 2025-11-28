package com.mltheuser.khtmlmarkdown.rules

import com.mltheuser.khtmlmarkdown.ConversionContext
import com.mltheuser.khtmlmarkdown.dom.KElement

/**
 * Converts `<table>` tags to Markdown tables.
 *
 * Sets up the table context for child elements.
 */
public object TableRule : ConversionRule {
    override fun convert(element: KElement, context: ConversionContext): String {
        // Enter table context
        val newContext = context.subContext(inTable = true)
        val content = newContext.processChildren(element).trim()
        if (content.isEmpty()) return ""
        return "\n\n" + content + "\n\n"
    }
}

/**
 * Handles structural table tags (`<thead>`, `<tbody>`, `<tfoot>`).
 *
 * These are transparently processed to render their children.
 */
public object TableSectionRule : ConversionRule {
    override fun convert(element: KElement, context: ConversionContext): String {
        return context.processChildren(element)
    }
}

/**
 * Converts `<tr>` tags to Markdown table rows.
 *
 * Also handles the generation of the separator row (e.g., `|---|---|`) for the header.
 */
public object TrRule : ConversionRule {
    override fun convert(element: KElement, context: ConversionContext): String {
        val content = context.processChildren(element).trim()
        if (content.isEmpty()) return ""

        // The children (Td/Th) will return strings like " content |"
        // So 'content' is " cell1 | cell2 |"
        // We prepend the opening pipe.
        val row = "| $content"

        // Separator Logic:
        // We need to add |---|---| if this row is a header.
        // Heuristic: If inside <thead> OR if children are <th>
        val isHeader =
                element.parent?.tagName == "thead" ||
                        element.children.any { (it as? KElement)?.tagName == "th" }

        if (isHeader) {
            // Calculate columns based on actual Td/Th children processed
            val colCount =
                    element.children.count {
                        it is KElement && (it.tagName == "td" || it.tagName == "th")
                    }

            if (colCount > 0) {
                val separator = "|---".repeat(colCount) + "|"
                return row + "\n" + separator + "\n"
            }
        }

        return row + "\n"
    }
}

/**
 * Converts `<td>` and `<th>` tags to Markdown table cells.
 *
 * Appends a trailing pipe `|` to the content.
 */
public object TableCellRule : ConversionRule {
    override fun convert(element: KElement, context: ConversionContext): String {
        val content = context.processChildren(element).trim()
        // Return content with trailing pipe.
        // Leading space handled here for clean alignment: " content |"
        return " $content |"
    }
}
