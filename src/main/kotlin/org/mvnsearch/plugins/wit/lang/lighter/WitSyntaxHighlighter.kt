package org.mvnsearch.plugins.wit.lang.lighter

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.mvnsearch.plugins.wit.lang.lexer.WitLexerAdapter
import org.mvnsearch.plugins.wit.lang.psi.WitTypes

class WitSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer {
        return WitLexerAdapter()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        val textAttributesKey = when (tokenType) {
            WitTypes.RESERVED_KEYWORD,
            WitTypes.TYPE_DECLARE_KEYWORD,
            WitTypes.DEFAULT_KEYWORD,
            WitTypes.INTERFACE_KEYWORD,
            WitTypes.WORLD_KEYWORD,
            WitTypes.FUNC_KEYWORD,
            WitTypes.REFER_KEYWORD,
            WitTypes.PATH_PREFIX_KEYWORD -> DefaultLanguageHighlighterColors.KEYWORD

            WitTypes.COMMENT -> DefaultLanguageHighlighterColors.LINE_COMMENT
            WitTypes.DOC_COMMENT -> DefaultLanguageHighlighterColors.DOC_COMMENT
            WitTypes.COMMENT_BLOCK -> DefaultLanguageHighlighterColors.BLOCK_COMMENT
            WitTypes.WIT_TYPE -> DefaultLanguageHighlighterColors.CLASS_NAME
            WitTypes.FUNC_NAME -> DefaultLanguageHighlighterColors.FUNCTION_DECLARATION
            WitTypes.FUNC_PARAM_NAME -> DefaultLanguageHighlighterColors.PARAMETER
            WitTypes.INTERFACE_NAME -> DefaultLanguageHighlighterColors.INTERFACE_NAME

            WitTypes.UNION_ITEM_NAME,
            WitTypes.ENUM_ITEM_NAME,
            WitTypes.WORLD_NAME,
            WitTypes.RECORD_ITEM_NAME -> DefaultLanguageHighlighterColors.INTERFACE_NAME

            WitTypes.COMMA -> DefaultLanguageHighlighterColors.COMMA
            WitTypes.LBRACE, WitTypes.RBRACE -> DefaultLanguageHighlighterColors.BRACES
            WitTypes.LPAREN, WitTypes.RPAREN -> DefaultLanguageHighlighterColors.PARENTHESES
            WitTypes.DOT, WitTypes.DOT -> DefaultLanguageHighlighterColors.DOT
            else -> {
                null
            }
        }
        return textAttributesKey?.let { arrayOf(it) } ?: emptyArray()
    }
}