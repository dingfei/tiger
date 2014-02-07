package ast.exp;

public class NewIntArray extends T
{
	public T exp;

	public NewIntArray(T exp, int line)
	{
		this.exp = exp;
		this.lineNum = line;
	}

	@Override
	public void accept(ast.Visitor v)
	{
		v.visit(this);
		return;
	}
}
