package org.mvnsearch.plugins.wit.lang.insight

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.FoldingGroup
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.mvnsearch.plugins.wit.lang.psi.WitInterfaceItem


class WitFoldingBuilder : FoldingBuilderEx(), DumbAware {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        // Initialize the group of folding regions that will expand/collapse together.
        val group = FoldingGroup.newGroup("wit")
        // Initialize the list of folding regions
        val descriptors: MutableList<FoldingDescriptor> = ArrayList()
        // Get a collection of the literal expressions in the document below root
        val interfaceItems: Collection<WitInterfaceItem> = PsiTreeUtil.findChildrenOfType(
            root,
            WitInterfaceItem::class.java
        )
        // Evaluate the collection
        for (interfaceItem in interfaceItems) {
            // Add a folding descriptor for the literal expression at this node.
            descriptors.add(
                FoldingDescriptor(
                    interfaceItem.node,
                    TextRange(
                        interfaceItem.textRange.startOffset,
                        interfaceItem.textRange.endOffset
                    ),
                    group,
                    "interface " + (interfaceItem.interfaceName?.text ?: "unknown") + "..."
                )
            )
        }
        return descriptors.toTypedArray<FoldingDescriptor>()

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