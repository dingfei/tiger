package ast.exp;

public class This extends T
{
	public This(int line)
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
