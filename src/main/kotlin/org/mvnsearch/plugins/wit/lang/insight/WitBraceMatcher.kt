package org.mvnsearch.plugins.wit.lang.insight

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import org.mvnsearch.plugins.wit.lang.psi.WitTypes


class WitBraceMatcher : PairedBraceMatcher {
    private val pairs = arrayOf(
        BracePair(WitTypes.LBRACE, WitTypes.RBRACE, true),
        BracePair(WitTypes.LPAREN, WitTypes.RPAREN, true),
        BracePair(WitTypes.LT, WitTypes.GT, true)
    )

    override fun getPairs(): Array<BracePair> {
        return pairs
    }

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
        return true
    }

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int {
        return openingBraceOffset
    }
}