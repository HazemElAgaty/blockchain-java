package client;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import utils.Transformer;

public class Client {

	private void start() {
		try {
			Blockchain blockchain = new Blockchain();
			Socket socket = new Socket("127.0.0.1", 4444);
			InputStreamReader inputStream = (new InputStreamReader(socket.getInputStream(), "UTF8"));
			OutputStream outputStream = socket.getOutputStream();
			WSHandler wsHandler = new WSHandler(socket, inputStream, outputStream, blockchain);
			wsHandler.start();

			// Mining Loop
			while (true) {
				int poolSize = blockchain.getMessagesPool().size();
				if (poolSize >= Blockchain.maxBlockMessages) {
					Block latestBlock = blockchain.getLatestBlock();
					int currentHeight = latestBlock.getBlockHeight() + 1;
					Block newBlock = new Block(currentHeight, latestBlock.hash());
					// Fill block
					for(int i = 0; i < Blockchain.maxBlockMessages; i++) {
						newBlock.addMessage(blockchain.getMessagesPool().get(i));
					}
					System.out.println("<<<<<<<Mining>>>>>>");
					Block minedBlock = blockchain.mine(newBlock);
					if (minedBlock != null) {
						System.out.println("Block Successfully Mined!!");
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
