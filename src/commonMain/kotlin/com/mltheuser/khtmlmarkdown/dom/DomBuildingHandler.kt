package com.mltheuser.khtmlmarkdown.dom

import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler

/**
 * A [KsoupHtmlHandler] implementation that builds an internal DOM tree ([KElement]s and [KTextNode]
 * s) from the HTML parsing events.
 */
public class DomBuildingHandler : KsoupHtmlHandler {
    private val root = KElement("root")
    private var current: KElement = root

    /**
     * Called when an opening tag is encountered. Creates a new [KElement] and appends it to the
     * current parent.
     */
    override fun onOpenTag(name: String, attributes: Map<String, String>, isImplied: Boolean) {
        val newElement = KElement(name, attributes)
        current.appendChild(newElement)
        current = newElement
    }

    /**
     * Called when a closing tag is encountered. Traverses up the tree to find the matching open tag
     * and sets the current element to its parent.
     */
    override fun onCloseTag(name: String, isImplied: Boolean) {
        var temp: KElement? = current
        while (temp != null && temp != root) {
            if (temp.tagName == name) {
                current = temp.parent ?: root
                return
            }
            temp = temp.parent
        }
    }

    /** Called when text content is encountered. Appends a [KTextNode] to the current element. */
    override fun onText(text: String) {
        current.appendChild(KTextNode(text))
    }

    /**
     * Returns the root element of the built DOM tree.
     *
     * @return The root [KElement].
     */
    public fun getRoot(): KElement = root
}
