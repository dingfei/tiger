package lexer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import lexer.Token.Kind;

public class Lexer
{
	String fname; // the input file name to be compiled
	InputStream fstream; // input stream for the above file

	int lineNum = 1;// the line number of current token

	public Lexer(String fname, InputStream fstream)
	{
		this.fname = fname;
		this.fstream = fstream;
	}

	// When called, return the next token (refer to the code "Token.java")
	// from the input stream.
	// Return TOKEN_EOF when reaching the end of the input stream.
	private Token nextTokenInternal() throws Exception
	{
		int c = this.fstream.read();
		if (-1 == c)
			// The value for "lineNum" is now "null",
			// you should modify this to an appropriate
			// line number for the "EOF" token.
			return new Token(Kind.TOKEN_EOF, this.lineNum);

		// skip all kinds of "blanks"
		while (' ' == c || '\t' == c || '\n' == c)
		{
			if ('\n' == c)
				this.lineNum = this.lineNum + 1;
			c = this.fstream.read();
		}
		if (-1 == c)
			return new Token(Kind.TOKEN_EOF, this.lineNum);
		switch (c)
		{
			case '+':
				return new Token(Kind.TOKEN_ADD, this.lineNum);
			case '&':
			{
				int cp = this.fstream.read();
				if (cp == '&')
					return new Token(Kind.TOKEN_AND, this.lineNum);
			}
			case '=':
				return new Token(Kind.TOKEN_ASSIGN, this.lineNum);
			case ',':
				return new Token(Kind.TOKEN_COMMER, this.lineNum);
			case '.':
				return new Token(Kind.TOKEN_DOT, this.lineNum);
			case '{':
				return new Token(Kind.TOKEN_LBRACE, this.lineNum);
			case '[':
				return new Token(Kind.TOKEN_LBRACK, this.lineNum);
			case '(':
				return new Token(Kind.TOKEN_LPAREN, this.lineNum);
			case '<':
				return new Token(Kind.TOKEN_LT, this.lineNum);
			case '!':
				return new Token(Kind.TOKEN_NOT, this.lineNum);
			case '}':
				return new Token(Kind.TOKEN_RBRACE, this.lineNum);
			case ']':
				return new Token(Kind.TOKEN_RBRACK, this.lineNum);
			case ')':
				return new Token(Kind.TOKEN_RPAREN, this.lineNum);
			case ';':
				return new Token(Kind.TOKEN_SEMI, this.lineNum);
			case '-':
				return new Token(Kind.TOKEN_SUB, this.lineNum);
			case '*':
				return new Token(Kind.TOKEN_TIMES, this.lineNum);

			default:
				// Lab 1, exercise 2: supply missing code to
				// lex other kinds of tokens.
				// Hint: think carefully about the basic
				// data structure and algorithms. The code
				// is not that much and may be less than 50 lines. If you
				// find you are writing a lot of code, you
				// are on the wrong way.

				StringBuffer sb = new StringBuffer();

				// if it is a number
				if (c >= '0' && c <= '9')
				{
					sb.append(c - 48);
					this.fstream.mark(0);
					c = this.fstream.read();
					while (c >= '0' && c <= '9')
					{
						sb.append(c - 48);
						this.fstream.mark(0);
						c = this.fstream.read();
					}
					this.fstream.reset();
					return new Token(Kind.TOKEN_NUM, this.lineNum,
							sb.toString());
				}

				// if it is a string
				else if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z')
				{
					// read this string
					// compare with keyword
					// if it is a key word, return keyword token
					// else return identifier token

					sb.append((char) c);
					this.fstream.mark(0);
					c = this.fstream.read();
					while (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z'
							|| c >= '0' && c <= '9' || c == '_')
					{
						sb.append((char) c);
						this.fstream.mark(0);
						c = this.fstream.read();
					}
					this.fstream.reset();

					String str = sb.toString();

					Iterator<String> it = lexer.Token.keyword.keySet()
							.iterator();
					while (it.hasNext())
					{
						if (it.next().equals(str))
						{
							Kind kind = lexer.Token.keyword.get(str);
							return new Token(kind, this.lineNum);
						}
					}
					return new Token(Kind.TOKEN_ID, this.lineNum, str);
				}

				// if it is annotation
				if (c == 47)
				{
					while (c != '\n')
						c = this.fstream.read();
					this.lineNum = this.lineNum + 1;
					return this.nextTokenInternal();
				}

				return null;
		}
	}

	public Token nextToken()
	{
		Token t = null;

		try
		{
			t = this.nextTokenInternal();
		} catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		if (control.Control.lex)
			System.out.println(t.toString());
		return t;
	}

	public boolean lookForward(int bit, String str)
	{
		StringBuffer buff = new StringBuffer();
		this.fstream.mark(0);
		int c = 0;
		do
		{
			try
			{
				c = this.fstream.read();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (c == ' ');

		while (bit-- > 0)
		{
			buff.append((char) c);
			try
			{
				c = this.fstream.read();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try
		{
			this.fstream.reset();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (buff.toString().equals(str))
			return true;
		return false;
	}
}
