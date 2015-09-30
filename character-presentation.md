## Character Terminology 

### Grapheme
The smallest unit of a writing system

### Glyph
The shape of a marking representing a grapheme

### Font
A set of glyphs in the same typeface with a particular set of parameters, such as weight, slope, width, serif, monospaced, etc.

### Font Family / Typeface
A range of fonts that share an overall design

### Character
Intended to be a computer representation of a grapheme, but it got more complicated than that.

### Code Point
Represents a single character in an encoding

### Code Unit
Smallest computer unit with which to build code points

### Encoding
The rules for converting code units to characters in a charset

### Charset
The set of characters supported by an encoding

### Unicode
A standard whose charset includes all characters, in an attempt to unify all languages by assigning a code point to each grapheme.

### UTF
Unicode transformation format.  A mapping from code units to unicode code points.

### UTF-8
A variable length mapping from 8-bit code units to code points, where each code point may be represented as 1-6 code units.
Designed to be compatible with ASCII.  

### UTF-16
Originally intended to be a fixed length mapping, now a variable length mapping between 16-bit code units and unicode code points.

### ASCII
American Standard Code for Information Interchange.  Includes the characters we are most familiar with.

### ISO-8859-1
Character encoding with support for languages used in the Americas, Western Europe, Oceania, and much of Africa.  Noteworthy because it is the default encoding of HTTP content.

### Serif
A small line attached to the ends of glyphs

### Sans Serif
No serif's

##G Clef Sample

### Symbol

[g clef](http://www.fileformat.info/info/unicode/char/1d11e/index.htm)

### output

```text
unicode code point (4) 0 1 d1 1e
high surrogate     (2) d8 34
low surrogate      (2) dd 1e
utf 8 bytes        (4) f0 9d 84 9e
utf 16 bytes       (6) fe ff d8 34 dd 1e
```

### sample application

```scala
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

object ClefSample extends App {
  val highSurrogate = '\uD834'
  val lowSurrogate = '\uDD1E'
  val gClef = "\uD834\uDD1E"

  println(formatLine("unicode code point", bytes(gClef.codePointAt(0))))
  println(formatLine("high surrogate    ", bytes(highSurrogate)))
  println(formatLine("low surrogate     ", bytes(lowSurrogate)))
  println(formatLine("utf 8 bytes       ", gClef.getBytes(StandardCharsets.UTF_8)))
  println(formatLine("utf 16 bytes      ", gClef.getBytes(StandardCharsets.UTF_16)))

  def formatLine(caption: String, bytes: Array[Byte]): String = {
    val bytesString = bytes.map(hexByte).mkString(" ")
    val size = bytes.size
    s"$caption ($size) $bytesString"
  }

  def bytes(x: Int): Array[Byte] = ByteBuffer.allocate(4).putInt(x).array()

  def bytes(x: Char): Array[Byte] = ByteBuffer.allocate(2).putChar(x).array()

  def hexByte(byte: Byte): String = {
    f"$byte%x"
  }
}
```
