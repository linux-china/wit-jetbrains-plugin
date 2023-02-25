package org.mvnsearch.plugins.wit.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiFile
import com.intellij.util.ProcessingContext
import org.mvnsearch.plugins.wit.lang.psi.WitFile
import org.mvnsearch.plugins.wit.lang.psi.WitInterfaceItem

class PkgCompletionContributor : CompletionContributor() {
    companion object {
        fun completeInterfaceSubTypes(interfaceItem: WitInterfaceItem, result: CompletionResultSet) {
            interfaceItem.interfaceItemsList.forEach { interfaceSubItem ->
                val typedefItem = interfaceSubItem.typedefItem
                if (typedefItem != null) {
                    typedefItem.typeItem?.let {
                        result.addElement(
                            LookupElementBuilder.create(it.typeItemName.text)
                                .withIcon(AllIcons.Nodes.Type)
                        )
                    }
                    typedefItem.recordItem?.let {
                        result.addElement(
                            LookupElementBuilder.create(it.recordItemName.text)
                                .withIcon(AllIcons.Nodes.Record)
                        )
                    }
                    typedefItem.unionItem?.let {
                        result.addElement(
                            LookupElementBuilder.create(it.unionItemName.text)
                        )
                    }
                    typedefItem.variantItem?.let {
                        result.addElement(
                            LookupElementBuilder.create(it.variantItemName.text)
                        )
                    }
                    typedefItem.flagsItem?.let {
                        result.addElement(
                            LookupElementBuilder.create(it.flagsItemName.text)
                        )
                    }
                    typedefItem.enumItem?.let {
                        result.addElement(
                            LookupElementBuilder.create(it.enumItemName.text)
                                .withIcon(AllIcons.Nodes.Enum)
                        )
                    }
                }
                interfaceSubItem.useItem?.let {
                    it.useNamesList?.useNamesTypeList?.forEach { useNamesType ->
                        val typeName = useNamesType.useNamesTypeAlias?.text ?: useNamesType.useNamesTypeName.text
                        result.addElement(
                            LookupElementBuilder.create(typeName).withIcon(AllIcons.Nodes.Type)
                        )
                    }
                }
            }
        }
    }

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
                    val witFile = parameters.originalFile as WitFile
                    if (prefixText.endsWith(" pkg.")) {
                        completePkg(witFile, result)
                    } else if (prefixText.contains(" pkg.") && prefixText.endsWith(".")) {
                        val pkgName = prefixText.substringAfter(" pkg.").substringBeforeLast(".")
                        completePkgInterfaceNames(witFile, pkgName, result)
                    } else if (prefixText.endsWith(" self.")) {
                        completeSelfInterfaceItems(witFile, result)
                    } else if (prefixText.contains(" self.") && prefixText.contains("{")) {
                        val interfaceName = prefixText.substringAfter(" self.").substringBefore(".")
                        witFile.findInterfaceItem(interfaceName)?.let {
                            completeInterfaceSubTypes(it, result)
                        }
                    } else if (prefixText.contains(" pkg.") && prefixText.contains("{")) {
                        val pkgName = prefixText.substringAfter(" pkg.").substringBefore(".")
                        witFile.parent?.findFile("$pkgName.wit")?.let { targetFile ->
                            val targetWitFile = targetFile as WitFile
                            val interfaceItem = if (prefixText.contains("${pkgName}.{")) { // default interface usage
                                targetWitFile.findDefaultInterfaceItem()
                            } else {
                                val interfaceName = prefixText.substringAfter("${pkgName}.").substringBefore(".")
                                targetWitFile.findInterfaceItem(interfaceName)
                            }
                            interfaceItem?.let { completeInterfaceSubTypes(it, result) }
                        }

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

    fun completePkgInterfaceNames(witFile: WitFile, pkgName: String, result: CompletionResultSet) {
        witFile.parent?.findFile("$pkgName.wit")?.let {
            (it as WitFile).getInterfaceItems().forEach { interfaceItem ->
                interfaceItem.interfaceName.text?.let { interfaceName ->
                    result.addElement(
                        LookupElementBuilder.create(interfaceName)
                            .withIcon(AllIcons.Nodes.Interface)
                    )
                }
            }
        }
    }

    fun completeSelfInterfaceItems(witFile: WitFile, result: CompletionResultSet) {
        witFile.getInterfaceItems().forEach {
            it.interfaceName.text?.let { interfaceName ->
                result.addElement(
                    LookupElementBuilder.create(interfaceName)
                        .withPresentableText(interfaceName)
                        .withIcon(AllIcons.Nodes.Interface)
                )
            }
        }
    }


}