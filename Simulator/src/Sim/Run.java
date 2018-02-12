package Sim;

// An example of how to build a topology and starting the simulation engine

public class Run {
	public static void main (String [] args)
	{
 		//Creates two links (maxDelay, lossProbability)
 		LossyLink link1 = new LossyLink(50, 30);
		LossyLink link2 = new LossyLink(30, 50);
		
		// Create two end hosts that will be
		// communicating via the router
		Node host1 = new Node(1,1);
		Node host2 = new Node(2,1);

		//Connect links to hosts
		host1.setPeer(link1);
		host2.setPeer(link2);

		// Creates as router and connect
		// links to it. Information about 
		// the host connected to the other
		// side of the link is also provided
		// Note. A switch is created in same way using the Switch class
		Router routeNode = new Router(2);
		routeNode.connectInterface(0, link1, host1);
		routeNode.connectInterface(1, link2, host2);
		
		int CBR = 5;
		// Generate some traffic
		// host1 will send 3 messages with time interval 5 to network 2, node 1. Sequence starts with number 1
		host1.StartSending(2, 2, 10000, CBR, 1); 
		// host2 will send 2 messages with time interval 10 to network 1, node 1. Sequence starts with number 10
		host2.StartSending(1, 1, 10000, CBR, 10); 
		
		// Start the simulation engine and of we go!
		Thread t=new Thread(SimEngine.instance());
	
		t.start();
		try
		{
			t.join();
		}
		catch (Exception e)
		{
			System.out.println("The motor seems to have a problem, time for service?");
		}		

		double totalSentPackets1 = host1.sentPackets(); //Sent packets for node 1
		double totalSentPackets2 = host2.sentPackets();	//Sent packets for node 2
		
		double totalRecievedPackets1 = host1.receivedPackets(); //Received packets for node 1
		double totalRecievedPackets2 = host2.receivedPackets(); //Received packets for node 2
		
		double dropped1 = totalSentPackets1 - totalRecievedPackets1; //Dropped packets for node 1
		double dropped2 = totalSentPackets2 - totalRecievedPackets2; //Dropped packets for node 2
		
		double averageDelay1 = (link1.totalDelay()) / totalRecievedPackets1; //Average delay Node 1
		double averageDelay2 = (link2.totalDelay()) / totalRecievedPackets2; //Average delay Node 2
		
		double averageJitter1 = (link1.totalJitter()) / totalRecievedPackets1; //Average jitter Node 1
		double averageJitter2 = (link2.totalJitter()) / totalRecievedPackets2; //Average jitter Node 2
		
		//RESULTS
		System.out.println("----------------------------");
		System.out.println("-Node 1 RESULTS-");
		System.out.println("----------------------------");
		System.out.println("Total sent packets: " + (int)totalSentPackets1 + "\nTotal packets received: " + (int)totalRecievedPackets1 + 
				"\nTotal lost packets: " + (int)dropped1 + "\nAverage delay: " + averageDelay1 + " ms" + "\nAverage jitter: " + averageJitter1 + " ms");
		System.out.println("----------------------------");
		System.out.println("----------------------------");
		System.out.println("-Node 2 RESULTS-");
		System.out.println("----------------------------");
		System.out.println("Total sent packets: " + (int)totalSentPackets2 + "\nTotal packets received: " + (int)totalRecievedPackets2 + 
				"\nTotal lost packets: " + (int)dropped2 + "\nAverage delay: " + averageDelay2 + " ms" + "\nAverage jitter: " + averageJitter2 + " ms");
		System.out.println("----------------------------");


	}
}
