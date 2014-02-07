package ast.exp;

public class NewObject extends T
{
	public ast.exp.Id Id;

	public NewObject(ast.exp.Id Id, int line)
	{
		this.Id = Id;
		this.lineNum = line;
	}

	public NewObject(ast.exp.Id Id)
	{
		this.Id = Id;
	}

	@Override
	public void accept(ast.Visitor v)
	{
		v.visit(this);
		return;
	}
}
