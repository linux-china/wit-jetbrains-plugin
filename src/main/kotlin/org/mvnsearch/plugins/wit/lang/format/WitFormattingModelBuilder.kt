package org.mvnsearch.plugins.wit.lang.format

import com.intellij.formatting.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import org.mvnsearch.plugins.wit.lang.WitLanguage
import org.mvnsearch.plugins.wit.lang.psi.WitTypes


class WitFormattingModelBuilder : FormattingModelBuilder {
    private fun SpacingBuilder.RuleBuilder.strictSpaces(needSpace: Boolean) =
        spacing(needSpace.toInt(), needSpace.toInt(), 0, false, 0)

    private fun Boolean.toInt() = if (this) 1 else 0

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val settings = formattingContext.codeStyleSettings
        val spacingBuilder = createSpacingBuilder(settings)
        val root = WitBlock(
            formattingContext.node, Wrap.createWrap(WrapType.NONE, false),
            Alignment.createAlignment(), spacingBuilder
        )
        return FormattingModelProvider.createFormattingModelForPsiFile(formattingContext.containingFile, root, settings)
    }


    private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
        return SpacingBuilder(settings, WitLanguage)
            .after(WitTypes.COLON).strictSpaces(true)
            .before(WitTypes.ARROW).spaces(1)
            .after(WitTypes.ARROW).strictSpaces(true)
    }

}