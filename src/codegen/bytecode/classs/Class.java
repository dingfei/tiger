package codegen.bytecode.classs;

import codegen.bytecode.Visitor;

public class Class extends T
{
	public ast.exp.Id Id;
	public ast.exp.Id extendss; // null for non-existing "extends"
	public java.util.LinkedList<codegen.bytecode.dec.T> decs;
	public java.util.LinkedList<codegen.bytecode.method.T> methods;

	public Class(ast.exp.Id Id, ast.exp.Id extendss,
			java.util.LinkedList<codegen.bytecode.dec.T> decs,
			java.util.LinkedList<codegen.bytecode.method.T> methods)
	{
		this.Id = Id;
		this.extendss = extendss;
		this.decs = decs;
		this.methods = methods;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}
}
