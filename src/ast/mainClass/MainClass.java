package ast.mainClass;

import ast.Visitor;

public class MainClass extends T
{
	public ast.exp.T Id;
	public ast.exp.T arg;
	public ast.stm.T stm;

	public MainClass(ast.exp.T Id, ast.exp.T arg, ast.stm.T stm, int line)
	{
		this.lineNum = line;
		this.Id = Id;
		this.arg = arg;
		this.stm = stm;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
		return;
	}

}
