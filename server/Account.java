package server;

public class Account {
	
	private String name;
	private String address;
	private String accNo;
	private String user;
	private String pw;
	private float balance;
	
	
	public Account(String name, String address, String accNo, String user, String pw) {
		super();
		this.name = name;
		this.address = address;
		this.accNo = accNo;
		this.user = user;
		this.pw = pw;
		this.setBalance(balance + 1000);
	}//constructor w/ params
	
	public Account(){}//empty constructor

	//GETTERS & SETTERS
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}
	
	public float getBalance(){
		return balance;
	}
	
	public void setBalance(float balance){
		this.balance = balance;
	}
	
}//ACCOUNT CLASS