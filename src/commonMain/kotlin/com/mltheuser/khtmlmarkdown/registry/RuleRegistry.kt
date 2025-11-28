package com.mltheuser.khtmlmarkdown.registry

import com.mltheuser.khtmlmarkdown.rules.ConversionRule

/** A registry for holding [ConversionRule]s mapped by HTML tag names. */
public class RuleRegistry {
    private val rules = mutableMapOf<String, ConversionRule>()

    /**
     * Registers a rule for a specific HTML tag.
     *
     * @param tagName The name of the tag (e.g., "div", "p"). Case-insensitive handling depends on
     * usage,
     * ```
     *                but typically lowercase is expected.
     * @param rule
     * ```
     * The conversion rule to apply for this tag.
     */
    public fun register(tagName: String, rule: ConversionRule) {
        rules[tagName] = rule
    }

    /**
     * Retrieves the rule registered for the given tag name.
     *
     * @param tagName The name of the tag to look up.
     * @return The registered [ConversionRule], or `null` if no rule is found.
     */
    public fun getRule(tagName: String): ConversionRule? {
        return rules[tagName]
    }
}
