package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import utils.Transformer;

public class WSHandler extends Thread {
	final InputStreamReader inputStream; 
    final OutputStream outputStream; 
    final Socket socket;
    final Blockchain blockchain;
    
    public WSHandler(Socket socket, InputStreamReader inputStream, OutputStream outputStream, Blockchain blockchain)  
    { 
        this.socket = socket; 
        this.inputStream = inputStream; 
        this.outputStream = outputStream; 
        this.blockchain = blockchain;
    } 
    
	public void run() {
		while(true) {
			try {
				System.out.println("Running");
				String notification = new BufferedReader(inputStream).readLine();
				String [] notificationData = notification.split("#");
				if(notificationData[0].equals("Message")) {
					Message newMessage = Transformer.stringArrayToMessage(notificationData);
					this.blockchain.addMessageToPool(newMessage);
				}
				else if(notificationData[0].equals("Block")) {
					Block block = Transformer.stringArrayToBlock(notificationData);
					this.blockchain.addBlock(block);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
