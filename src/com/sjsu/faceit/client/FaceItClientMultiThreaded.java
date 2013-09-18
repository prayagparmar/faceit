package com.sjsu.faceit.client;

import java.io.IOException;

import javax.xml.transform.TransformerException;

public class FaceItClientMultiThreaded{

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	
	public static void main(String[] args) throws InterruptedException {
		long startTime = System.currentTimeMillis();
		new ClientThread();
		long stopTime = System.currentTimeMillis();
		System.out.println("Total Time: " + (stopTime - startTime)/1000 + "sec" );
	}

	

}

class ClientThread implements Runnable{
	
	Thread t;
	ClientThread() throws InterruptedException {
	      // Create a new, second thread
	      t = new Thread(this, "Demo Thread");
	      System.out.println("Child thread: " + t);
	      t.start(); // Start the thread
	      
	}
	
	@Override
	public void run() {
	     for(int i = 10; i > 0; i--) {
	    	 try {
	    		System.out.println("Calling Login: " + i); 
				FaceItClient.loginUser();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			}
	     }
		
	}
}
