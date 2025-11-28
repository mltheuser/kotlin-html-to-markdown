package com.mltheuser.khtmlmarkdown.dom

/** Constants and helper functions related to HTML tags and structure. */
public object HtmlConstants {
    /**
     * A set of HTML tag names that are considered block-level elements. These elements typically
     * start on a new line and take up the full width available.
     */
    public val blockTags: Set<String> =
            setOf(
                    "address",
                    "article",
                    "aside",
                    "blockquote",
                    "canvas",
                    "dd",
                    "div",
                    "dl",
                    "dt",
                    "fieldset",
                    "figcaption",
                    "figure",
                    "footer",
                    "form",
                    "h1",
                    "h2",
                    "h3",
                    "h4",
                    "h5",
                    "h6",
                    "header",
                    "hr",
                    "li",
                    "main",
                    "nav",
                    "noscript",
                    "ol",
                    "p",
                    "pre",
                    "section",
                    "table",
                    "thead",
                    "tbody",
                    "tfoot",
                    "tr",
                    "th",
                    "td",
                    "ul",
                    "video",
                    "body",
                    "html",
                    "root"
            )

    /**
     * Checks if a given [KNode] is a block-level element.
     *
     * @param node The node to check.
     * @return `true` if the node is a [KElement] and its tag name is in [blockTags], `false`
     * otherwise.
     */
    public fun isBlock(node: KNode): Boolean {
        return node is KElement && blockTags.contains(node.tagName.lowercase())
    }
}
