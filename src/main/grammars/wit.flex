package org.mvnsearch.plugins.wit.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.mvnsearch.plugins.wit.lang.psi.WitTypes;
import static org.mvnsearch.plugins.wit.lang.psi.WitTypes.*;
import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import com.intellij.psi.TokenType;

%%


%{
  public WitLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class WitLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

///////////////////////////////////////////////////////////////////////////////////////////////////
// Whitespaces
///////////////////////////////////////////////////////////////////////////////////////////////////

EOL_WS           = \n | \r | \r\n
LINE_WS          = [\ \t]
WHITE_SPACE_CHAR = {EOL_WS} | {LINE_WS}
WHITE_SPACE      = {WHITE_SPACE_CHAR}+

///////////////////////////////////////////////////////////////////////////////////////////////////
// Identifier
///////////////////////////////////////////////////////////////////////////////////////////////////

IDENTIFIER = [a-zA-Z][a-zA-Z0-9-]*

///////////////////////////////////////////////////////////////////////////////////////////////////
// comment
///////////////////////////////////////////////////////////////////////////////////////////////////

COMMENT=("//")[^\n]*
DOC_COMMENT=("///")[^\n]*
BLOCK_COMMENT_START="/*"
BLOCK_COMMENT_END="*/"

///////////////////////////////////////////////////////////////////////////////////////////////////
// Literals
///////////////////////////////////////////////////////////////////////////////////////////////////

INTEGER_LITERAL=[\d][\d_]*
DOUBLE_LITERAL=([\d][\d_]*)(\.)([\d][\d_]*)

CHAR_LITERAL   = ([^\r\n\ \t]+)
STRING_LITERAL = (\"[^\\\"\r\n]*\")

%state IN_BLOCK_COMMENT

%%

<IN_BLOCK_COMMENT> {
  "*/"                           { yybegin(YYINITIAL); return BLOCK_COMMENT_END; }
  {CHAR_LITERAL}                 { yybegin(IN_BLOCK_COMMENT); return CHAR_LITERAL; }
  {WHITE_SPACE}                  { yybegin(IN_BLOCK_COMMENT); return WHITE_SPACE; }
}

<YYINITIAL> {
  "{"                             { return LBRACE; }
  "}"                             { return RBRACE; }
  "["                             { return LBRACK; }
  "]"                             { return RBRACK; }
  "("                             { return LPAREN; }
  ")"                             { return RPAREN; }
  ":"                             { return COLON; }
  ","                             { return COMMA; }
  ";"                             { return SEMICOLON; }
  "."                             { return DOT; }
  "="                             { return EQ; }
  "+"                             { return PLUS; }
  "-"                             { return MINUS; }
  "*"                             { return MUL; }
  "/"                             { return DIV; }
  "%"                             { return REM; }
  "|"                             { return OR; }
  "<"                             { return LT; }
  ">"                             { return GT; }
  "->"                            { return ARROW; }
  /* Builtin types */
  "u8" | "u16" | "u32" | "u64" | "s8" | "s16" |"s32" |"s64"  | "float32" | "float64" | "char" | "bool" | "string"  | "tuple" | "list"  | "option" | "result" | "handle" | "id"
                                 { return BUILTIN_TYPE; }
  /* type decleare */
  "type" | "resource" | "record" | "enum" | "flags" | "variant" | "union"       { return TYPE_DECLARE_KEYWORD; }
  "use" | "import" | "export" | "include"    { return REFER_KEYWORD; }
  /* path keywords */
  "pkg"|"self"                   { return PATH_PREFIX_KEYWORD; }
  "default"                      { return DEFAULT_KEYWORD; }
  "func"                         { return FUNC_KEYWORD; }
  "interface"                    { return INTERFACE_KEYWORD; }
  "world"                        { return  WORLD_KEYWORD; }
  "package"                      { return  PACKAGE_KEYWORD; }
  /* other keywords */
  "as" | "static" | "future" | "stream"  { return RESERVED_KEYWORD; }
  /* Comment */
  {DOC_COMMENT}                  { return DOC_COMMENT; }
  {COMMENT}                      { return COMMENT; }
  {BLOCK_COMMENT_START}          { yybegin(IN_BLOCK_COMMENT); return BLOCK_COMMENT_START; }

  /* LITERALS */
  {INTEGER_LITERAL}                   { return INTEGER_LITERAL; }
  {DOUBLE_LITERAL}                   { return  DOUBLE_LITERAL; }
  {STRING_LITERAL}                { return STRING_LITERAL; }

  {IDENTIFIER}                   { return IDENTIFIER; }
  {WHITE_SPACE}                   { return WHITE_SPACE; }
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Catch All
///////////////////////////////////////////////////////////////////////////////////////////////////

[^] { return BAD_CHARACTER; }