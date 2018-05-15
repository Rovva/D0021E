package Sim;

public class notifySender  implements Event {
	
	int newInterface;

	
	public notifySender(int newInterface){
		this.newInterface = newInterface;

	}
	
	public int getInterface() {
		return this.newInterface;
	}
	
	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
		
	}
	
	
}