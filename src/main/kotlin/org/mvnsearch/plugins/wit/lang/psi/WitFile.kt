package org.mvnsearch.plugins.wit.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import org.mvnsearch.plugins.wit.lang.WitLanguage

class WitFile(viewProvider: FileViewProvider?) : PsiFileBase(viewProvider!!, WitLanguage) {
    override fun getFileType() = WitFileType
    override fun toString() = "WIT File"

    fun getInterfaceItems(): Array<WitInterfaceItem> {
        return findChildrenByClass(WitInterfaceItem::class.java)
    }

    fun findDefaultInterfaceItem(): WitInterfaceItem? {
        return findChildrenByClass(WitInterfaceItem::class.java).find {
            it.text.startsWith("default ")
        }
    }

    fun findInterfaceItem(interfaceName: String): WitInterfaceItem? {
        if (interfaceName.isEmpty()) {
            return findDefaultInterfaceItem()
        }
        return findChildrenByClass(WitInterfaceItem::class.java).find {
            it.interfaceName.text == interfaceName
        }
    }
}

