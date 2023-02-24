package org.mvnsearch.plugins.wit.lang.contributor

import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import org.mvnsearch.plugins.wit.lang.psi.WitUsePathPkgName


class WitReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(WitUsePathPkgName::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    if (element is WitUsePathPkgName) {
                        val pkgName = element.text
                        if (element.containingFile != null) {
                            element.containingFile.parent?.let {
                                val pkgFile = it.findFile("$pkgName.wit")
                                if (pkgFile != null) {
                                    return arrayOf(pkgFile.reference!!)
                                }
                            }
                        }
                    }
                    return PsiReference.EMPTY_ARRAY
                }
            })
    }
}