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
	
	public Link getLink() {
		return this.link;
	}
	
	public Node getNode() {
		return this.node;
	}
	
	public int getId() {
		return this.node.getAddr().networkId();
	}
	
	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
		
	}
	
	
}
