package Sim;

import java.util.Collections;

// An example of how to build a topology and starting the simulation engine

public class Run {
	public static void main (String [] args)
	{
 		Link link1 = new Link();
		Link link2 = new Link();
		Link link3 = new Link();
		Link link4 = new Link();
		
 		//Creates two links (maxDelay, lossProbability)
 		//LossyLink link1 = new LossyLink(50, 0);
		//LossyLink link2 = new LossyLink(30, 0);
		
		// Create two end hosts that will be
		// communicating via the router
		Node host1 = new Node(1,1);
		Node host2 = new Node(1,2);

		//Connect links to hosts
		host1.setPeer(link1);
		host2.setPeer(link2);

		// Creates as router and connect
		// links to it. Information about 
		// the host connected to the other
		// side of the link is also provided
		// Note. A switch is created in same way using the Switch class
		Router router1 = new Router(1, 5);
		Router router2 = new Router(2, 5);
		
		host1.setHomeAgent(router1);
		host2.setHomeAgent(router1);
		
		
		router1.connectInterface(0, link1, host1);
		router1.connectInterface(1, link2, host2);
		router1.connectInterface(2, link3, router2);
		router2.connectInterface(2, link3, router1);
		
		int changeInterface = 4;	//The interface to change to when we order a changeInterface Event.
		int afterMessages = 5;		//After how many messages the changeInterface Event should be triggered.
		
		//Change to interface 4 after 5 messages
		//host2.changeInterfaceCounter(afterMessages, changeInterface);	//Change interface for Host 2 and update the router
		//host1.changeToNetwork(afterMessages, changeInterface);			//after the router is changed... change the senders "toNetwork" value
				
		// Generate some traffic
		// host1 will send 20 messages with time interval 5 to network 2, node 1. Sequence starts with number 1. Generator is CBR.
		host1.StartSending(1, 2, 20, "CBR", 1, 5); 
		
		host2.moveMobileNode(router1, router2, 3, 50);
		
		//Use Poisson with Lambda 1
		//host2.StartSending(1, 1, 1000, "Poisson", 10, 1); 
		
		//host1.moveNode(2);
		//(int oldInterface, int newInterface, Link, Node)
		//routeNode.changeInterface(0, 1, link1, host1);
		
		//Use Gaussian with mean 20 and deviation 5
		//host1.StartSending(1, 1, 1000, "Gaussian", 10, 20, 5); 
		
		
		
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
		
		double averageDelay = 0.0;
		double averagejitter = 0.0;
		
		for(int i = 0; i < host1.receivedDelay.size(); i++) {
			averageDelay = averageDelay + host1.receivedDelay.get(i);
		}
		
		averageDelay = averageDelay / host1.receivedDelay.size();
		
		
		for(int i = 0; i <= host1.receivedDelay.size(); i++) {
			if(i+1 >= host1.receivedDelay.size()) {
				break;
			} else {
				averagejitter = averagejitter + Math.abs(host1.receivedDelay.get(i+1) - host1.receivedDelay.get(i));
			}
		}
		
		averagejitter = averagejitter / host1.receivedDelay.size();
		
		System.out.println("Node 1 results (using: " + host1.returnGenerator() + " generator): ");
		System.out.println("Total sent packets: " + host1.sentDelay.size());
		System.out.println("Total received packets: " + host1.receivedDelay.size());
		System.out.println("Average delay: " + averageDelay + " ms");
		System.out.println("Average jitter: " + averagejitter + " ms");
		
		averageDelay = 0.0;
		
		for(int i = 0; i < host2.receivedDelay.size(); i++) {
			averageDelay = averageDelay + host2.receivedDelay.get(i);
		}
		
		averageDelay = averageDelay / host2.receivedDelay.size();
		
		averagejitter = 0.0;
		
		for(int i = 0; i <= host2.receivedDelay.size(); i++) {
			if(i+1 >= host2.receivedDelay.size()) {
				break;
			} else {
				averagejitter = averagejitter + Math.abs(host2.receivedDelay.get(i+1) - host2.receivedDelay.get(i));
			}
		}
		
		averagejitter = averagejitter / host2.receivedDelay.size();
		
		System.out.println("----------------");
		System.out.println("Node 2 results (using: " + host1.returnGenerator() + " generator): ");
		System.out.println("Total sent packets: " + host2.sentDelay.size());
		System.out.println("Total received packets: " + host2.receivedDelay.size());
		System.out.println("Average delay: " + averageDelay + " ms");
		System.out.println("Average jitter: " + averagejitter + " ms");
		System.out.println("----------------");
		System.out.println("HOST 1");
		System.out.println("----------------");
		
		Collections.sort(host1.sentDelay);
		int max = 0;
		
		//Checks for the largest integer to decide how big the array should be
		for(int i = 0; i < host1.sentDelay.size(); i++) {
			if(host1.sentDelay.get(i) >= max) {
				max = host1.sentDelay.get(i);
			}
		
		}
		
		int[] array = new int[max+1];
		int currentNumber = 0;
		
		//Primary loop to count how many of a certain integer occurs (For plotting purposes)
		for (int i = 0; i < host1.sentDelay.size(); i++) {
			
			if(host1.sentDelay.get(i) == currentNumber) {
				array[currentNumber] = array[currentNumber] + 1;

			} else {
				currentNumber++;
			}
		}
		
		currentNumber = 0;
		
		//Writes out the result from the calculations above.
		for(int i = 0; i < array.length; i++) {
			currentNumber = 0;
			for(int y = 0; y < array[i]; y++) {
				currentNumber++;
			}
			System.out.println(i + " " + currentNumber);
		}
		/*
		System.out.println("----------------");
		System.out.println("HOST 2");
		System.out.println("----------------");
		
		for (int i = 0; i < host2.sentDelay.size(); i++) {
			//System.out.println("Message # " + (i+1) + ": " + (host2.receivedDelay.get(i) - host2.sentDelay.get(i)) + " ms");
			System.out.println((host2.sentDelay.get(i)));
		}
		
		//System.out.println(host1.receivedDelay.get(1).toString());
		
		//averageDelay = averageDelay/totalSentPackets1;
		
		
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
		*/

	}
}
