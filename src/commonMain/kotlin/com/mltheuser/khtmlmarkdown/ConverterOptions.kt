package com.mltheuser.khtmlmarkdown

/**
 * Configuration options for the HTML to Markdown converter.
 *
 * @property bulletCharacter The character to use for unordered lists (e.g., "*", "-", "+"). Default
 * is "*".
 * @property strongDelimiter The delimiter to use for strong/bold text (e.g., "**", "__"). Default
 * is "**".
 * @property emDelimiter The delimiter to use for emphasized/italic text (e.g., "*", "_"). Default
 * is "*".
 * @property linkStyle The style to use for links (inline or reference). Default is
 * [LinkStyle.INLINE].
 */
public data class ConverterOptions(
        val bulletCharacter: String = "*",
        val strongDelimiter: String = "**",
        val emDelimiter: String = "*",
        val linkStyle: LinkStyle = LinkStyle.INLINE
)

/** Defines the style of Markdown links. */
public enum class LinkStyle {
    /** Standard inline links: `[text](url)`. */
    INLINE,
    // REFERENCE // [text][id] (Reserved for future impl)
}
