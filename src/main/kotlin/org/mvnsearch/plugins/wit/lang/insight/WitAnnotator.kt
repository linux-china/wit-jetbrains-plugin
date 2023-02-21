package org.mvnsearch.plugins.wit.lang.insight

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import org.mvnsearch.plugins.wit.lang.psi.WitTypes

class WitAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        /*when (element.elementType) {

            WitTypes.USE_PATH_PART -> {
                val pathName = element.text
                if (pathName.equals("pkg") || pathName.equals("self")) {
                    holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(element.textRange)
                        .textAttributes(DefaultLanguageHighlighterColors.KEYWORD).create()
                }
            }
        }*/
    }

}