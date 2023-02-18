package org.mvnsearch.plugins.wit.lang.insight

import com.intellij.lang.Commenter

class WitCommenter : Commenter {
    override fun getLineCommentPrefix(): String {
        return "// "
    }

    override fun getBlockCommentPrefix(): String {
        return "/* "
    }

    override fun getBlockCommentSuffix(): String {
        return " */"
    }

    override fun getCommentedBlockCommentPrefix(): String? {
        return null
    }

    override fun getCommentedBlockCommentSuffix(): String? {
        return null
    }
}