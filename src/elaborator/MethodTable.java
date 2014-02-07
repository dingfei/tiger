package elaborator;

import java.util.Iterator;

public class MethodTable
{
	private java.util.LinkedHashMap<String, ast.type.T> table;

	public MethodTable()
	{
		this.table = new java.util.LinkedHashMap<String, ast.type.T>();
	}

	// Duplication is not allowed
	public void put(java.util.LinkedList<ast.dec.T> formals,
			java.util.LinkedList<ast.dec.T> locals)
	{
		for (ast.dec.T dec : formals)
		{
			ast.dec.Dec decc = (ast.dec.Dec) dec;
			if (this.table.get(decc.Id.id) != null)
			{
				System.out.println("duplicated parameter: " + decc.Id.id);
				System.exit(1);
			}
			this.table.put(decc.Id.id, decc.type);
		}

		for (ast.dec.T dec : locals)
		{
			ast.dec.Dec decc = (ast.dec.Dec) dec;
			if (this.table.get(decc.Id.id) != null)
			{
				System.out.println("duplicated variable: " + decc.Id.id);
				System.exit(1);
			}
			this.table.put(decc.Id.id, decc.type);
		}

	}

	// return null for non-existing keys
	public ast.type.T get(String id)
	{
		return this.table.get(id);
	}

	public void dump()
	{
		System.out.println("===MethodTable dump start:===\n");
		Iterator<String> methodName = this.table.keySet().iterator();
		while (methodName.hasNext())
		{
			String strMethodName = methodName.next();
			System.out.print("Var Name: " + strMethodName + "			");
			System.out.println("Var Type: "
					+ this.table.get(strMethodName).toString());
		}
		System.out.println("\n===MethodTable dump end===\n");
	}

	@Override
	public String toString()
	{
		return this.table.toString();
	}
}
