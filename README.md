# KHtmlToMarkdown

A Kotlin Multiplatform library for converting HTML to Markdown.

[![Maven Central](https://img.shields.io/maven-central/v/io.github.mltheuser/khtmltomarkdown)](https://central.sonatype.com/artifact/io.github.mltheuser/khtmltomarkdown)
[![GitHub license](https://img.shields.io/github/license/mltheuser/kotlin-html-to-markdown)](https://github.com/mltheuser/kotlin-html-to-markdown/blob/main/LICENSE)
[![Kotlin](https://img.shields.io/badge/kotlin-2.2.20-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Build Status](https://img.shields.io/github/actions/workflow/status/mltheuser/kotlin-html-to-markdown/publish.yml)](https://github.com/mltheuser/kotlin-html-to-markdown/actions)

<!-- The "KMP" Flex Badge -->
![Badge-Android](https://img.shields.io/badge/platform-Android-3DDC84.svg?style=flat&logo=android)
![Badge-iOS](https://img.shields.io/badge/platform-iOS-CDCDCD.svg?style=flat&logo=apple)
![Badge-JVM](https://img.shields.io/badge/platform-JVM-DB413D.svg?style=flat&logo=openjdk)
![Badge-JS](https://img.shields.io/badge/platform-JS-F7DF1E.svg?style=flat&logo=javascript)
![Badge-Linux](https://img.shields.io/badge/platform-Linux-FCC624.svg?style=flat&logo=linux)

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
