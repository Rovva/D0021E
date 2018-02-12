package Sim;

import java.util.ArrayList;
import java.util.Random;

// This class implements a node (host) it has an address, a peer that it communicates with
// and it count messages send and received.

public class Node extends SimEnt {
	private NetworkAddr _id;
	private SimEnt _peer;
	private int _sentmsg=0;
	private int _seq = 0;
	private int totalDelay;
	private int receivedPackets;
	private int lastMessageTime;

	
	public Node (int network, int node)
	{
		super();
		_id = new NetworkAddr(network, node);
		this.totalDelay = 0;
	}	
	
	
	// Sets the peer to communicate with. This node is single homed
	
	public void setPeer (SimEnt peer)
	{
		_peer = peer;
		
		if(_peer instanceof Link )
		{
			 ((Link) _peer).setConnector(this);
		}
	}
	
	
	public NetworkAddr getAddr()
	{
		return _id;
	}
	
//**********************************************************************************	
	// Just implemented to generate some traffic for demo.
	// In one of the labs you will create some traffic generators
	
	private int _stopSendingAfter = 0; //messages
	private double _timeBetweenSending = 10; //time between messages
	private int _toNetwork = 0;
	private int _toHost = 0;
	private int timeInterval;
	private String generator;
	public ArrayList<Object> receivedDelay = new ArrayList<Object>();
	public ArrayList<Object> sentDelay = new ArrayList<Object>();
	
	public void StartSending(int network, int node, int number, String generator, int startSeq, int cbrInterval)
	{
		if (generator == "CBR") {
			timeInterval = cbrInterval;
		}
		this.generator = generator;
		_stopSendingAfter = number;
		_toNetwork = network;
		_toHost = node;
		_seq = startSeq;
		send(this, new TimerEvent(),0);	
	}
	
	public double guassianSendNext(double mean, double deviation) {
		Random rand = new Random();
		return (rand.nextGaussian() * deviation + mean);
	}
	
	// https://stackoverflow.com/questions/1241555/algorithm-to-generate-poisson-and-binomial-random-numbers
	public double poissonSendNext(double lambda) {
		double L = Math.exp(-lambda);
		  double p = 1.0;
		  int k = 0;

		  do {
		    k++;
		    p *= Math.random();
		  } while (p > L);

		  return k - 1;
		
	}
	

	
//**********************************************************************************	
	
	// This method is called upon that an event destined for this node triggers.
	
	public void recv(SimEnt src, Event ev)
	{
		if (ev instanceof TimerEvent)
		{			
			if (_stopSendingAfter > _sentmsg)
			{
				
				if (generator == "Gaussian") {
					_timeBetweenSending = guassianSendNext(5.1, 1.1);
				} else if (generator == "Poisson") {
					_timeBetweenSending = poissonSendNext(5.1);
				} else {
					_timeBetweenSending = timeInterval;
				}
				
				_sentmsg++;
				send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost),_seq),0);
				send(this, new TimerEvent(),_timeBetweenSending);
				//System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +" sent message with seq: "+_seq + " at time "+SimEngine.getTime());
				double time = SimEngine.getTime();
				receivedDelay.add(time);
				_seq++;
			}
		}
		if (ev instanceof Message)
		{
			this.receivedPackets++;
			/*
			System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +" receives message with seq: "+((Message) ev).seq() + 
					" at time "+SimEngine.getTime());
			*/
			double time = SimEngine.getTime();
			receivedDelay.add(time);
			int previousMessageTime = this.lastMessageTime;
			int currentDelay = (int)SimEngine.getTime() - previousMessageTime;
			System.out.println("");
			//System.out.println("Current jitter for Node "+_id.networkId()+ "." + _id.nodeId() +": " + currentDelay);
			System.out.println("");
			this.lastMessageTime = (int)SimEngine.getTime();
			this.totalDelay += currentDelay;
			
		}
	}
	
	public int sentPackets() {
		return this._sentmsg;
	}
	
	public int receivedPackets() {
		return this.receivedPackets;
	}
	
	public int totalDelay() {
		return this.totalDelay;
	}
}
