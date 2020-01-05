package client;

import java.util.ArrayList;

public class Blockchain {
	static int difficulty = 4;
	static int maxNonce = (int) Math.pow(2, 32);
	static String target = new String(new char[difficulty]).replace('\0', '0');
	static Block genesisBlock = new Block(0, "0");
	static int maxBlockMessages = 5;
	
	private volatile ArrayList<Block> chainBlocks;
	private volatile ArrayList<Message> messagesPool;
	
	public Blockchain() {
		this.chainBlocks = new ArrayList<Block>();
		this.messagesPool = new ArrayList<Message>();
		this.chainBlocks.add(genesisBlock);
	}
	
	public Blockchain(ArrayList<Block> chainBlocks) {
		this.chainBlocks = chainBlocks;
		this.messagesPool = new ArrayList<Message>();
	}
	
	public ArrayList<Message> getMessagesPool() {
		return messagesPool;
	}

	public void addMessageToPool(Message message) {
		this.messagesPool.add(message);
	}
	
	public ArrayList<Block> getChainBlocks() {
		return chainBlocks;
	}

	public void addBlock(Block block) {
		if (this.isBlockValid(block)) {
			chainBlocks.add(block);
		}
	}

	private boolean isBlockValid(Block block) {
		boolean previousHashCorrect = block.getPreviousBlockHash().equals(this.getLatestBlock().hash());
		boolean powCorrect = block.hash().substring(0, difficulty).equals(Blockchain.target);
		return previousHashCorrect && powCorrect;
	}

	public Block mine(Block block) {
		int maxBlockHeight = this.getChainBlocks().size();
		for (int i = 0; i < Blockchain.maxNonce; i++) {
			if(this.getChainBlocks().size() > maxBlockHeight) {
				// New Block added
				System.out.println("New block is added while mining");
				break;
			}
			block.setNonce(i);
			String hash = block.hash();
			if (hash.substring(0, difficulty).equals(Blockchain.target)) {
				return block;
			}
		}
		return null;
	}
	
	public Block getLatestBlock() {
		int length = this.chainBlocks.size();
		return this.chainBlocks.get(length - 1);
	}
	
	public String toString() {
		String result = "";
		for(Block block : this.chainBlocks) {
			result+= "---------------------------------------------------------\n";
			result += "Hash: " + block.hash() + "\n";
			result += "Previous Hash: " + block.getPreviousBlockHash() + "\n";
			result += "Messages: " + block.getBlockMessages() +"\n"; 
		}
		result+= "---------------------------------------------------------\n";
		return result;
	}

}
