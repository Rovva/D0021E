package Sim;
import java.util.Random;

public class LossyLink extends Link{
	private SimEnt _connectorA=null;
	private SimEnt _connectorB=null;
	private int maxDelay;
	private int jitter;
	private int totalJitter;
	private int lossProb;
	private int previousDelay;
	private int totalDelay;
	private int totalPackets;
	private int totalDroppedPackets;
	
	/*
	 * @param maxDelay The maximum possible delay from  0-maxDelay.
	 * @param lossProb The probability that a packet is lost whilst traversing the link.
	 */
	public LossyLink(int maxDelay, int lossProb){
		super();	
		this.maxDelay = maxDelay;
		this.jitter = 0;
		this.totalJitter = 0;
		this.lossProb = lossProb;
		this.previousDelay = 0;
		this.totalPackets = 0;
		this.totalDroppedPackets = 0;


	}
	
	// Connects the link to some simulation entity like
	// a node, switch, router etc.
	
	public void setConnector(SimEnt connectTo){
		if (_connectorA == null) 
			_connectorA=connectTo;
		else
			_connectorB=connectTo;
	}

	// Called when a message enters the link
	
	public void recv(SimEnt src, Event ev){
		
		int tmpRandLoss = this.random(100); // Generate a random number from 0-100
		int tmpRandDelay = this.random(maxDelay); // Generate a random number from 0-maxDelay
			
		/*
		 * Checks whether the packet is lost or not
		 */
		if (tmpRandLoss >= lossProb){
			
			// Determine the jitter of the current transmission
			if (this.totalPackets > 0){
				this.jitter = Math.abs(tmpRandDelay - this.previousDelay);
				this.totalJitter += this.jitter;
			}
			
			this.totalDelay += tmpRandDelay;	
			this.previousDelay = tmpRandDelay; //Store the delay for the next jitter comparison
			this.totalPackets++;
			
			//Packet success
			if (ev instanceof Message){
				
				System.out.println("Link recv msg, passes it through");
				if (src == _connectorA){
					send(_connectorB, ev, tmpRandDelay);
				} else {
					send(_connectorA, ev, tmpRandDelay);
				}
			}
		} else {
			//Packet lost
			System.out.println("OH NO PACKET LOST! :O");
			this.totalDroppedPackets++;
		}

	}
	
	/*
	 * Random number from 0-max is returned.
	 */
	public int random (int max){
		Random rand = new Random();
		return rand.nextInt(max);
	}
	
	public float totalDelay(){
			return this.totalDelay;
	}
	
	public float totalJitter(){
			return this.totalJitter;
	}
}
