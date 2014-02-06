package ast.type;

import ast.Visitor;

public class Class extends T
{
	public String id;

	public Class(String id, int line)
	{
		this.lineNum = line;
		this.id = id;
	}

	public Class(String id)
	{
		this.id = id;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString()
	{
		return this.id;
	}

	@Override
	public int getNum()
	{
		return 2;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}
}
