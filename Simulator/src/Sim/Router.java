package Sim;

import java.util.ArrayList;

// This class implements a simple router

public class Router extends SimEnt{

	public static ArrayList<AgentEntry> TableAgent = new ArrayList<AgentEntry>();
	private RouteTableEntry [] _routingTable;
	private int _interfaces;
	private int _now=0;
	private int updatedInterface;

	// When created, number of interfaces are defined
	
	Router(int interfaces)
	{
		_routingTable = new RouteTableEntry[interfaces];
		_interfaces=interfaces;
	}
	
	public void printRouterTable() {
		for(int i = 0; i <_routingTable.length; i++) {
			if(_routingTable[i]!=null) {
				System.out.println("Node: " +((Node)_routingTable[i].node()).getAddr().networkId() + "." + ((Node)_routingTable[i].node()).getAddr().nodeId() + " router interface:" + i);
			}
			
		}
	}
	
	// This method connects links to the router and also informs the 
	// router of the host connects to the other end of the link
	
	public void connectInterface(int interfaceNumber, SimEnt link, SimEnt node)
	{
		int oldID = ((Node)node).getAddr().nodeId();
		this.updatedInterface = interfaceNumber;
		if (interfaceNumber<_interfaces)
		{
			
			for (int i = 0; i < _routingTable.length; i++) {
				if (_routingTable[i] == null) {
					continue;
				}
				
				if (((Node)_routingTable[i].node()).getAddr().networkId() == oldID) {
					oldID++;
					i = 0;
				}
			}
			_routingTable[interfaceNumber] = new RouteTableEntry(link, node);
			if (((Node)node).getHomeRouter() == null) {
				((Node)node).setHomeRouter(this);
			}
		}
		else
			System.out.println("Trying to connect to port not in router");
		
		((Link) link).setConnector(this);
		
		for (int i = 0; i < _routingTable.length; i++) {
			RouteTableEntry routeTableEntry = _routingTable[i];
			if (routeTableEntry != null && ((Node)routeTableEntry.node()).getAddr().networkId() != oldID) {
				send(this, new RouterAdvertisement(_routingTable), 0);
			}
		}
		
		for (int i = 0; i < _routingTable.length; i++) {
			if (_routingTable[i] != null) {
				System.out.println("" + ((Node)_routingTable[i].node()).getAddr().networkId());
			}
		}
	}
	

	public int disconnectInterface(int networkaddress) {
		Link routerInterfaceLink = (Link) getInterface(networkaddress);
		int oldInterface = 0;
		for(int i = 0 ; i < _interfaces; i++){
			if(_routingTable[i] !=  null) {
				if(_routingTable[i].link() ==  routerInterfaceLink){ _routingTable[i] = null; oldInterface = i;}
			}
		}
		
		routerInterfaceLink.removeConnector(this);
		return oldInterface;
	}
	

	// This method searches for an entry in the routing table that matches
	// the network number in the destination field of a messages. The link
	// represents that network number is returned
	
	private SimEnt getInterface(int networkAddress)
	{
		SimEnt routerInterface=null;
		for(int i=0; i<_interfaces; i++)
			if (_routingTable[i] != null)
			{
				if (((Node) _routingTable[i].node()).getAddr().networkId() == networkAddress)
				{
					routerInterface = _routingTable[i].link();
				}
			}
		return routerInterface;
	}
	
	public void RouterAgentChange(Router router, int nodeNetworkID) {
		Link link = new Link();
		link.setConnector(this);
		link.setConnector(router);
		TableAgent.add(new AgentEntry(router, link, nodeNetworkID));
	}
	
	
	// When messages are received at the router this method is called
	
	public void recv(SimEnt source, Event event){
		if (event instanceof Message){
			//((Message) event).destination().setNetworkId(updatedInterface);
			System.out.println("Router handles packet with seq: " + ((Message) event).seq()+" from node: "+((Message) event).source().networkId()+"." + ((Message) event).source().nodeId() );
			SimEnt sendNext = getInterface(((Message) event).destination().networkId());
			System.out.println(((Message) event).destination().networkId());
			System.out.println("Router sends to node: " + ((Message) event).destination().networkId()+"." + ((Message) event).destination().nodeId());		
			send (sendNext, event, _now);
		}	
		
		
		//changeInterface event is triggered which changes the interface in the RouterTable.
		else if (event instanceof changeInterface) {		
			changeInterface temp = (changeInterface)event;	//Grabs the values from the event
			
			//Disconnect the receiver node from it's current position in the routerTable
			disconnectInterface(temp.getId());				
			
			connectInterface(temp.getInterface(), temp.getLink(), temp.getNode()); //Connects to the desired interface
			//updatedInterface = temp.getInterface();
			printRouterTable();
			
			
		}
	}
}
