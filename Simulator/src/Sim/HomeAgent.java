package Sim;

import java.util.HashMap;
import java.util.Map;

public class HomeAgent {
	
	HashMap<NetworkAddr, NetworkAddr> agentTable;
	
	public HomeAgent() {
		agentTable = new HashMap<NetworkAddr, NetworkAddr>();
		
	}
	
	public void addressUpdate(NetworkAddr oldID, NetworkAddr newID) {
		/*agentTable.put(update.getHomeOfAddress().networkId() + "."
				+ update.getHomeOfAddress().nodeId(), update.getCareOfAddress().networkId()
				+ "." + update.getCareOfAddress().nodeId());
		*/
		agentTable.put(oldID, newID);
	}
	
	/*public void addressUpdate(AddressUpdate update) {
		/*agentTable.put(update.getHomeOfAddress().networkId() + "."
				+ update.getHomeOfAddress().nodeId(), update.getCareOfAddress().networkId()
				+ "." + update.getCareOfAddress().nodeId());
	
		agentTable.put(update.getHomeOfAddress(), update.getCareOfAddress());
	}*/
	
	public boolean checkHashMap(NetworkAddr temp) {
		//return agentTable.containsKey(temp);
		Map<NetworkAddr, NetworkAddr> map = agentTable;
		for (Map.Entry<NetworkAddr, NetworkAddr> entry : map.entrySet()) {
			if(temp.networkId() == entry.getKey().networkId()) {
				if(temp.nodeId() == entry.getKey().nodeId()) {
					System.out.println("MATCHING KEY");
					return true;
				}
			}
		    //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
		return false;
		//return agentTable.containsKey(temp.networkId() + "."
		//		+ temp.nodeId());
		//return agentTable.containsKey(addr);
	}
	
	public NetworkAddr getCOA(NetworkAddr HOA) {
		/*String addr = agentTable.get(HOA.networkId()+"."+HOA.nodeId());
		System.out.println(addr);
		
		String[] temp = addr.split("\\.");
		return new NetworkAddr( Integer.parseInt(temp[0]),Integer.parseInt(temp[1]));
		return new NetworkAddr(agentTable.get(HOA.networkId() + "." + HOA.nodeId());
		*/
		NetworkAddr temp = null;
		Map<NetworkAddr, NetworkAddr> map = agentTable;
		for (Map.Entry<NetworkAddr, NetworkAddr> entry : map.entrySet()) {
			if(HOA.networkId() == entry.getKey().networkId()) {
				if(HOA.nodeId() == entry.getKey().nodeId()) {
					System.out.println("MATCHING VALUE");
					temp = entry.getValue();
				}
			}
		    //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
		return temp;
		
	}
	
}
