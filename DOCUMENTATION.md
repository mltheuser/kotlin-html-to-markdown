# KHtmlToMarkdown Documentation

This document provides in-depth information on how to configure and extend the `KHtmlToMarkdown` library.

## Table of Contents

1. [Builder Setup](#builder-setup)
2. [Configuration Options](#configuration-options)
3. [Understanding Element and Context](#understanding-element-and-context)
4. [Creating Custom Rules](#creating-custom-rules)

---

## Builder Setup

The `KHtmlToMarkdown.Builder` is the entry point for creating a converter instance. It allows you to configure global options and register custom conversion rules.

```kotlin
val converter = KHtmlToMarkdown.Builder()
    .options {
        // Configure options here
    }
    .rule("tag-name") { element, context ->
        // Define custom rule here
        ""
    }
    .build()
```

### Methods

- **`options(block: OptionsBuilder.() -> Unit)`**: Configures global conversion settings.
- **`rule(tagName: String, handler: (KElement, ConversionContext) -> String)`**: Registers a simple lambda-based rule for a specific HTML tag.
- **`rule(tagName: String, rule: ConversionRule)`**: Registers a class-based `ConversionRule` implementation.
- **`build()`**: Creates the immutable `KHtmlToMarkdown` instance.

---

## Configuration Options

Inside the `.options {}` block, you can customize the Markdown output style.

| Option | Type | Default | Description |
| :--- | :--- | :--- | :--- |
| `bulletCharacter` | `String` | `*` | The character used for unordered list items (e.g., `*`, `-`, `+`). |
| `strongDelimiter` | `String` | `**` | The delimiter for bold text (e.g., `**` for `**text**`, `__` for `__text__`). |
| `emDelimiter` | `String` | `*` | The delimiter for italic text (e.g., `*` for `*text*`, `_` for `_text_`). |
| `linkStyle` | `LinkStyle` | `INLINE` | The style of links. Currently, only `LinkStyle.INLINE` (`[text](url)`) is supported. |

**Example:**

```kotlin
.options {
    bulletCharacter = "-"
    strongDelimiter = "__"
    emDelimiter = "_"
}
```

---

## Understanding Element and Context

When writing custom rules, you interact primarily with `KElement` and `ConversionContext`.

### KElement

`KElement` represents an HTML tag in the internal DOM.

- **`tagName`**: The name of the tag (e.g., "div", "span").
- **`attributes`**: A `Map<String, String>` containing the tag's attributes (e.g., `href`, `class`, `id`).
- **`children`**: A list of child nodes (`KNode`), which can be other `KElement`s or `KTextNode`s.
- **`parent`**: The parent `KElement` (nullable).

**Useful Methods:**
- **`hasClass(className: String)`**: Checks if the element has a specific CSS class.

### ConversionContext

`ConversionContext` holds the state of the current conversion process and provides utility methods.

- **`options`**: Access to the global `ConverterOptions`.
- **`listType`**: The current list context (`NONE`, `UNORDERED`, `ORDERED`).
- **`indentLevel`**: The current indentation depth (used for nested lists).
- **`inTable`**: Boolean indicating if we are currently processing inside a table.

**Key Methods:**

- **`processChildren(element: KElement): String`**:
  This is the most important method. It recursively processes all children of the given element using the registered rules and returns the combined Markdown string. You should almost always call this in your custom rules unless you want to suppress the element's content.

- **`subContext(...)`**:
  Creates a new context for processing children with modified state (e.g., increasing indent, entering a table).

---

## Creating Custom Rules

You can extend the library to handle custom tags or override default behavior for standard tags.

### Example 1: Simple Wrapper

Let's say you want to convert a `<callout>` tag to a blockquote with a specific prefix.

```kotlin
.rule("callout") { element, context ->
    val content = context.processChildren(element).trim()
    "\n\n> **Note:** $content\n\n"
}
```

### Example 2: Handling Attributes (Custom Link)

Suppose you have a special `<user-link id="123">Name</user-link>` tag that should convert to a profile URL.

```kotlin
.rule("user-link") { element, context ->
    val userId = element.attributes["id"]
    val userName = context.processChildren(element)
    
    if (userId != null) {
        "[$userName](https://example.com/users/$userId)"
    } else {
        userName // Fallback to just text
    }
}
```

### Example 3: Complex Logic (Syntax Highlighting Hint)

If you have a `div` with a specific class that should be treated as a code block.

```kotlin
.rule("div") { element, context ->
    if (element.hasClass("code-snippet")) {
        val language = element.attributes["data-lang"] ?: ""
        val code = context.processChildren(element).trim()
        "\n\n```$language\n$code\n```\n\n"
    } else {
        // Fallback: Just render children normally for other divs
        context.processChildren(element)
    }
}
```

### Example 4: Nested Contexts

If you are implementing a custom structure that requires indentation (like a custom list), you can use `subContext`.

```kotlin
.rule("custom-list") { element, context ->
    // Create a context with increased indentation
    val newContext = context.subContext(incrementIndent = true)
    val content = newContext.processChildren(element)
    "\n$content"
}
```

---

## Custom Context Data

For more complex rules, you might need to pass state down the tree (e.g., "am I inside a blockquote?", "what is the current font size?"). You can use `ContextDataKey` for this.

### 1. Define a Key

Create a key object. The type parameter ensures type safety.

```kotlin
val InBlockquoteKey = ContextDataKey<Boolean>("InBlockquote")
```

### 2. Set Data in a Rule

Use `context.with(key, value)` to create a new context with the data.

```kotlin
.rule("blockquote") { element, context ->
    val newContext = context.with(InBlockquoteKey, true)
    val content = newContext.processChildren(element).trim()
    "\n\n> $content\n\n"
}
```

### 3. Retrieve Data in a Child Rule

Use `context.get(key)` to retrieve the data.

```kotlin
.rule("p") { element, context ->
    val inBlockquote = context.get(InBlockquoteKey) ?: false
    val content = context.processChildren(element).trim()
    
    if (inBlockquote) {
        // Special handling for paragraphs inside blockquotes
        content + "\n"
    } else {
        "\n\n" + content + "\n\n"
    }
}
```
