package org.mvnsearch.plugins.wit.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiFile
import com.intellij.util.ProcessingContext
import org.mvnsearch.plugins.wit.lang.psi.WitFile

class PkgCompletionContributor : CompletionContributor() {
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
                    if (prefixText.endsWith(" pkg.")) {
                        completePkg(parameters.originalFile as WitFile, result)
                    } else if (prefixText.endsWith(" self.")) {
                        completeSelfInterfaceItems(parameters.originalFile as WitFile, result)
                    }
                }
            }
        )
    }

    fun completePkg(witFile: WitFile, result: CompletionResultSet) {
        val witFileName = witFile.name
        witFile.parent?.children
            ?.filter { it is PsiFile && it.name.endsWith(".wit") && it.name != witFileName }
            ?.map { it as WitFile }
            ?.forEach {
                val pkgName = it.name.substringBeforeLast(".wit")
                result.addElement(
                    LookupElementBuilder.create("$pkgName.")
                        .withPresentableText(pkgName)
                        .withIcon(AllIcons.Nodes.Package)
                )
            }
    }

    fun completeSelfInterfaceItems(witFile: WitFile, result: CompletionResultSet) {
        witFile.getInterfaceItems().forEach {
            it.interfaceName?.text?.let { interfaceName ->
                result.addElement(
                    LookupElementBuilder.create(interfaceName)
                        .withPresentableText(interfaceName)
                        .withIcon(AllIcons.Nodes.Interface)
                )
            }
        }
    }
}