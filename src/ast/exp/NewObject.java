package ast.exp;

public class NewObject extends T
{
	public ast.exp.Id Id;

	public NewObject(ast.exp.Id Id, int line)
	{
		this.Id = Id;
		this.lineNum = line;
	}

	@Override
	public void accept(ast.Visitor v)
	{
		v.visit(this);
		return;
	}
}
