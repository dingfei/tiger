package ast.exp;

public class Parenthesis extends T
{

	public T exp;

	public Parenthesis(T exp, int line)
	{
		this.lineNum = line;
		this.exp = exp;
	}

	@Override
	public void accept(ast.Visitor v)
	{
		v.visit(this);
		return;
	}

}
