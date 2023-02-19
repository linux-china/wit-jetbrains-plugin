package org.mvnsearch.plugins.wit.lang.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory


object WitElementFactory {

    fun createFile(project: Project, text: String) =
        PsiFileFactory.getInstance(project).createFileFromText("demo.wit", WitFileType, text) as WitFile


    fun createInterfaceItem(project: Project, name: String): WitInterfaceItem {
        return createFile(project, "interface $name { }").firstChild as WitInterfaceItem
    }

}