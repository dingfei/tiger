package elaborator;

import java.util.Iterator;

public class ClassTable
{
	// map each class name (a string), to the class bindings.
	private java.util.LinkedHashMap<String, ClassBinding> table;

	public ClassTable()
	{
		this.table = new java.util.LinkedHashMap<String, ClassBinding>();
	}

	// Duplication is not allowed
	public void put(String c, ClassBinding cb)
	{
		if (this.table.get(c) != null)
		{
			System.out.println("duplicated class: " + c);
			System.exit(1);
		}
		this.table.put(c, cb);
	}

	// put a field into this table
	// Duplication is not allowed
	public void put(String c, String id, ast.type.T type)
	{
		ClassBinding cb = this.table.get(c);
		cb.put(id, type);
		return;
	}

	// put a method into this table
	// Duplication is not allowed.
	// Also note that MiniJava does NOT allow overloading.
	public void put(String c, String id, MethodType type)
	{
		ClassBinding cb = this.table.get(c);
		cb.put(id, type);
		return;
	}

	// return null for non-existing class
	public ClassBinding get(String className)
	{
		return this.table.get(className);
	}

	// get type of some field
	// return null for non-existing field.
	public ast.type.T get(String className, String xid)
	{
		ClassBinding cb = this.table.get(className);
		ast.type.T type = cb.fields.get(xid);
		while (type == null)
		{ // search all parent classes until found or fail
			if (cb.extendss == null)
				return type;

			cb = this.table.get(cb.extendss);
			type = cb.fields.get(xid);
		}
		return type;
	}

	// get type of some method
	// return null for non-existing method
	public MethodType getm(String className, String mid)
	{
		ClassBinding cb = this.table.get(className);
		MethodType type = cb.methods.get(mid);
		while (type == null)
		{ // search all parent classes until found or fail
			if (cb.extendss == null)
				return type;

			cb = this.table.get(cb.extendss);
			type = cb.methods.get(mid);
		}
		return type;
	}

	public void dump()
	{
		System.out.println("===ClassTable dump start:===\n");

		Iterator<String> className = this.table.keySet().iterator();

		while (className.hasNext())
		{
			String strClassName = className.next();
			System.out.println("Class name: " + strClassName);
			ClassBinding cb = this.table.get(strClassName);

			if (cb.fields != null)
			{
				java.util.LinkedHashMap<String, ast.type.T> fields = cb.fields;
				Iterator<String> fieldsName = fields.keySet().iterator();
				while (fieldsName.hasNext())
				{
					String strFieldsName = fieldsName.next();
					System.out.print("Fields name: " + strFieldsName + "	");
					System.out.println("Fields type: "
							+ fields.get(strFieldsName).toString());
				}
			}

			if (cb.methods != null)
			{
				java.util.LinkedHashMap<String, MethodType> methods = cb.methods;
				Iterator<String> methodsName = methods.keySet().iterator();
				while (methodsName.hasNext())
				{
					String strMethodsName = methodsName.next();
					System.out.print("Method name: " + strMethodsName + "	");
					System.out.println("Mehotd type: "
							+ methods.get(strMethodsName));
				}
			}
			// System.out.println();
		}
		System.out.println("\n===ClassTable dump end.===\n");
	}

	@Override
	public String toString()
	{
		return this.table.toString();
	}
}
