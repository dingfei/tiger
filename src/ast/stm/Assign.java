package ast.stm;

public class Assign extends T
{
	public ast.exp.Id Id;
	public ast.exp.T exp;

	public Assign(ast.exp.Id Id, ast.exp.T exp, int line)
	{
		this.lineNum = line;
		this.Id = Id;
		this.exp = exp;
	}

	public Assign(ast.exp.Id Id, ast.exp.T exp)
	{
		this.Id = Id;
		this.exp = exp;
	}

	@Override
	public void accept(ast.Visitor v)
	{
		v.visit(this);
	}
}
