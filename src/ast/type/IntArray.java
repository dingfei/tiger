package ast.type;

import ast.Visitor;

public class IntArray extends T
{
	public IntArray(int line)
	{
		this.lineNum = line;
	}

	@Override
	public String toString()
	{
		return "@int[]";
	}

	@Override
	public int getNum()
	{
		return 1;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}
}
