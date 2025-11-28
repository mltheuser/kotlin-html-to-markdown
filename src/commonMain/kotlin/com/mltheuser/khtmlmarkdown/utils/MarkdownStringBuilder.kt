package com.mltheuser.khtmlmarkdown.utils

/**
 * A specialized StringBuilder wrapper that handles Markdown-specific string building concerns.
 *
 * Primarily, it manages newline normalization to ensure that no more than two consecutive newlines
 * (one blank line) are ever appended, maintaining clean Markdown output.
 */
internal class MarkdownStringBuilder {
    private val sb = StringBuilder()

    /**
     * Appends the given text to the builder, ensuring correct newline handling.
     *
     * @param text The text to append.
     */
    fun append(text: String) {
        if (text.isEmpty()) return
        appendValidatingNewlines(text)
    }

    /** Returns the accumulated string content. */
    override fun toString(): String = sb.toString()

    /**
     * Appends text while ensuring that the total number of consecutive newlines (trailing in buffer
     * + leading in new text) does not exceed 2.
     *
     * @param text The text to append.
     */
    private fun appendValidatingNewlines(text: String) {
        // Count trailing newlines in existing buffer
        var sbNewlines = 0
        for (i in sb.length - 1 downTo 0) {
            if (sb[i] == '\n') sbNewlines++ else break
        }

        // Count leading newlines in incoming text
        var textNewlines = 0
        for (char in text) {
            if (char == '\n') textNewlines++ else break
        }

        val totalNewlines = sbNewlines + textNewlines
        val maxNewlines = 2 // Max 1 blank line

        if (totalNewlines > maxNewlines) {
            val toSkip = totalNewlines - maxNewlines
            if (toSkip <= textNewlines) {
                sb.append(text.substring(toSkip))
            } else {
                // Rare edge case: SB has more than max newlines?
                // Trim incoming entirely and rely on SB.
                sb.append(text.trimStart('\n'))
            }
        } else {
            sb.append(text)
        }
    }
}
