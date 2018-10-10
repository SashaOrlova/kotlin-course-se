import org.junit.Assert.*
import org.junit.Test
import ru.hse.hw.dsl.*

class TexTest {
    @Test
    fun testDocument() {
        val sb = StringBuilder()
        document {
            documentclass("slides", "10pt")
            usepackage("geometry", "margin=2cm")
        }.render(sb, "")
        val expected = """
            \documentclass[10pt]{slides}
            \usepackage[margin=2cm]{geometry}
            \begin{document}
            \end{document}
        """.trimIndent()
        assertEquals(expected, sb.toString().trimIndent())
    }

    @Test
    fun testMathMode() {
        val sb = StringBuilder()
        document {
            math("2 \\cdot 3")
            newline()
        }.render(sb, "")
        val expected = """
            \begin{document}
             $2 \cdot 3$
            \end{document}
        """.trimIndent()
        assertEquals(expected, sb.toString().trimIndent())
    }

    @Test
    fun testEnumerateMode() {
        val sb = StringBuilder()
        document {
            enumerate {
                item { +"1" }
                item { +"2" }
                item { +"3" }
            }
            itemize {
                item { +"1" }
                item { +"2" }
                item { +"3" }
            }
        }.render(sb, "")
        val expected = """
           \begin{document}
            \begin{enumerate}
             \item
              1
             \item
              2
             \item
              3
            \end{enumerate}
            \begin{itemize}
             \item
              1
             \item
              2
             \item
              3
            \end{itemize}
           \end{document}
        """.trimIndent()
        assertEquals(expected, sb.toString().trimIndent())
    }
}