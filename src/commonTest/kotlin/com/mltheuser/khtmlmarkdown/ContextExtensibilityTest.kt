package com.mltheuser.khtmlmarkdown

import kotlin.test.Test
import kotlin.test.assertEquals

class ContextExtensibilityTest {

    @Test
    fun testCustomContextDataPassing() {
        val testKey = ContextDataKey<String>("TestKey")

        val converter =
                KHtmlToMarkdown.Builder()
                        .rule("parent") { element, context ->
                            val newContext = context.with(testKey, "PassedValue")
                            newContext.processChildren(element)
                        }
                        .rule("child") { _, context ->
                            val value = context.get(testKey) ?: "NotFound"
                            "Value: $value"
                        }
                        .build()

        val html = "<parent><child></child></parent>"
        val markdown = converter.convert(html)

        assertEquals("Value: PassedValue", markdown.trim())
    }

    @Test
    fun testCustomContextDataIsolation() {
        val testKey = ContextDataKey<Int>("Counter")

        val converter =
                KHtmlToMarkdown.Builder()
                        .rule("container") { element, context ->
                            // Set value to 1
                            val ctx1 = context.with(testKey, 1)
                            val part1 = ctx1.processChildren(element)

                            // Set value to 2 (should not affect ctx1 or parent context)
                            val ctx2 = context.with(testKey, 2)
                            val part2 = ctx2.processChildren(element)

                            "$part1 | $part2"
                        }
                        .rule("item") { _, context -> (context.get(testKey) ?: 0).toString() }
                        .build()

        // We reuse the same child tag logic, but the parent calls it with different contexts
        val html = "<container><item></item></container>"
        val markdown = converter.convert(html)

        assertEquals("1 | 2", markdown.trim())
    }
}
