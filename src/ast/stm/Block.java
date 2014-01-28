package ast.stm;

public class Block extends T
{
	public java.util.LinkedList<T> stms;

	public Block(java.util.LinkedList<T> stms, int line)
	{
		this.lineNum = line;
		this.stms = stms;
	}

	@Override
	public void accept(ast.Visitor v)
	{
		v.visit(this);
	}
}
