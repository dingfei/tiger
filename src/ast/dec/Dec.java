package ast.dec;

import ast.Visitor;

public class Dec extends T
{
	public ast.type.T type;
	public ast.exp.Id Id;

	public Dec(ast.type.T type, ast.exp.Id Id, int line)
	{
		this.type = type;
		this.Id = Id;
		this.lineNum = line;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}
}
