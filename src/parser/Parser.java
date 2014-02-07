package parser;

import ast.exp.T;
import lexer.Lexer;
import lexer.Token;
import lexer.Token.Kind;

public class Parser
{
	Lexer lexer;
	Token current;

	public Parser(String fname, java.io.InputStream fstream)
	{
		lexer = new Lexer(fname, fstream);
		current = lexer.nextToken();
	}

	// /////////////////////////////////////////////
	// utility methods to connect the lexer
	// and the parser.

	private void advance()
	{
		current = lexer.nextToken();
	}

	private void eatToken(Kind kind)
	{
		if (kind == current.kind)
			advance();
		else
		{
			System.out.println("Expects: " + kind.toString());
			System.out.println("But got: " + current.kind.toString());
			System.out.println("@line:" + current.lineNum);
			System.exit(1);
		}
	}

	private void error()
	{
		System.out.println("Syntax error: compilation aborting...\n");
		System.exit(1);
		return;
	}

	// ////////////////////////////////////////////////////////////
	// below are method for parsing.

	// A bunch of parsing methods to parse expressions. The messy
	// parts are to deal with precedence and associativity.

	// ExpList -> Exp ExpRest*
	// ->
	// ExpRest -> , Exp
	private java.util.LinkedList<T> parseExpList()
	{
		java.util.LinkedList<T> args = new java.util.LinkedList<T>();
		if (current.kind == Kind.TOKEN_RPAREN)
			return args;
		args.add(parseExp());
		while (current.kind == Kind.TOKEN_COMMER)
		{
			advance();
			args.add(parseExp());
		}
		return args;
	}

	// AtomExp -> (exp)
	// -> INTEGER_LITERAL
	// -> true
	// -> false
	// -> this
	// -> id
	// -> new int [exp]
	// -> new id ()
	private ast.exp.T parseAtomExp()
	{
		ast.exp.T exp = null;
		switch (current.kind)
		{
			case TOKEN_LPAREN:
				advance();
				exp = parseExp();
				eatToken(Kind.TOKEN_RPAREN);
				return new ast.exp.Parenthesis(exp, this.current.lineNum);
			case TOKEN_NUM:
				int num = Integer.parseInt(this.current.lexeme);
				advance();
				return new ast.exp.Num(num, this.current.lineNum);
			case TOKEN_TRUE:
				advance();
				return new ast.exp.True(this.current.lineNum);
			case TOKEN_FALSE:
				advance();
				return new ast.exp.False(this.current.lineNum);
			case TOKEN_THIS:
				advance();
				return new ast.exp.This(this.current.lineNum);
			case TOKEN_ID:
				String id = this.current.lexeme;
				advance();
				return new ast.exp.Id(id, this.current.lineNum);
			case TOKEN_NEW:
			{
				advance();
				switch (current.kind)
				{
					case TOKEN_INT:
						advance();
						eatToken(Kind.TOKEN_LBRACK);
						exp = parseExp();
						eatToken(Kind.TOKEN_RBRACK);
						return new ast.exp.NewIntArray(exp,
								this.current.lineNum);
					case TOKEN_ID:
						exp = parseExp();

						eatToken(Kind.TOKEN_LPAREN);
						eatToken(Kind.TOKEN_RPAREN);
						return new ast.exp.NewObject((ast.exp.Id) exp,
								this.current.lineNum);
					default:
						error();
						return null;
				}
			}
			default:
				error();
				return null;
		}
	}

	// NotExp -> AtomExp
	// -> AtomExp .id (expList)
	// -> AtomExp [exp]
	// -> AtomExp .length
	private ast.exp.T parseNotExp()
	{
		ast.exp.T exp = parseAtomExp();
		while (current.kind == Kind.TOKEN_DOT
				|| current.kind == Kind.TOKEN_LBRACK)
		{
			if (current.kind == Kind.TOKEN_DOT)
			{
				advance();
				if (current.kind == Kind.TOKEN_LENGTH)
				{
					advance();
					exp = new ast.exp.Length(exp, this.current.lineNum);
				}
				else if (current.kind == Kind.TOKEN_ID)
				{
					ast.exp.Id Id = (ast.exp.Id) parseAtomExp();
					eatToken(Kind.TOKEN_LPAREN);
					java.util.LinkedList<T> args = parseExpList();
					eatToken(Kind.TOKEN_RPAREN);
					exp = new ast.exp.Call(exp, Id, args, this.current.lineNum);
				}
			}
			else
			{
				advance();
				ast.exp.T index = parseExp();
				eatToken(Kind.TOKEN_RBRACK);
				exp = new ast.exp.ArraySelect((ast.exp.Id) exp, index,
						this.current.lineNum);
			}
		}
		return exp;
	}

