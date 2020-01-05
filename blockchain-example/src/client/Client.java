package client;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import utils.Transformer;

public class Client {

	private void start() {
		try {
			Blockchain blockchain = new Blockchain();
			InetAddress ip = InetAddress.getByName("localhost");
			Socket socket = new Socket(ip, 4444);
			InputStreamReader inputStream = (new InputStreamReader(socket.getInputStream(), "UTF8"));
			OutputStream outputStream = socket.getOutputStream();
			WSHandler wsHandler = new WSHandler(socket, inputStream, outputStream, blockchain);
			wsHandler.start();

			// Mining Loop
			while (true) {
				int poolSize = blockchain.getMessagesPool().size();
				if (poolSize >= Blockchain.maxBlockMessages) {
					Block latestBlock = blockchain.getLatestBlock();
					Block newBlock = new Block(0, latestBlock.hash());
					// Fill block
					for(int i = 0; i < Blockchain.maxBlockMessages; i++) {
						newBlock.addMessage(blockchain.getMessagesPool().get(i));
					}
					System.out.println("<<<<<<<Mining>>>>>>");
					Block minedBlock = blockchain.mine(newBlock);
					if (minedBlock != null) {
						String encodedBlock = Transformer.blockToString(minedBlock);
						outputStream.write(encodedBlock.getBytes());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.start();
	}

}
