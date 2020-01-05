package utils;

import client.Block;
import client.Message;

public class Transformer {
	
	public static Message stringArrayToMessage(String [] data) {
		return new Message(data[1]);
	}
	
	public static String blockToString(Block block) {
		// Add WS MSG Type
		String encodedBlock = "Block";
		// Add Hash
		encodedBlock += "#"+ block.hash();
		// Add Previous Hash
		encodedBlock += "#"+ block.getPreviousBlockHash();
		// Add Block Height
		encodedBlock += "#"+ block.getBlockHeight();
		// Add Block Nonce
		encodedBlock += "#"+ block.getNonce();
		// Add Block Messages
		encodedBlock += "#";
		for(Message message : block.getBlockMessages()) {
			encodedBlock += message.getText() + ",";
		}
		// Remove last comma
		return encodedBlock.substring(0, encodedBlock.length() - 1) + "\n";
	}
	
	public static Block stringArrayToBlock(String [] data) {
		String previousHash = data[2];
		int height = Integer.parseInt(data[3]);
		int nonce = Integer.parseInt(data[4]);
		String [] messages = data[5].split(",");
		
		Block block = new Block(height, previousHash);
		block.setNonce(nonce);
		for(String message : messages) {
			block.addMessage(new Message(message));
		}
		return block;
	}
}
