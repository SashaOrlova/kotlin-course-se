package ru.hse.hw.dsl

@DslMarker
annotation class TexTagMarker


fun renderParams(params: List<String>) =
        if (!params.isEmpty()) "[${params.joinToString(", ")}]" else ""

@TexTagMarker
abstract class Element {
    abstract fun render(builder: StringBuilder, indent: String)

    override fun toString(): String {
        return buildString { render(this@buildString, "") }
    }
}

class TextElement(private val text: String) : Element() {
    override fun render(builder: StringBuilder, indent: String) {
        text.trimIndent().lines().forEach { builder.append("$indent$it\n") }
    }
}

@TexTagMarker
abstract class SimpleTag(private val name: String, private val argument: String, private vararg val params: String) : Element() {

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\$name[${params.toList().joinToString(", ")}]{$argument}\n")
    }
}

class UsePackage(argument: String, vararg params: String) : SimpleTag("usepackage", argument, *params)

class DocumentClass(argument: String, vararg params: String) : SimpleTag("documentclass", argument, *params)

class Math(private val content: String) : Element() {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\$$content\$")
    }
}

class Newline : Element() {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append('\n')
    }
}


@TexTagMarker
open class TagWithArgs(val name: String, vararg val params: String) : Element() {
    val children = arrayListOf<Element>()

    operator fun String.unaryPlus() {
        children += TextElement(this)
    }

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\begin{$name}${renderParams(params.toList())}\n")
        children.forEach { it.render(builder, "$indent ") }
        builder.append("$indent\\end{$name}\n")
    }

    protected fun <T : Element> initNewTag(tag: T, init: T.() -> Unit): T {
        tag.init()
        children += tag
        return tag
    }

    fun customTag(name: String, vararg params: String, init: TagWithArgs.() -> Unit) =
            initNewTag(TagWithArgs(name, *params), init)

    fun itemize(init: Itemize.() -> Unit) = initNewTag(Itemize(), init)
    fun enumerate(init: Enumerate.() -> Unit) = initNewTag(Enumerate(), init)
    fun math(content: String) = children.add(Math(content))
    fun newline() = children.add(Newline())
    fun frame(frameTitle: String, vararg params: String, init: Frame.() -> Unit) = initNewTag(Frame(frameTitle, *params), init)
    fun center(init: Center.() -> Unit) = initNewTag(Center(), init)
}

class Enumerate : TagWithArgs("enumerate") {
    fun item(init: Item.() -> Unit): Item = initNewTag(Item(), init)
}

class Itemize : TagWithArgs("itemize") {
    fun item(init: Item.() -> Unit): Item = initNewTag(Item(), init)
}

class Center : TagWithArgs("center")

class Frame(private val title: String, vararg params: String) : TagWithArgs("frame", *params) {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\begin{$name}${renderParams(params.toList())}\n")
        builder.append("$indent \\frametitle{$title}\n")
        children.forEach { it.render(builder, "$indent ") }
        builder.append("$indent\\end{$name}\n")
    }
}


class Item : TagWithArgs("item") {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\$name\n")
        children.forEach { it.render(builder, "$indent ") }
    }
}

class Document : TagWithArgs("document") {
    private val headerChildren = arrayListOf<Element>()

    override fun render(builder: StringBuilder, indent: String) {
        headerChildren.forEach { it.render(builder, indent) }
        super.render(builder, indent)
    }

    fun documentclass(argument: String, vararg params: String) =
            headerChildren.add(DocumentClass(argument, *params))

    fun usepackage(argument: String, vararg params: String) =
            headerChildren.add(UsePackage(argument, *params))
}


fun document(init: Document.() -> Unit): Document {
    val document = Document()
    document.init()
    return document
}