	// TimesExp -> ! TimesExp
	// -> NotExp
	private ast.exp.T parseTimesExp()
	{
		int notTimes = 0;
		while (current.kind == Kind.TOKEN_NOT)
		{
			notTimes++;
			advance();
		}
		ast.exp.T exp = parseNotExp();

		if (notTimes != 0)
			for (int i = 0; i < notTimes; i++)
				exp = new ast.exp.Not(exp, this.current.lineNum);

		return exp;
	}

	// AddSubExp -> TimesExp * TimesExp
	// -> TimesExp
	private ast.exp.T parseAddSubExp()
	{
		ast.exp.T left = parseTimesExp();
		while (current.kind == Kind.TOKEN_TIMES)
		{
			advance();
			ast.exp.T right = parseTimesExp();
			left = new ast.exp.Times(left, right, this.current.lineNum);
		}
		return left;
	}

	// LtExp -> AddSubExp + AddSubExp
	// -> AddSubExp - AddSubExp
	// -> AddSubExp
	private ast.exp.T parseLtExp()
	{
		ast.exp.T left = parseAddSubExp();
		while (current.kind == Kind.TOKEN_ADD || current.kind == Kind.TOKEN_SUB)
		{
			Kind kind = this.current.kind;
			advance();
			ast.exp.T right = parseAddSubExp();
			if (kind == Kind.TOKEN_ADD)
				left = new ast.exp.Add(left, right, this.current.lineNum);
			else
				left = new ast.exp.Sub(left, right, this.current.lineNum);
		}
		return left;
	}

	// AndExp -> LtExp < LtExp
	// -> LtExp
	private ast.exp.T parseAndExp()
	{
		ast.exp.T left = parseLtExp();
		while (current.kind == Kind.TOKEN_LT)
		{
			advance();
			ast.exp.T right = parseLtExp();
			left = new ast.exp.Lt(left, right, this.current.lineNum);
		}
		return left;
	}

	// Exp -> AndExp && AndExp
	// -> AndExp
	private ast.exp.T parseExp()
	{
		ast.exp.T left = parseAndExp();
		while (current.kind == Kind.TOKEN_AND)
		{
			advance();
			ast.exp.T right = parseAndExp();
			left = new ast.exp.And(left, right, this.current.lineNum);
		}
		return left;
	}

	// Statement -> { Statement* }
	// -> if ( Exp ) Statement else Statement
	// -> while ( Exp ) Statement
	// -> System.out.println ( Exp ) ;
	// -> id = Exp ;
	// -> id [ Exp ]= Exp ;
	private ast.stm.T parseStatement()
	{
		// Lab1. Exercise 4: Fill in the missing code
		// to parse a statement.

		ast.stm.T stm = null;
		int line = 0;
		switch (current.kind)
		{
			case TOKEN_LBRACE:
				eatToken(Kind.TOKEN_LBRACE);
				java.util.LinkedList<ast.stm.T> blocks = parseStatements();
				eatToken(Kind.TOKEN_RBRACE);
				return new ast.stm.Block(blocks);
			case TOKEN_IF:
				eatToken(Kind.TOKEN_IF);
				line = this.current.lineNum;
				eatToken(Kind.TOKEN_LPAREN);
				ast.exp.T ifcon = parseExp();
				eatToken(Kind.TOKEN_RPAREN);
				ast.stm.T ifstm1 = parseStatement();
				eatToken(Kind.TOKEN_ELSE);
				ast.stm.T ifstm2 = parseStatement();
				stm = new ast.stm.If(ifcon, ifstm1, ifstm2, line);
				break;
			case TOKEN_WHILE:
				eatToken(Kind.TOKEN_WHILE);
				line = this.current.lineNum;
				eatToken(Kind.TOKEN_LPAREN);
				ast.exp.T whiexp = parseExp();
				eatToken(Kind.TOKEN_RPAREN);
				ast.stm.T whistm = parseStatement();
				stm = new ast.stm.While(whiexp, whistm, line);
				break;
			case TOKEN_SYSTEM:
				eatToken(Kind.TOKEN_SYSTEM);
				line = this.current.lineNum;
				eatToken(Kind.TOKEN_DOT);
				eatToken(Kind.TOKEN_OUT);
				eatToken(Kind.TOKEN_DOT);
				eatToken(Kind.TOKEN_PRINTLN);
				eatToken(Kind.TOKEN_LPAREN);
				ast.exp.T sysexp = parseExp();
				eatToken(Kind.TOKEN_RPAREN);
				eatToken(Kind.TOKEN_SEMI);
				stm = new ast.stm.Print(sysexp, line);
				break;
			case TOKEN_ID:
				ast.exp.T exp = parseExp();
				if (exp instanceof ast.exp.Id)
				{
					eatToken(Kind.TOKEN_ASSIGN);
					line = this.current.lineNum;
					ast.exp.T ass = parseExp();
					eatToken(Kind.TOKEN_SEMI);
					stm = new ast.stm.Assign((ast.exp.Id) exp, ass, line);
				}
				else if (exp instanceof ast.exp.ArraySelect)
				{
					eatToken(Kind.TOKEN_ASSIGN);
					line = this.current.lineNum;
					ast.exp.T ass = parseExp();
					eatToken(Kind.TOKEN_SEMI);
					stm = new ast.stm.AssignArray((ast.exp.ArraySelect) exp,
							ass, line);
				}
				break;
			default:
				break;
		}
		return stm;
	}

