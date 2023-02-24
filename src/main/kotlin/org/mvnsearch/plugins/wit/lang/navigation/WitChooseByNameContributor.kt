package org.mvnsearch.plugins.wit.lang.navigation

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import org.mvnsearch.plugins.wit.lang.psi.WitFile
import org.mvnsearch.plugins.wit.lang.psi.WitFileType
import org.mvnsearch.plugins.wit.lang.psi.WitInterfaceItem


class WitChooseByNameContributor : ChooseByNameContributor {
    override fun getNames(project: Project, includeNonProjectItems: Boolean): Array<String> {
        val names = mutableListOf<String>()
        findWitFiles(project).forEach {
            it.getInterfaceItems().forEach { item ->
                names.add(item.interfaceName.text)
            }
        }
        return names.toTypedArray()
    }

    override fun getItemsByName(
        name: String,
        pattern: String?,
        project: Project,
        includeNonProjectItems: Boolean
    ): Array<NavigationItem> {
        return findInterfaces(project, name).toTypedArray()
    }

    private fun findWitFiles(project: Project): List<WitFile> {
        val result = mutableListOf<WitFile>()
        FileTypeIndex.getFiles(WitFileType, GlobalSearchScope.allScope(project)).forEach {
            val witFile: WitFile? = PsiManager.getInstance(project).findFile(it) as WitFile?
            if (witFile != null) {
                result.add(witFile)
            }
        }
        return result
    }

    private fun findInterfaces(project: Project, interfaceName: String): List<WitInterfaceItem> {
        val result = mutableListOf<WitInterfaceItem>()
        FileTypeIndex.getFiles(WitFileType, GlobalSearchScope.allScope(project)).forEach {
            val witFile = PsiManager.getInstance(project).findFile(it) as WitFile
            witFile.getInterfaceItems().forEach { item ->
                if (item.interfaceName.text == interfaceName) {
                    result.add(item)
                }
            }
        }
        return result
    }
}

