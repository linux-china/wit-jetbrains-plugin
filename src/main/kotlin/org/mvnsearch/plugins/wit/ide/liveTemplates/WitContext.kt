package org.mvnsearch.plugins.wit.ide.liveTemplates

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
import org.mvnsearch.plugins.wit.lang.psi.WitFile

class WitContext : TemplateContextType("WIT") {

    override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
        return templateActionContext.file is WitFile
    }
}