package com.mltheuser.khtmlmarkdown.rules

import com.mltheuser.khtmlmarkdown.ConversionContext
import com.mltheuser.khtmlmarkdown.dom.KElement

/**
 * A rule that defines how to convert a specific HTML tag into Markdown.
 *
 * Implementations of this interface are registered in the
 * [com.mltheuser.khtmlmarkdown.registry.RuleRegistry] and are invoked when the converter encounters
 * a matching tag.
 */
public fun interface ConversionRule {
    /**
     * Converts the given HTML element to a Markdown string.
     *
     * @param element The HTML element to convert.
     * @param context The current conversion context, providing access to options and state.
     * @return The Markdown representation of the element.
     */
    public fun convert(element: KElement, context: ConversionContext): String
}
