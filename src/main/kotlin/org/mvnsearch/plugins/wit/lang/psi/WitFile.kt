package org.mvnsearch.plugins.wit.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import org.mvnsearch.plugins.wit.lang.WitLanguage

class WitFile(viewProvider: FileViewProvider?) : PsiFileBase(viewProvider!!, WitLanguage) {
    override fun getFileType() = WitFileType
    override fun toString() = "WIT File"

}

