package org.mvnsearch.plugins.wit.lang.navigation

import com.intellij.navigation.DirectNavigationProvider
import com.intellij.psi.PsiElement
import org.mvnsearch.plugins.wit.lang.psi.WitFile
import org.mvnsearch.plugins.wit.lang.psi.WitUsePathSelfInterfaceName

@Suppress("UnstableApiUsage")
class WitUsePathSelfNavigation : DirectNavigationProvider {
    override fun getNavigationElement(element: PsiElement): PsiElement? {
        if (element is WitUsePathSelfInterfaceName) {
            if (element.containingFile != null) {
                val interfaceName = element.text
                val witFile = element.containingFile as WitFile
                witFile.getInterfaceItems().forEach {
                    if (interfaceName == it.interfaceName?.text) {
                        return it.interfaceName
                    }
                }
            }
        }
        return null
    }
}