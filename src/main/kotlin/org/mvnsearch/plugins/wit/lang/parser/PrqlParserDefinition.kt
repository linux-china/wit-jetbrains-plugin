package org.mvnsearch.plugins.wit.lang.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import org.mvnsearch.plugins.wit.lang.WitLanguage
import org.mvnsearch.plugins.wit.lang.lexer.WitLexerAdapter
import org.mvnsearch.plugins.wit.lang.psi.WitFile
import org.mvnsearch.plugins.wit.lang.psi.WitTypes


class WitParserDefinition : ParserDefinition {
    override fun createLexer(project: Project?): Lexer = WitLexerAdapter()

    override fun getWhitespaceTokens(): TokenSet = WHITE_SPACES

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = LITERALS

    override fun createParser(project: Project?): PsiParser = WitParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun createFile(viewProvider: FileViewProvider): PsiFile = WitFile(viewProvider)

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode?, right: ASTNode?): ParserDefinition.SpaceRequirements =
            ParserDefinition.SpaceRequirements.MAY

    override fun createElement(node: ASTNode?): PsiElement = WitTypes.Factory.createElement(node)

    companion object {
        val WHITE_SPACES: TokenSet = TokenSet.create(TokenType.WHITE_SPACE)
        val COMMENTS: TokenSet = TokenSet.create(WitTypes.COMMENT, WitTypes.DOC_COMMENT)
        val LITERALS: TokenSet = TokenSet.create(WitTypes.STRING_LITERAL, WitTypes.CHAR_LITERAL)
        val FILE: IFileElementType = IFileElementType(WitLanguage)
    }
}