	// Statements -> Statement Statements
	// ->
	private java.util.LinkedList<ast.stm.T> parseStatements()
	{
		java.util.LinkedList<ast.stm.T> stms = new java.util.LinkedList<ast.stm.T>();
		while (current.kind == Kind.TOKEN_LBRACE
				|| current.kind == Kind.TOKEN_IF
				|| current.kind == Kind.TOKEN_WHILE
				|| current.kind == Kind.TOKEN_SYSTEM
				|| current.kind == Kind.TOKEN_ID)
		{
			stms.add(parseStatement());
		}
		return stms;
	}

	// Type -> int[]
	// -> boolean
	// -> int
	// -> id
	private ast.type.T parseType()
	{
		// Lab1. Exercise 4: Fill in the missing code
		// to parse a type.
		ast.type.T type = null;
		if (current.kind == Kind.TOKEN_BOOLEAN)
		{
			advance();
			type = new ast.type.Boolean(this.current.lineNum);
		}
		else if (current.kind == Kind.TOKEN_INT)
		{
			if (this.lexer.lookForward(2, "[]"))
			{
				advance();
				advance();
				advance();
				type = new ast.type.IntArray(this.current.lineNum);
			}
			else
			{
				advance();
				type = new ast.type.Int(this.current.lineNum);
			}
		}
		else if (current.kind == Kind.TOKEN_ID)
		{
			ast.exp.Id Id = (ast.exp.Id) parseExp();
			type = new ast.type.Class(Id.id, this.current.lineNum);
		}
		return type;
	}

	// VarDecl -> Type id ;
	private ast.dec.T parseVarDecl()
	{
		// to parse the "Type" nonterminal in this method, instead of writing
		// a fresh one.

		ast.type.T type = parseType();
		ast.exp.Id Id = (ast.exp.Id) parseExp();
		eatToken(Kind.TOKEN_SEMI);
		return new ast.dec.Dec(type, Id, this.current.lineNum);
	}

	// VarDecls -> VarDecl VarDecls
	// ->
	private java.util.LinkedList<ast.dec.T> parseVarDecls()
	{
		java.util.LinkedList<ast.dec.T> decls = new java.util.LinkedList<ast.dec.T>();
		while (current.kind == Kind.TOKEN_INT
				|| current.kind == Kind.TOKEN_BOOLEAN
				|| current.kind == Kind.TOKEN_ID)
		{
			if (current.kind == Kind.TOKEN_ID)
			{
				if (this.lexer.lookForward(1, "="))
					break;
			}
			decls.add(parseVarDecl());
		}
		return decls;
	}

	// FormalList -> Type id FormalRest*
	// ->
	// FormalRest -> , Type id
	private java.util.LinkedList<ast.dec.T> parseFormalList()
	{
		java.util.LinkedList<ast.dec.T> formals = new java.util.LinkedList<ast.dec.T>();
		if (current.kind == Kind.TOKEN_INT
				|| current.kind == Kind.TOKEN_BOOLEAN
				|| current.kind == Kind.TOKEN_ID)
		{

			ast.dec.Dec dec = null;
			ast.type.T type = null;
			ast.exp.Id Id = null;

			type = parseType();
			Id = (ast.exp.Id) parseAtomExp();
			dec = new ast.dec.Dec(type, Id, this.current.lineNum);
			formals.add(dec);
			while (current.kind == Kind.TOKEN_COMMER)
			{
				advance();
				type = parseType();
				Id = (ast.exp.Id) parseAtomExp();
				dec = new ast.dec.Dec(type, Id, this.current.lineNum);
				formals.add(dec);
			}
		}
		return formals;
	}

