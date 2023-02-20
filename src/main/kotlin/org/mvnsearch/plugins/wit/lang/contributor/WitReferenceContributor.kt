package org.mvnsearch.plugins.wit.lang.contributor

import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.mvnsearch.plugins.wit.lang.psi.WitUsePath
import org.mvnsearch.plugins.wit.lang.psi.WitUsePathPart


class WitReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    if (element is WitUsePathPart) {
                        val pathPart: WitUsePathPart = element as WitUsePathPart
                        val witUsePath = pathPart.parentOfType<WitUsePath>()
                        if (witUsePath != null && witUsePath.text.startsWith("pkg.")) {
                            val pathText = witUsePath.text
                            var pkgName = pathText.substring(4)
                            if (pkgName.contains(".")) {
                                pkgName = pkgName.substring(0, pkgName.lastIndexOf("."))
                            }
                            if (element.containingFile != null) {
                                element.containingFile.parent?.let {
                                    val pkgFile = it.findFile("$pkgName.wit")
                                    if (pkgFile != null) {
                                        return arrayOf(pkgFile.reference!!)
                                    }
                                }
                            }
                        }
                    }
                    return PsiReference.EMPTY_ARRAY
                }
            })
    }
}