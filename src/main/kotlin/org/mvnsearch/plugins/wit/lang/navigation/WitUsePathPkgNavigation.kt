package org.mvnsearch.plugins.wit.lang.navigation

import com.intellij.navigation.DirectNavigationProvider
import com.intellij.psi.PsiElement
import org.mvnsearch.plugins.wit.lang.psi.WitFile
import org.mvnsearch.plugins.wit.lang.psi.WitUsePathPkg
import org.mvnsearch.plugins.wit.lang.psi.WitUsePathPkgInterfaceName
import org.mvnsearch.plugins.wit.lang.psi.WitUsePathPkgName

@Suppress("UnstableApiUsage")
class WitUsePathPkgNavigation : DirectNavigationProvider {
    override fun getNavigationElement(element: PsiElement): PsiElement? {
        if (element is WitUsePathPkgName) {
            val pkgName = element.text
            if (element.containingFile != null) {
                element.containingFile.parent?.let {
                    val pkgFile = it.findFile("$pkgName.wit")
                    if (pkgFile != null) {
                        return pkgFile
                    }
                }
            }
        } else if (element is WitUsePathPkgInterfaceName) {
            val interfaceName = element.text
            val pkgName = (element.parent as WitUsePathPkg).usePathPkgName.text
            if (element.containingFile != null) {
                element.containingFile.parent?.let { psiDirectory ->
                    val pkgFile = psiDirectory.findFile("$pkgName.wit")
                    if (pkgFile != null) {
                        (pkgFile as WitFile).getInterfaceItems().forEach { witInterfaceItem ->
                            if (interfaceName == witInterfaceItem.interfaceName.text) {
                                return witInterfaceItem.interfaceName
                            }
                        }
                    }
                }
            }
        }
        return null
    }
}