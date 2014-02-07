package ast.mainClass;

import ast.Visitor;

public class MainClass extends T
{
	public ast.exp.Id Id;
	public ast.exp.Id arg;
	public ast.stm.T stm;

	public MainClass(ast.exp.Id Id, ast.exp.Id arg, ast.stm.T stm, int line)
	{
		this.lineNum = line;
		this.Id = Id;
		this.arg = arg;
		this.stm = stm;
	}

	public MainClass(ast.exp.Id Id, ast.exp.Id arg, ast.stm.T stm)
	{
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
