package ast.program;

import ast.Visitor;

public class Program extends T
{
	public ast.mainClass.T mainClass;
	public java.util.LinkedList<ast.classs.T> classes;

	public Program(ast.mainClass.T mainClass,
			java.util.LinkedList<ast.classs.T> classes, int line)
	{
		this.lineNum = line;
		this.mainClass = mainClass;
		this.classes = classes;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
		return;
	}
}
