
public class Account {

	public static void main(String[] args)
	{
	insrtAcc MyAcc= new insrtAcc();
	MyAcc.init_insert();
	MyAcc.init_data(2222,3333);
	MyAcc.deposit_bank_data(300);
	System.out.println(MyAcc.accNo);
	System.out.println("Net balance" +MyAcc.balanceAmt);
	MyAcc.withdraw_bank(50);
	System.out.println("Net balance" +MyAcc.balanceAmt);
	MyAcc.add_insrt_mnthly();
	
	}
}
