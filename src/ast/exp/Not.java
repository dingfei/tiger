package ast.exp;

public class Not extends T
{
	public T exp;

	public Not(T exp, int line)
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
