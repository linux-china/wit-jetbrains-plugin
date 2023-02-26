package org.mvnsearch.plugins.wit.ide.typing

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiFile
import org.mvnsearch.plugins.wit.lang.psi.WitFile

class WitEnterHandler : EnterHandlerDelegateAdapter() {
    override fun preprocessEnter(
        file: PsiFile,
        editor: Editor,
        caretOffset: Ref<Int>,
        caretAdvance: Ref<Int>,
        dataContext: DataContext,
        originalHandler: EditorActionHandler?
    ): EnterHandlerDelegate.Result {
        val superProcessor = super.postProcessEnter(file, editor, dataContext)
        if (file !is WitFile) {
            return superProcessor
        }
        return EnterHandlerDelegate.Result.DefaultSkipIndent
    }

}