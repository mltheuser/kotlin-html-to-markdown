# KHtmlToMarkdown

A Kotlin Multiplatform library for converting HTML to Markdown.

## Features

- **Extensible:** Easily add or replace rules for specific HTML tags.
- **Configurable:** Customize Markdown syntax (bullet characters, delimiters, etc.).
- **Multiplatform:** Works on JVM, JS, Native, and other Kotlin targets.
- **Robust:** Handles nested structures, tables, lists, and code blocks with syntax highlighting support.

## Documentation

For in-depth details on configuration, custom rules, and internal structures, please see the [Detailed Documentation](DOCUMENTATION.md).

## Quick Start

### Installation

Add the dependency to your `build.gradle.kts`:

```kotlin
implementation("io.github.mltheuser:khtmltomarkdown:1.+")
```

### Basic Usage

```kotlin
import com.mltheuser.khtmlmarkdown.KHtmlToMarkdown

fun main() {
    val html = "<h1>Hello World</h1><p>This is a <b>bold</b> statement.</p>"
    
    val converter = KHtmlToMarkdown.Builder().build()
    val markdown = converter.convert(html)
    
    println(markdown)
}
```

**Output:**

```markdown
# Hello World

This is a **bold** statement.
```

## Advanced Usage

### Customizing Options

You can configure the output style using the `options` block:

```kotlin
val converter = KHtmlToMarkdown.Builder()
    .options {
        bulletCharacter = "-" // Use dashes for lists
        strongDelimiter = "__" // Use underscores for bold
    }
    .build()
```

### Adding Custom Rules

You can add custom rules to handle specific tags or override default behavior:

```kotlin
val converter = KHtmlToMarkdown.Builder()
    .rule("custom-tag") { element, context ->
        // Custom logic here
        val content = context.processChildren(element)
        "{{ $content }}"
    }
    .build()
```

### Handling Tables

The library supports converting HTML tables to Markdown tables, including headers and alignment (basic support).

```html
<table>
  <thead>
    <tr>
      <th>Header 1</th>
      <th>Header 2</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Cell 1</td>
      <td>Cell 2</td>
    </tr>
  </tbody>
</table>
```

**Converts to:**

```markdown
| Header 1 | Header 2 |
|---|---|
| Cell 1 | Cell 2 |
```

## Supported Tags

- **Blocks:** `p`, `h1`-`h6`, `hr`, `br`, `pre`, `blockquote`
- **Inline:** `b`, `strong`, `i`, `em`, `s`, `del`, `code`, `a`, `img`
- **Lists:** `ul`, `ol`, `li` (nested lists supported)
- **Tables:** `table`, `thead`, `tbody`, `tfoot`, `tr`, `th`, `td`

## Contributing

Contributions are welcome! Please open an issue or submit a pull request.

## License

[MIT License](LICENSE)
