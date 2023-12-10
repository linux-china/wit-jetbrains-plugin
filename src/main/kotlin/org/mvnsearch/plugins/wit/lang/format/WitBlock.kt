package org.mvnsearch.plugins.wit.lang.format

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import org.mvnsearch.plugins.wit.lang.psi.*


class WitBlock(node: ASTNode, wrap: Wrap?, alignment: Alignment?, val spacingBuilder: SpacingBuilder) :
    AbstractBlock(node, wrap, alignment) {
    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return spacingBuilder.getSpacing(this, child1, child2)
    }

    override fun isLeaf(): Boolean {
        return myNode.firstChildNode == null;
    }

    override fun getIndent(): Indent? {
        val psi = myNode.psi
        val parent = psi.parent
        if (myNode.elementType === WitTypes.REFER_KEYWORD || myNode.elementType === WitTypes.TYPE_DECLARE_KEYWORD) {
            return Indent.getSpaceIndent(2)
        } else if (myNode.elementType === WitTypes.FUNC_NAME) {
            return Indent.getSpaceIndent(2)
        } else if (myNode.elementType === WitTypes.RECORD_FIELD_NAME
            || myNode.elementType === WitTypes.UNION_CASE_TYPE
            || myNode.elementType === WitTypes.VARIANT_CASE_NAME
            || myNode.elementType === WitTypes.VARIANT_CASE_NAME
            || myNode.elementType === WitTypes.FLAGS_FIELD_NAME
            || myNode.elementType === WitTypes.ENUM_CASE_NAME
        ) {
            return Indent.getSpaceIndent(4)
        } else if (myNode.elementType === WitTypes.RESOURCE_FUNC_ITEM) {
            return Indent.getSpaceIndent(2)
        } else if (myNode.elementType === WitTypes.COMMENT
            || myNode.elementType === WitTypes.DOC_COMMENT
        ) {
            return if (parent is WitFile) {
                Indent.getNoneIndent()
            } else {
                Indent.getSpaceIndent(2, true)
            }
        } else if (myNode.elementType === WitTypes.COMMENT_BLOCK
            || myNode.elementType === WitTypes.BLOCK_COMMENT_START
            || myNode.elementType === WitTypes.BLOCK_COMMENT_END
        ) {
            return if (parent is WitFile || parent is WitCommentBlock) {
                Indent.getNoneIndent()
            } else {
                Indent.getSpaceIndent(2, true)
            }
        } else if (myNode.elementType === WitTypes.RBRACE) {
            return if (parent is WitWorldItem) {
                Indent.getNoneIndent()
            } else if (parent is WitInterfaceItem || parent is WitWorldInterfaceType) {
                Indent.getNoneIndent()
            } else {
                Indent.getSpaceIndent(2)
            }
        }
        return Indent.getNoneIndent()
    }

    override fun buildChildren(): MutableList<Block> {
        val blocks: MutableList<Block> = ArrayList()
        var child = myNode.firstChildNode
        while (child != null) {
            if (child.elementType !== TokenType.WHITE_SPACE) {
                val block: Block = WitBlock(
                    child, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment(),
                    spacingBuilder
                )
                blocks.add(block)
            }
            child = child.treeNext
        }
        return blocks
    }
}