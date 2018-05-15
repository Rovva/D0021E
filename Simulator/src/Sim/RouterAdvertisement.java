package Sim;

public class RouterAdvertisement implements Event {
	
	private RouteTableEntry [] routingTable;
	
	public RouterAdvertisement(RouteTableEntry [] routingTable){
		this.routingTable = routingTable;
	}
	
	public RouteTableEntry[] getRoutingTable() {
		return this.routingTable;
	}
	
	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
		
	}
	
}
