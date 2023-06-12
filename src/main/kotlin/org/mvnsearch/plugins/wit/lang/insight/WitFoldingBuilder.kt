package org.mvnsearch.plugins.wit.lang.insight

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.mvnsearch.plugins.wit.lang.psi.*


class WitFoldingBuilder : FoldingBuilderEx(), DumbAware {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors: MutableList<FoldingDescriptor> = ArrayList()
        foldInterfaceItems(root, descriptors)
        foldWorldItems(root, descriptors)
        return descriptors.toTypedArray<FoldingDescriptor>()
    }

    private fun addElementFolder(
        element: PsiElement,
        descriptors: MutableList<FoldingDescriptor>,
        placeHolderText: String
    ) {
        descriptors.add(
            FoldingDescriptor(
                element.node,
                TextRange(
                    element.textRange.startOffset,
                    element.textRange.endOffset
                ),
                null,
                placeHolderText
            )
        )
    }

    private fun foldInterfaceItems(root: PsiElement, descriptors: MutableList<FoldingDescriptor>) {
        val interfaceItems: Collection<WitInterfaceItem> = PsiTreeUtil.findChildrenOfType(
            root, WitInterfaceItem::class.java
        )
        interfaceItems.forEach { interfaceItem ->
            var interfacePlaceHolderText = "interface " + (interfaceItem.interfaceName?.text ?: "unknown") + "..."
            if (interfaceItem.firstChild.text == "default") {
                interfacePlaceHolderText = "default $interfacePlaceHolderText"
            }
            addElementFolder(interfaceItem, descriptors, interfacePlaceHolderText)
            // fold record items
            PsiTreeUtil.findChildrenOfType(interfaceItem, WitRecordItem::class.java).forEach { recordItem ->
                val recordPlaceHolderText = "record " + (recordItem.recordItemName?.text ?: "unknown") + "..."
                addElementFolder(recordItem, descriptors, recordPlaceHolderText)
            }
            // fold variant items
            PsiTreeUtil.findChildrenOfType(interfaceItem, WitVariantItem::class.java).forEach { variantItem ->
                val recordPlaceHolderText = "variant " + (variantItem.variantItemName?.text ?: "unknown") + "..."
                addElementFolder(variantItem, descriptors, recordPlaceHolderText)
            }
            // fold enum items
            PsiTreeUtil.findChildrenOfType(interfaceItem, WitEnumItem::class.java).forEach { enumItem ->
                val recordPlaceHolderText = "enum " + (enumItem.enumItemName?.text ?: "unknown") + "..."
                addElementFolder(enumItem, descriptors, recordPlaceHolderText)
            }
            // fold union items
            PsiTreeUtil.findChildrenOfType(interfaceItem, WitUnionItem::class.java).forEach { unionItem ->
                val recordPlaceHolderText = "union " + (unionItem.unionItemName?.text ?: "unknown") + "..."
                addElementFolder(unionItem, descriptors, recordPlaceHolderText)
            }
            // fold flags items
            PsiTreeUtil.findChildrenOfType(interfaceItem, WitFlagsItem::class.java).forEach { flagsItem ->
                val recordPlaceHolderText = "flags " + (flagsItem.flagsItemName?.text ?: "unknown") + "..."
                addElementFolder(flagsItem, descriptors, recordPlaceHolderText)
            }
        }
    }


    private fun foldWorldItems(root: PsiElement, descriptors: MutableList<FoldingDescriptor>) {
        PsiTreeUtil.findChildrenOfType(root, WitWorldItem::class.java).forEach { worldItem ->
            var placeHolderText = "world " + (worldItem.worldName?.text ?: "unknown") + "..."
            if (worldItem.firstChild.text == "default") {
                placeHolderText = "default $placeHolderText"
            }
            addElementFolder(worldItem, descriptors, placeHolderText)
            // fold world inline interface item in import and export
            PsiTreeUtil.findChildrenOfType(worldItem, WitWorldImportItem::class.java).forEach { worldImportItem ->
                worldImportItem.worldExternType.let { worldExternType ->
                    worldExternType?.worldInterfaceType?.worldInterfaceTypeInline?.let {
                        addElementFolder(
                            worldImportItem,
                            descriptors,
                            "import ${worldImportItem.worldImportItemName.text} with interface ..."
                        )
                    }
                }
            }
            PsiTreeUtil.findChildrenOfType(worldItem, WitWorldExportItem::class.java).forEach { worldExportItem ->
                worldExportItem.worldExternType.let { worldExternType ->
                    worldExternType?.worldInterfaceType?.worldInterfaceTypeInline?.let {
                        addElementFolder(
                            worldExportItem,
                            descriptors,
                            "export ${worldExportItem.worldExportItemName.text} with interface ..."
                        )
                    }
                }
            }
        }
    }

    override fun getPlaceholderText(node: ASTNode): String {
        val retTxt = "..."
        if (node.psi is WitInterfaceItem) {
            val witInterfaceItem = node.psi as WitInterfaceItem
            return "interface " + witInterfaceItem.interfaceName + "..."
        }
        return retTxt
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return false
    }
}