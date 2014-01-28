package ast.stm;

public class AssignArray extends T
{
	public ast.exp.ArraySelect as;
	public ast.exp.T exp;

	public AssignArray(ast.exp.ArraySelect as, ast.exp.T exp, int line)
	{
		this.lineNum = line;
		this.as = as;
		this.exp = exp;
	}

	@Override
	public void accept(ast.Visitor v)
	{
		v.visit(this);
	}
}
