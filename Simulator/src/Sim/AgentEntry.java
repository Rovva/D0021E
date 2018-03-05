package Sim;

public class AgentEntry{
	
	private Router router;
	private Link link;
	private int networkID;
	private int oldNetworkID;
	
	public AgentEntry(Router router, Link link, int networkID){
		this.router = router;
		this.link = link;
		this.networkID = networkID;
		
	}
	
	public Router getRouter() {
		return this.router;
	}
	
	public Link getLink() {
		return this.link;
	}
	
	public int getNetworkID() {
		return this.networkID;
	}
	
	public void changeNetworkID(int newNetworkID) {
		this.oldNetworkID = this.networkID;
		this.networkID = newNetworkID;
	}
		
	
}
