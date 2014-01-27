package lexer;

import java.util.HashMap;

public class Token
{
	// Lab 1, exercise 1: read the MiniJava specification
	// carefully, and answer these two questions:
	// 1. whether or not one should add other token kinds?
	// 2. which tokens come with an extra "lexeme", and
	// which don't?
	// It's highly recommended that these token names are
	// alphabetically ordered, if you add new ones.

	// public static String rsvword[] =
	// { "boolean", "class", "else", "extends", "false", "if", "int", "length",
	// "main", "new", "out", "println", "public", "return", "static",
	// "System", "this", "true", "void", "while" };

	public static HashMap<String, Kind> keyword = new HashMap<String, Kind>()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("boolean", Kind.TOKEN_BOOLEAN);
				put("class", Kind.TOKEN_CLASS);
				put("else", Kind.TOKEN_ELSE);
				put("extends", Kind.TOKEN_EXTENDS);
				put("false", Kind.TOKEN_FALSE);
				put("if", Kind.TOKEN_IF);
				put("int", Kind.TOKEN_INT);
				put("length", Kind.TOKEN_LENGTH);
				put("main", Kind.TOKEN_MAIN);
				put("new", Kind.TOKEN_NEW);
				put("out", Kind.TOKEN_OUT);
				put("println", Kind.TOKEN_PRINTLN);
				put("public", Kind.TOKEN_PUBLIC);
				put("return", Kind.TOKEN_RETURN);
				put("static", Kind.TOKEN_STATIC);
				put("System", Kind.TOKEN_SYSTEM);
				put("String", Kind.TOKEN_STRING);
				put("this", Kind.TOKEN_THIS);
				put("true", Kind.TOKEN_TRUE);
				put("void", Kind.TOKEN_VOID);
				put("while", Kind.TOKEN_WHILE);
			}
		};

	public enum Kind
	{
		TOKEN_ADD, // "+"
		TOKEN_AND, // "&&"
		TOKEN_ASSIGN, // "="
		TOKEN_BOOLEAN, // "boolean"
		TOKEN_CLASS, // "class"
		TOKEN_COMMER, // ","
		TOKEN_DOT, // "."
		TOKEN_ELSE, // "else"
		TOKEN_EOF, // EOF
		TOKEN_EXTENDS, // "extends"
		TOKEN_FALSE, // "false"
		TOKEN_ID, // Identifier
		TOKEN_IF, // "if"
		TOKEN_INT, // "int"
		TOKEN_LBRACE, // "{"
		TOKEN_LBRACK, // "["
		TOKEN_LENGTH, // "length"
		TOKEN_LPAREN, // "("
		TOKEN_LT, // "<"
		TOKEN_MAIN, // "main"
		TOKEN_NEW, // "new"
		TOKEN_NOT, // "!"
		TOKEN_NUM, // IntegerLiteral
		// "out" is not a Java key word, but we treat it as
		// a MiniJava keyword, which will make the
		// compilation a little easier. Similar cases apply
		// for "println", "System" and "String".
		TOKEN_OUT, // "out"
		TOKEN_PRINTLN, // "println"
		TOKEN_PUBLIC, // "public"
		TOKEN_RBRACE, // "}"
		TOKEN_RBRACK, // "]"
		TOKEN_RETURN, // "return"
		TOKEN_RPAREN, // ")"
		TOKEN_SEMI, // ";"
		TOKEN_STATIC, // "static"
		TOKEN_STRING, // "String"
		TOKEN_SUB, // "-"
		TOKEN_SYSTEM, // "System"
		TOKEN_THIS, // "this"
		TOKEN_TIMES, // "*"
		TOKEN_TRUE, // "true"
		TOKEN_VOID, // "void"
		TOKEN_WHILE, // "while"
	}

	public Kind kind; // kind of the token
	public String lexeme; // extra lexeme for this token, if any
	public Integer lineNum; // on which line of the source file this token
							// appears

	// Some tokens don't come with lexeme but
	// others do.
	public Token(Kind kind, Integer lineNum)
	{
		this.kind = kind;
		this.lineNum = lineNum;
	}

	public Token(Kind kind, Integer lineNum, String lexeme)
	{
		this(kind, lineNum);
		this.lexeme = lexeme;
	}

	@Override
	public String toString()
	{
		String s;

		// to check that the "lineNum" field has been properly set.
		if (this.lineNum == null)
			new util.Todo();

		s = ": " + ((this.lexeme == null) ? "<NONE>" : this.lexeme)
				+ " : at line " + this.lineNum.toString();
		return this.kind.toString() + s;
	}
}
