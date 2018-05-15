package Sim;

public class AddressUpdate implements Event {
	
	private NetworkAddr homeOfAddress;
	private NetworkAddr careOfAddress;
	
	public AddressUpdate(NetworkAddr hoa, NetworkAddr coa) {
		this.homeOfAddress = hoa;
		this.careOfAddress = coa;
	}
	
	public NetworkAddr getHomeOfAddress() {
		return this.homeOfAddress;
	}
	
	public NetworkAddr getCareOfAddress() {
		return this.careOfAddress;
	}
	
	@Override
	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
		
	}

}
