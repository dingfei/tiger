package codegen.bytecode.mainClass;

import codegen.bytecode.Visitor;

public class MainClass extends T
{
	public ast.exp.Id Id;
	public ast.exp.T arg;
	public java.util.LinkedList<codegen.bytecode.stm.T> stms;

	public MainClass(ast.exp.Id Id, ast.exp.T arg,
			java.util.LinkedList<codegen.bytecode.stm.T> stms)
	{
		this.Id = Id;
		this.arg = arg;
		this.stms = stms;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
		return;
	}

}
