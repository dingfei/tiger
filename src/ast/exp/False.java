package ast.exp;

public class False extends T
{
	public False(int line)
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
