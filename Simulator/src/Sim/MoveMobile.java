package Sim;

public class MoveMobile implements Event {
	
	Router oldRouter;
	Router newRouter;
	int newInterface;
	
	public MoveMobile(Router oldRouter, Router newRouter, int newInterface) {
		this.oldRouter = oldRouter;
		this.newRouter = newRouter;
		this.newInterface = newInterface;
	}
	
	public Router getOldRouter() {
		return this.oldRouter;
	}
	
	public Router getNewRouter() {
		return this.newRouter;
	}
	
	
	public int getNewInterface() {
		return this.newInterface;
	}

	@Override
	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
		
	}
	
	

}
