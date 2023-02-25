package org.mvnsearch.plugins.wit.lang.format

import com.intellij.formatting.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import org.mvnsearch.plugins.wit.lang.WitLanguage
import org.mvnsearch.plugins.wit.lang.psi.WitTypes


class WitFormattingModelBuilder : FormattingModelBuilder {
    private fun createSpaceBuilder(settings: CodeStyleSettings): SpacingBuilder {
        return SpacingBuilder(settings, WitLanguage)
            .around(WitTypes.EQ).spaceIf(true)
            .before(WitTypes.DEFAULT_KEYWORD).none()
            .before(WitTypes.REFER_KEYWORD).spaces(2)
    }

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val codeStyleSettings = formattingContext.codeStyleSettings
        return FormattingModelProvider
            .createFormattingModelForPsiFile(
                formattingContext.containingFile,
                WitBlock(
                    formattingContext.node,
                    Wrap.createWrap(WrapType.NONE, false),
                    Alignment.createAlignment(),
                    createSpaceBuilder(codeStyleSettings)
                ),
                codeStyleSettings
            )
    }

}