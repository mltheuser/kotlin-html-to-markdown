// src/commonMain/kotlin/com/mltheuser/khtmlmarkdown/dom/Dom.kt
package com.mltheuser.khtmlmarkdown.dom

/** Base class for all nodes in the internal DOM representation. */
public sealed class KNode {
    /** The parent element of this node. Null if the node is a root or not attached to a tree. */
    public var parent: KElement? = null
}

/**
 * Represents an HTML element (tag).
 *
 * @property tagName The name of the tag (e.g., "div", "p").
 * @property attributes A map of attribute names to their values.
 */
public class KElement(
        public val tagName: String,
        public val attributes: Map<String, String> = emptyMap()
) : KNode() {
    /** The list of child nodes contained within this element. */
    public val children: MutableList<KNode> = mutableListOf()

    /**
     * Appends a child node to this element and sets this element as the child's parent.
     *
     * @param node The node to append.
     */
    public fun appendChild(node: KNode) {
        node.parent = this
        children.add(node)
    }

    /**
     * Checks if this element has a specific CSS class.
     *
     * @param className The class name to check for.
     * @return `true` if the "class" attribute contains the specified class name, `false` otherwise.
     */
    public fun hasClass(className: String): Boolean {
        val classAttr = attributes["class"] ?: return false
        return classAttr.split(" ").contains(className)
    }
}

/**
 * Represents a text node containing raw text content.
 *
 * @property text The text content of the node.
 */
public class KTextNode(public val text: String) : KNode()
