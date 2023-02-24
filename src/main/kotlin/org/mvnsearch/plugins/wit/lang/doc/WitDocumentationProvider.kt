package org.mvnsearch.plugins.wit.lang.doc

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.elementType
import org.mvnsearch.plugins.wit.lang.psi.WitInterfaceItem
import org.mvnsearch.plugins.wit.lang.psi.WitInterfaceName
import org.mvnsearch.plugins.wit.lang.psi.WitTypes
import java.util.*


class WitDocumentationProvider : AbstractDocumentationProvider() {

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        if (element is WitInterfaceItem) {
            return findDocumentationComment(element)
        }
        return null
    }

    override fun getCustomDocumentationElement(
        editor: Editor,
        file: PsiFile,
        contextElement: PsiElement?,
        targetOffset: Int
    ): PsiElement? {
        if (contextElement?.parent is WitInterfaceName) {
            return contextElement.parent.parent
            //return contextElement
        }
        return super.getCustomDocumentationElement(editor, file, contextElement, targetOffset)
    }

    override fun generateHoverDoc(element: PsiElement, originalElement: PsiElement?): String? {
        return generateDoc(element, originalElement)
    }

    /**
     * Attempts to collect any comment elements above the Simple key/value pair.
     */
    private fun findDocumentationComment(property: PsiElement): String {
        val result: MutableList<String> = LinkedList()
        var element: PsiElement? = property.prevSibling
        while (element is PsiComment || element is PsiWhiteSpace) {
            if (element is PsiComment && element.elementType == WitTypes.DOC_COMMENT) {
                val commentText = element.getText().replaceFirst("/// ", "")
                result.add(commentText)
            }
            element = element.prevSibling
        }
        return result.joinToString("\n")
    }

}