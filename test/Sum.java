class Sum
{
	public static void main(String[] a)
	{
		System.out.println(new Doit().doit(101));
	}
}

<<<<<<< HEAD
class Doit
{
	public int doit(int n)
	{
		int sum;
		int i;

		i = 0;
		sum = 0;
		while (i < n)
		{
			sum = sum + i;
			i = i + 1;
		}
		return sum;
	}
=======
class Doit {
    public int doit(int n) {
        int sum;
        int i;
        
        i = 0;
        sum = 0;
        while (i<n){
        	sum = sum + i;
        	i = i+1;
        }
        return sum;
    }
>>>>>>> refs/remotes/origin/Lab3
}
