package ast.type;

import ast.Visitor;

public class Int extends T
{
	public Int(int line)
	{
		this.lineNum = line;
	}

	public Int()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString()
	{
		return "@int";
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	@Override
	public int getNum()
	{
		return 0;
	}
}
