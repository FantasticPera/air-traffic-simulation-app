package model;

public class IdGenerator {

	private volatile int id = 1;
	
	public synchronized int generateID() {
		return id++;
	}
	
}
