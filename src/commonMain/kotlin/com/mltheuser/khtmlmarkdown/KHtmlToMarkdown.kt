package com.mltheuser.khtmlmarkdown

import com.mltheuser.khtmlmarkdown.dom.DomBuildingHandler
import com.mltheuser.khtmlmarkdown.dom.KElement
import com.mltheuser.khtmlmarkdown.registry.RuleRegistry
import com.mltheuser.khtmlmarkdown.rules.*
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser

/**
 * The main entry point for converting HTML to Markdown.
 *
 * Use the [Builder] to configure and create an instance of this class.
 */
public class KHtmlToMarkdown
private constructor(private val registry: RuleRegistry, private val options: ConverterOptions) {

    /**
     * Converts the provided HTML string to Markdown.
     *
     * @param html The HTML string to convert.
     * @return The generated Markdown string.
     */
    public fun convert(html: String): String {
        val handler = DomBuildingHandler()
        val parser = KsoupHtmlParser(handler = handler)
        parser.write(html)
        parser.end()

        val root = handler.getRoot()
        // Try to find body, otherwise use root
        val body = findBody(root) ?: root

        val context = DefaultConversionContext(registry, options)
        return context.processChildren(body).trim()
    }

    private fun findBody(element: KElement): KElement? {
        if (element.tagName == "body") return element
        for (child in element.children) {
            if (child is KElement) {
                val found = findBody(child)
                if (found != null) return found
            }
        }
        return null
    }

    /**
     * Builder for creating [KHtmlToMarkdown] instances with custom configuration.
     *
     * It comes pre-configured with a set of default rules for standard HTML tags.
     */
    public class Builder {
        private val registry = RuleRegistry()
        private var options = ConverterOptions()

        init {
            registerDefaultRules()
        }

        private fun registerDefaultRules() {
            // Blocks
            registry.register("p", ParagraphRule)
            registry.register("br", BrRule)
            registry.register("hr", HrRule)
            (1..6).forEach { level -> registry.register("h$level", HeadingRule) }
            registry.register("pre", PreCodeRule)

            // Inline
            registry.register("b", StrongRule)
            registry.register("strong", StrongRule)
            registry.register("i", EmphasisRule)
            registry.register("em", EmphasisRule)
            registry.register("s", StrikethroughRule)
            registry.register("del", StrikethroughRule)
            registry.register("strike", StrikethroughRule)
            registry.register("code", CodeRule)
            registry.register("a", LinkRule)
            registry.register("img", ImageRule)

            // Lists
            registry.register("ul", ListRule)
            registry.register("ol", ListRule)
            registry.register("li", ListItemRule)

            // Tables
            registry.register("table", TableRule)
            registry.register("thead", TableSectionRule)
            registry.register("tbody", TableSectionRule)
            registry.register("tfoot", TableSectionRule)
            registry.register("tr", TrRule)
            registry.register("th", TableCellRule)
            registry.register("td", TableCellRule)
        }

        /**
         * Configures global conversion options.
         *
         * @param block A lambda to configure [OptionsBuilder].
         */
        public fun options(block: OptionsBuilder.() -> Unit): Builder {
            val builder = OptionsBuilder(options)
            builder.block()
            options = builder.build()
            return this
        }

        /**
         * Adds or replaces a rule for a specific HTML tag.
         *
         * @param tagName The tag name to handle (e.g., "div").
         * @param handler A lambda that converts the element to a Markdown string.
         */
        public fun rule(
                tagName: String,
                handler: (KElement, ConversionContext) -> String
        ): Builder {
            registry.register(
                    tagName,
                    ConversionRule { element, context -> handler(element, context) }
            )
            return this
        }

        /**
         * Adds or replaces a rule using a [ConversionRule] implementation.
         *
         * @param tagName The tag name to handle.
         * @param rule The rule implementation.
         */
        public fun rule(tagName: String, rule: ConversionRule): Builder {
            registry.register(tagName, rule)
            return this
        }

        /** Builds the [KHtmlToMarkdown] instance. */
        public fun build(): KHtmlToMarkdown {
            return KHtmlToMarkdown(registry, options)
        }
    }

    /** DSL builder for [ConverterOptions]. */
    public class OptionsBuilder(current: ConverterOptions) {
        /** The character to use for unordered lists (e.g., "*", "-", "+"). */
        public var bulletCharacter: String = current.bulletCharacter
        /** The delimiter to use for strong/bold text (e.g., "**", "__"). */
        public var strongDelimiter: String = current.strongDelimiter
        /** The delimiter to use for emphasized/italic text (e.g., "*", "_"). */
        public var emDelimiter: String = current.emDelimiter

        internal fun build() =
                ConverterOptions(
                        bulletCharacter = bulletCharacter,
                        strongDelimiter = strongDelimiter,
                        emDelimiter = emDelimiter
                )
    }
}
