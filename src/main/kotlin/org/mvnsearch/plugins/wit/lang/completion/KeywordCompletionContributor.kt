package org.mvnsearch.plugins.wit.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.util.ProcessingContext
import org.mvnsearch.plugins.wit.lang.psi.WitInterfaceItem
import org.mvnsearch.plugins.wit.lang.psi.WitWorldItem

class KeywordCompletionContributor : CompletionContributor() {

    private val typeDeclareKeywords = listOf("record", "union", "enum", "variant", "flags", "resource", "type")

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
                    val prefixText = parameters.editor.document.getText(TextRange(lineOffset, caret.offset))
                    val trimmedPrefixText = prefixText.trim()
                    val parentElement = parameters.originalPosition?.parent
                    //val witFile = parameters.originalFile as WitFile
                    if (prefixText.isEmpty()) {
                        result.addElement(
                            LookupElementBuilder.create("interface ").withPresentableText("interface")
                                .withIcon(AllIcons.Nodes.Interface)
                        )
                        result.addElement(
                            LookupElementBuilder.create("world ").withPresentableText("world")
                                .withIcon(AllIcons.Nodes.Artifact)
                        )
                        result.addElement(LookupElementBuilder.create("default ").withPresentableText("default"))
                    } else if (trimmedPrefixText == "default") {
                        result.addElement(
                            LookupElementBuilder.create("interface ").withPresentableText("interface")
                                .withIcon(AllIcons.Nodes.Interface)
                        )
                        result.addElement(
                            LookupElementBuilder.create("world ").withPresentableText("world")
                                .withIcon(AllIcons.Nodes.Artifact)
                        )
                    } else if (trimmedPrefixText.isEmpty() && prefixText.startsWith(" ") && parentElement != null) {
                        if (parentElement is WitWorldItem) {
                            result.addElement(
                                LookupElementBuilder.create("import ").withPresentableText("import")
                                    .withIcon(AllIcons.Nodes.Type)
                            )
                            result.addElement(
                                LookupElementBuilder.create("export ").withPresentableText("export")
                                    .withIcon(AllIcons.Nodes.Type)
                            )
                            result.addElement(
                                LookupElementBuilder.create("use ").withPresentableText("use")
                                    .withIcon(AllIcons.Nodes.Type)
                            )
                        } else if (parentElement is WitInterfaceItem) {
                            result.addElement(
                                LookupElementBuilder.create("use ").withPresentableText("use")
                                    .withIcon(AllIcons.Nodes.Type)
                            )
                            typeDeclareKeywords.forEach {
                                result.addElement(
                                    LookupElementBuilder.create("$it ").withPresentableText(it)
                                        .withIcon(AllIcons.Nodes.Type)
                                )
                            }
                        }
                    }
                }
            }
        )
    }


}