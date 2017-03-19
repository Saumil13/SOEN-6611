
public class insrtAcc extends Acc_bank {

	private static double init_default=7.95;
	private double rate_int;
	public String balanceAmt;
	public char[] accNo;
	public void init_insert()
	{
		bal_amt=0.0;
		rate_int=init_default;
	}
	public void add_insrt_mnthly()
	{
		bal_amt=bal_amt + (bal_amt*rate_int/100)/12;
	}
	private double get_bal()
	{
		return bal_amt;
	}

}
