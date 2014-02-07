package ast.exp;

public class Lt extends T
{
	public T left;
	public T right;

	public Lt(T left, T right, int line)
	{
		this.left = left;
		this.right = right;
		this.lineNum = line;
	}

	@Override
	public void accept(ast.Visitor v)
	{
		v.visit(this);
		return;
	}
}
