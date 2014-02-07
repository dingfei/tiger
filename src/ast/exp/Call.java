package ast.exp;

public class Call extends T
{
	public T exp;
	public ast.exp.Id Id;
	public java.util.LinkedList<T> args;
	public String type; // type of first field "exp"
	public java.util.LinkedList<ast.type.T> at; // arg's type
	public ast.type.T rt;

	public Call(T exp, ast.exp.Id Id, java.util.LinkedList<T> args, int line)
	{
		this.exp = exp;
		this.Id = Id;
		this.args = args;
		this.type = null;
		this.lineNum = line;
	}

	@Override
	public void accept(ast.Visitor v)
	{
		v.visit(this);
		return;
	}
}
