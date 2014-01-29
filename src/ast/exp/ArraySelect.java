package ast.exp;

public class ArraySelect extends T
{
	public ast.exp.Id Id;
	public T index;

	public ArraySelect(ast.exp.Id Id, T index, int line)
	{
		this.Id = Id;
		this.index = index;
		this.lineNum = line;
	}

	@Override
	public void accept(ast.Visitor v)
	{
		v.visit(this);
		return;
	}
}
