package ast.exp;

public class True extends T
{
	public True(int line)
	{
		this.lineNum = line;
	}

	@Override
	public void accept(ast.Visitor v)
	{
		v.visit(this);
		return;
	}
}
