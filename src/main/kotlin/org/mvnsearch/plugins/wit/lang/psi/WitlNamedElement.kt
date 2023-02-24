package org.mvnsearch.plugins.wit.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.navigation.NavigationItem
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import org.mvnsearch.plugins.wit.lang.psi.WitElementFactory.createInterfaceItem
import javax.swing.Icon

interface WitNamedElement : PsiNameIdentifierOwner, NavigationItem {
    fun getKey(): String?

    fun getValue(): String?

    override fun getName(): String?

    override fun setName(name: String): PsiElement?

    override fun getNameIdentifier(): PsiElement?

    override fun getPresentation(): ItemPresentation?
}

abstract class WitNamedElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), WitNamedElement {
    private var _name: String? = null

    override fun getName(): String? {
        return this._name
    }

    override fun setName(name: String): PsiElement? {
        this._name = name
        return this
    }

    override fun getNameIdentifier(): PsiElement? {
        return this
    }

    override fun getPresentation(): ItemPresentation? {
        return null
    }

}

abstract class WitInterfaceItemElementImpl(node: ASTNode) : WitNamedElementImpl(node) {
    override fun getKey(): String? {
        val keyNode: ASTNode? = this.node.findChildByType(WitTypes.INTERFACE_NAME)
        return keyNode?.text
    }

    override fun getValue(): String? {
        val valueNode: ASTNode? = this.node.findChildByType(WitTypes.INTERFACE_ITEMS)
        return valueNode?.text
    }

    override fun getName(): String? {
        return getKey()
    }

    override fun setName(name: String): PsiElement? {
        val keyNode: ASTNode? = this.node.findChildByType(WitTypes.INTERFACE_NAME)
        if (keyNode != null) {
            val interfaceItem = createInterfaceItem(this.project, name)
            val newKeyNode = interfaceItem.interfaceName.node
            this.node.replaceChild(keyNode, newKeyNode)
        }
        return this
    }

    override fun getNameIdentifier(): PsiElement? {
        return this.node.findChildByType(WitTypes.INTERFACE_NAME)?.psi
    }

    override fun getPresentation(): ItemPresentation? {
        val presentationText = this.getKey()
        val element = this
        return object : ItemPresentation {
            override fun getPresentableText(): String? {
                return presentationText
            }

            override fun getLocationString(): String? {
                return element.containingFile?.name
            }

            override fun getIcon(unused: Boolean): Icon {
                return AllIcons.Nodes.Interface
            }
        }
    }
}
