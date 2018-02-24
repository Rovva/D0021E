package Sim;

public class changeInterface implements Event {
	
	int newInterface;
	Link link;
	Node node;
	
	public changeInterface(int newInterface, Link link, Node node){
		this.newInterface = newInterface;
		this.link = link;
		this.node = node;
		
	}
	
	public int getInterface() {
		return this.newInterface;
	}
	

	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
		
	}
	
	
}
