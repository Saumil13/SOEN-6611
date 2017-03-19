
public class Acc_bank {

	public double bal_amt;
	public double acc_no;
	public void init_data(double amt, double no)
	{
		bal_amt=amt;
		acc_no=no;
	}
	
	public void deposit_bank_data(double amt)
	{
		bal_amt+=amt;
	}
	
	public double withdraw_bank(double amt)
	{
		if(bal_amt >=amt)
		{
			bal_amt-=amt;
			return amt;
		}
		else
		return 0;
	}
}
