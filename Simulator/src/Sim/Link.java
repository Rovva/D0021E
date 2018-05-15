package Sim;

// This class implements a link without any loss, jitter or delay

public class Link extends SimEnt{
	private SimEnt _connectorA=null;
	private SimEnt _connectorB=null;
	private int _now=0;
	
	public Link()
	{
		super();	
	}
	
	// Connects the link to some simulation entity like
	// a node, switch, router etc.
	
	public void setConnector(SimEnt connectTo)
	{
		if (_connectorA == null) 
			_connectorA=connectTo;
		else
			_connectorB=connectTo;
	}
	
	public void removeConnector(SimEnt peer) {
		if (_connectorA == peer) {
			_connectorA = null;
		} else if (_connectorB == peer) {
			_connectorB = null;
		}
	}

	// Called when a message enters the link
	
	public void recv(SimEnt src, Event ev)
	{
		if (ev instanceof Message) {
			System.out.println("Link recv msg, passes it through");
			if (src == _connectorA){
				send(_connectorB, ev, _now);
			} else {
				send(_connectorA, ev, _now);
			}
		} 
		if (ev instanceof changeInterface) {
			if (src == _connectorA){
				send(_connectorB, ev, _now);
			} else {
				send(_connectorA, ev, _now);
			}
		}
		if (ev instanceof notifySender) {
			if (src == _connectorA){
				send(_connectorB, ev, _now);
			} else {
				send(_connectorA, ev, _now);
			}
		}
	}	
}