	// Method -> public Type id ( FormalList )
	// { VarDecl* Statement* return Exp ;}
	private ast.method.Method parseMethod()
	{
		// Lab1. Exercise 4: Fill in the missing code
		// to parse a method.

		eatToken(Kind.TOKEN_PUBLIC);
		ast.type.T retType = parseType();
		ast.exp.Id Id = (ast.exp.Id) parseExp();
		eatToken(Kind.TOKEN_LPAREN);
		java.util.LinkedList<ast.dec.T> formals = parseFormalList();
		eatToken(Kind.TOKEN_RPAREN);
		eatToken(Kind.TOKEN_LBRACE);
		java.util.LinkedList<ast.dec.T> locals = parseVarDecls();
		java.util.LinkedList<ast.stm.T> stms = parseStatements();
		eatToken(Kind.TOKEN_RETURN);
		ast.exp.T retExp = parseExp();
		eatToken(Kind.TOKEN_SEMI);
		eatToken(Kind.TOKEN_RBRACE);
		return new ast.method.Method(retType, Id.id, formals, locals, stms,
				retExp, this.current.lineNum);
	}

	// MethodDecls -> MethodDecl MethodDecls
	// ->
	private java.util.LinkedList<ast.method.T> parseMethodDecls()
	{
		java.util.LinkedList<ast.method.T> methods = new java.util.LinkedList<ast.method.T>();
		while (current.kind == Kind.TOKEN_PUBLIC)
		{
			methods.add(parseMethod());
		}
		return methods;
	}

	// ClassDecl -> class id { VarDecl* MethodDecl* }
	// -> class id extends id { VarDecl* MethodDecl* }
	private ast.classs.T parseClassDecl()
	{
		eatToken(Kind.TOKEN_CLASS);
		ast.exp.Id Id = (ast.exp.Id) parseExp();
		ast.exp.Id extId = null;
		if (current.kind == Kind.TOKEN_EXTENDS)
		{
			eatToken(Kind.TOKEN_EXTENDS);
			extId = (ast.exp.Id) parseExp();
		}
		eatToken(Kind.TOKEN_LBRACE);
		java.util.LinkedList<ast.dec.T> decs = parseVarDecls();
		java.util.LinkedList<ast.method.T> methods = parseMethodDecls();
		eatToken(Kind.TOKEN_RBRACE);
		ast.classs.Class classs = null;
		if (extId == null)
			classs = new ast.classs.Class(Id, null, decs, methods,
					this.current.lineNum);
		else
			classs = new ast.classs.Class(Id, extId, decs, methods,
					this.current.lineNum);

		return classs;
	}

	// ClassDecls -> ClassDecl ClassDecls
	// ->
	private java.util.LinkedList<ast.classs.T> parseClassDecls()
	{
		java.util.LinkedList<ast.classs.T> classs = new java.util.LinkedList<ast.classs.T>();

		while (current.kind == Kind.TOKEN_CLASS)
		{
			classs.add(parseClassDecl());
		}
		return classs;
	}

	// MainClass -> class id
	// {
	// public static void main ( String [] id )
	// {
	// Statement
	// }
	// }
	private ast.mainClass.T parseMainClass()
	{
		// Lab1. Exercise 4: Fill in the missing code
		// to parse a main class as described by the
		// grammar above.

		eatToken(Kind.TOKEN_CLASS);
		ast.exp.T Id1 = parseExp();
		eatToken(Kind.TOKEN_LBRACE);
		eatToken(Kind.TOKEN_PUBLIC);
		eatToken(Kind.TOKEN_STATIC);
		eatToken(Kind.TOKEN_VOID);
		eatToken(Kind.TOKEN_MAIN);
		eatToken(Kind.TOKEN_LPAREN);
		eatToken(Kind.TOKEN_STRING);
		eatToken(Kind.TOKEN_LBRACK);
		eatToken(Kind.TOKEN_RBRACK);
		ast.exp.T Id2 = parseExp();
		eatToken(Kind.TOKEN_RPAREN);
		eatToken(Kind.TOKEN_LBRACE);
		ast.stm.T stm = parseStatement();
		eatToken(Kind.TOKEN_RBRACE);
		eatToken(Kind.TOKEN_RBRACE);
		return new ast.mainClass.MainClass((ast.exp.Id) Id1, (ast.exp.Id) Id2,
				stm, this.current.lineNum);
	}

	// Program -> MainClass ClassDecl*
	private ast.program.T parseProgram()
	{
		ast.mainClass.T main = parseMainClass();
		java.util.LinkedList<ast.classs.T> classs = parseClassDecls();
		eatToken(Kind.TOKEN_EOF);
		return new ast.program.Program(main, classs, this.current.lineNum);
	}

	public ast.program.T parse()
	{
		ast.program.T prog = parseProgram();
		return prog;
	}
}
