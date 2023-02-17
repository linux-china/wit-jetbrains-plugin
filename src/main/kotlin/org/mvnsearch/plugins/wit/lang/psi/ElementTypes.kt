package org.mvnsearch.plugins.wit.lang.psi

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.psi.tree.IElementType
import org.mvnsearch.plugins.wit.ide.icons.WitIcons
import org.mvnsearch.plugins.wit.lang.WitLanguage
import javax.swing.Icon

class WitTokenType(debugName: String) : IElementType(debugName, WitLanguage) {
    override fun toString(): String = "WitToken." + super.toString()
}

class WitElementType(debugName: String) : IElementType(debugName, WitLanguage)

object WitFileType : LanguageFileType(WitLanguage) {
    override fun getName(): String = "wit"
    override fun getDescription(): String = "WIT file"
    override fun getDefaultExtension(): String = "wit"

    override fun getIcon(): Icon = WitIcons.WASM
}