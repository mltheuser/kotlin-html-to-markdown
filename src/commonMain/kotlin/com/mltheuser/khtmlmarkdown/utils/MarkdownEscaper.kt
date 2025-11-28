package com.mltheuser.khtmlmarkdown.utils

/**
 * Utility object for escaping characters in text to prevent them from being interpreted as Markdown
 * syntax.
 */
internal object MarkdownEscaper {

    /**
     * Escapes special Markdown characters in the given text.
     *
     * This function applies a series of replacements to ensure that the text is rendered literally
     * in Markdown, rather than triggering formatting. It handles global escapes, start-of-line
     * escapes, and lookahead escapes.
     *
     * @param text The text to escape.
     * @param inTable Whether the text is inside a table cell. If true, pipe characters ('|') are
     * also escaped.
     * @return The escaped text string.
     */
    fun escape(text: String, inTable: Boolean = false): String {
        var result = text

        // 1. Global Escapes (Always)
        // Escape backslashes first to avoid double-escaping later
        result = result.replace("\\", "\\\\")

        // Escape pipes only inside tables (where they are column delimiters)
        if (inTable) {
            result = result.replace("|", "\\|")
        }

        // Escape backticks (code)
        result = result.replace("`", "\\`")

        // Escape opening brackets (links)
        result = result.replace("[", "\\[")

        // Escape asterisks (emphasis/strong) if they are not surrounded by spaces
        result = result.replace(Regex("(?<! )\\*|\\*(?! )"), "\\\\*")

        // 2. Start-of-Line Escapes
        // Escape headers (#) at the start of a line
        result = result.replace(Regex("^#", RegexOption.MULTILINE), "\\\\#")

        // Escape list items (-, +) at the start of a line
        result = result.replace(Regex("^([-+]) ", RegexOption.MULTILINE), "\\\\$1 ")

        // Escape blockquotes (>) at the start of a line
        result = result.replace(Regex("^>", RegexOption.MULTILINE), "\\\\>")

        // Escape ordered list items (1.) at the start of a line
        result = result.replace(Regex("^(\\d+)\\.(?= )", RegexOption.MULTILINE), "\$1\\\\.")

        // Escape fenced code blocks (~~~) at the start of a line
        result = result.replace(Regex("^~~~", RegexOption.MULTILINE), "\\\\~~~")

        // 3. Lookahead Escapes
        // Escape exclamation marks followed by a link start
        result = result.replace(Regex("!(?=\\\\\\[)"), "\\\\!")

        // Escape less-than signs that look like HTML tags
        result = result.replace(Regex("<(?=[a-zA-Z/!])"), "\\\\<")

        // Escape underscores that are inside words or at word boundaries (emphasis)
        result = result.replace(Regex("\\b_|_\\b"), "\\\\_")

        return result
    }
}
