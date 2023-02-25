package org.mvnsearch.plugins.wit.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.util.ProcessingContext
import org.mvnsearch.plugins.wit.lang.psi.WitFile

class TypeNameCompletionContributor : CompletionContributor() {
    private val builtInTypeNames = listOf(
        "u8",
        "u16",
        "u32",
        "u64",
        "s8",
        "s16",
        "s32",
        "s64",
        "float32",
        "float64",
        "char",
        "bool",
        "string",
        "tuple",
        "list",
        "option",
        "result",
        "handle",
        "id"
    )

    init {
        extend(
            CompletionType.BASIC,
            psiElement(),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val caret = parameters.editor.caretModel.currentCaret
                    val lineOffset = caret.visualLineStart
                    val prefixText = parameters.editor.document.getText(TextRange(lineOffset, caret.offset)).trim()
                    //val witFile = parameters.originalFile as WitFile
                    if (prefixText.contains("type ") && prefixText.endsWith("=")) {
                        completeTypeNames(result)
                    } else if (!(prefixText.startsWith("import")
                                || prefixText.startsWith("export")
                                || prefixText.startsWith("use"))
                    ) {
                        if (prefixText.endsWith(":")
                            || prefixText.endsWith("->")
                            || prefixText.endsWith("<")
                        )
                            completeTypeNames(result)
                    }
                }
            }
        )
    }

    fun completeTypeNames(result: CompletionResultSet) {
        builtInTypeNames.forEach { builtInTypeName ->
            result.addElement(
                LookupElementBuilder.create(builtInTypeName)
                    .withIcon(AllIcons.Nodes.Type)
            )
        }
    }

}