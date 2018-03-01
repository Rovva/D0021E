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
	private int _timeBetweenSending = 10; //time between messages
	private int _toNetwork = 0;
	private int _toHost = 0;
	private int timeInterval;	//Constant Bit Rate
	private int mean;			//Mean value for Poisson/Gaussian Generators
	private int lambda;
	private int deviation;		//Deviation for Gaussian
	public int interfaceCounter;
	private int whichInterface;
	private String generator;	//"CBR", "Gaussian" or "Poisson"
	public ArrayList<Integer> receivedDelay = new ArrayList<Integer>();	//If needed, keeps track of when a node receives the item.
	public ArrayList<Integer> sentDelay = new ArrayList<Integer>();		//Stores all the packet delays before they are being sent.
	
	
	/*
	 * @param generator The generator to be used: "CBR" or "Poisson"
	 * @param cbrOrMean The CBR value if "CBR" generator OR Lambda value if "Poisson"
	 */
	
	public void StartSending(int network, int node, int number, String generator, int startSeq, int cbrOrLambda)
	{
		//Check if generator is CBR or Poisson.
		if (generator == "CBR") {
			this.timeInterval = cbrOrLambda;	//CBR value
		} else if (generator == "Poisson") {
			this.lambda = cbrOrLambda; 			//Lambda value. 
		}
		this.generator = generator;
		_stopSendingAfter = number;
		_toNetwork = network;
		_toHost = node;
		_seq = startSeq;
		send(this, new TimerEvent(),0);	
	}
	
	
	/*
	 * @param generator Generator to be used. This constructor accepts: "Gaussian"
	 * @param mean Mean value
	 * @param deviation Deviation value
	 */
	public void StartSending(int network, int node, int number, String generator, int startSeq, int mean, int deviation)
	{
		if (generator == "Gaussian") {
			this.mean = mean;			
			this.deviation = deviation;
		} else {
			System.out.println("Remove deviation parameter if you want to use CBR!");
		}
		this.generator = generator;
		_stopSendingAfter = number;
		_toNetwork = network;
		_toHost = node;
		_seq = startSeq;
		send(this, new TimerEvent(),0);	
	}
	
	public int guassianSendNext(double mean, double deviation) {
		Random rand = new Random();
		return (int)(rand.nextGaussian() * deviation + mean);
	}
	
	// https://stackoverflow.com/questions/1241555/algorithm-to-generate-poisson-and-binomial-random-numbers
	public int poissonSendNext(double lambda) {
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
					_timeBetweenSending = guassianSendNext(this.mean, this.deviation); //Gaussian
				} else if (generator == "Poisson") {
					_timeBetweenSending = poissonSendNext(this.lambda); //Poisson
				} else {
					_timeBetweenSending = timeInterval; //CBR value
				}
				sentDelay.add(_timeBetweenSending);		//Store the delay of the packet.
				_sentmsg++;
				send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost),_seq),0);
				send(this, new TimerEvent(),_timeBetweenSending);
				_seq++;
			}
		}
		if (ev instanceof Message)
		{
			this.receivedPackets++;
			System.out.println("RECEIVEDPACKETS: " + this.receivedPackets);
			System.out.println("INTERFACECOUNTER: " + this.interfaceCounter);
			if(this.receivedPackets == this.interfaceCounter) {
				moveNode(this.whichInterface);
			}
			int time = (int)SimEngine.getTime();
			receivedDelay.add(time);
			int previousMessageTime = this.lastMessageTime;
			int currentDelay = (int)SimEngine.getTime() - previousMessageTime;
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
	
	public String returnGenerator() {
		return this.generator;
	}
	
	public void changeInterfaceCounter(int count, int whichInterface) {
		
		this.interfaceCounter = count;
		this.whichInterface = whichInterface;
	}
	
	/*
	 * @param newNetworkId The new network to change to.
	 */
	public void moveNode(int newNetworkId){
		this._id.setNetworkId(newNetworkId);
		_toNetwork = newNetworkId;
		System.out.println("BALLS");

		send (_peer, new changeInterface(whichInterface, (Link)_peer, this), 0);
		
	}
}
