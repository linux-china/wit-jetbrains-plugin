package org.mvnsearch.plugins.wit.lang.navigation

import com.intellij.navigation.DirectNavigationProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import org.mvnsearch.plugins.wit.lang.psi.WitFile
import org.mvnsearch.plugins.wit.lang.psi.WitInterfaceItem
import org.mvnsearch.plugins.wit.lang.psi.WitUseItem
import org.mvnsearch.plugins.wit.lang.psi.WitUseNamesTypeName

@Suppress("UnstableApiUsage")
class WitTypeNameNavigation : DirectNavigationProvider {
    override fun getNavigationElement(element: PsiElement): PsiElement? {
        if (element is WitUseNamesTypeName) {
            if (element.containingFile != null) {
                val typeName = element.text
                val witFile = element.containingFile as WitFile
                val witUseItem = element.parentOfType<WitUseItem>(false)
                witUseItem?.let {
                    it.usePath.let { usePath ->
                        usePath.usePathPkg?.let { usePathPkg ->
                            val pkgName = usePathPkg.usePathPkgName.text
                            val interfaceName = usePathPkg.usePathPkgInterfaceName?.text ?: ""
                            witFile.parent?.let { psiDirectory ->
                                val pkgFile = psiDirectory.findFile("$pkgName.wit")
                                if (pkgFile != null) {
                                    (pkgFile as WitFile).findInterfaceItem(interfaceName)?.let { witInterfaceItem ->
                                        return findInterfaceSubItem(typeName, witInterfaceItem)
                                    }
                                }
                            }
                        }
                        usePath.usePathSelf?.let { usePathSelf ->
                            val interfaceName = usePathSelf.usePathSelfInterfaceName.text
                            witFile.findInterfaceItem(interfaceName)?.let { witInterfaceItem ->
                                return findInterfaceSubItem(typeName, witInterfaceItem)
                            }
                        }
                    }

                }
            }
        }
        return null
    }

    fun findInterfaceSubItem(itemName: String, interfaceItem: WitInterfaceItem): PsiElement? {
        interfaceItem.interfaceItemsList.forEach {
            it.typedefItem?.let { typedefItem ->
                if (itemName == typedefItem.recordItem?.recordItemName?.text) {
                    return typedefItem.recordItem?.recordItemName
                }
                if (itemName == typedefItem.typeItem?.typeItemName?.text) {
                    return typedefItem.typeItem?.typeItemName
                }
                if (itemName == typedefItem.enumItem?.enumItemName?.text) {
                    return typedefItem.enumItem?.enumItemName
                }
                if (itemName == typedefItem.unionItem?.unionItemName?.text) {
                    return typedefItem.unionItem?.unionItemName
                }
                if (itemName == typedefItem.variantItem?.variantItemName?.text) {
                    return typedefItem.variantItem?.variantItemName
                }
                if (itemName == typedefItem.flagsItem?.flagsItemName?.text) {
                    return typedefItem.flagsItem?.flagsItemName
                }

            }
        }
        return null
    }
}