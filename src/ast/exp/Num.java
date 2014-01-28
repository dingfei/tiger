package ast.exp;

public class Num extends T
{
	public int num;

	public Num(int num, int line)
	{
		this.num = num;
		this.lineNum = line;
	}

	@Override
	public void accept(ast.Visitor v)
	{
		v.visit(this);
		return;
	}
}
