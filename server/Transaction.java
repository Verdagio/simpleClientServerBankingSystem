package server;

public class Transaction {
	private String accNo;
	private char type;
	private float amt;
	
	public Transaction(String accNo, char type, float amt) {
		super();
		this.accNo = accNo;
		this.type = type;
		this.amt = amt;
	}//constructor
	
	public Transaction(){}//default

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public float getAmt() {
		return amt;
	}

	public void setAmt(float amt) {
		this.amt = amt;
	}

	public String history() {
		return "Transaction [accNo: " + accNo + "| type: " + type + "| amt: " + amt + "]";
	}
	
	
}//class
