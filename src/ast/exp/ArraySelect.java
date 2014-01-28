package ast.exp;

public class ArraySelect extends T
{
	public T array;
	public T index;

	public ArraySelect(T array, T index, int line)
	{
		this.array = array;
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
