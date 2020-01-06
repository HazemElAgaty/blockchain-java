package client;

import java.util.ArrayList;

public class Blockchain {
	// Not included
	static int difficulty = 4;
	static int maxNonce = (int) Math.pow(2, 32);
	static String target = new String(new char[difficulty]).replace('\0', '0');
	static Block genesisBlock = new Block(0, "0");
	static int maxBlockMessages = 5;

	private volatile ArrayList<ArrayList<Block>> chainBlocks;
	// Not included
	private volatile ArrayList<Message> messagesPool;

	public Blockchain() {
		this.chainBlocks = new ArrayList<ArrayList<Block>>();
		this.messagesPool = new ArrayList<Message>();

		ArrayList<Block> height0 = new ArrayList<Block>();
		height0.add(genesisBlock);
		this.chainBlocks.add(height0);
	}

	public ArrayList<Message> getMessagesPool() {
		return messagesPool;
	}

	public void addMessageToPool(Message message) {
		this.messagesPool.add(message);
	}

	public ArrayList<ArrayList<Block>> getChainBlocks() {
		return chainBlocks;
	}

	public boolean addBlock(Block block) {
		if (this.isBlockValid(block)) {
			int height = block.getBlockHeight();
			int previousBlockIndex = -1;
			for (int i = 0; i < this.chainBlocks.get(height - 1).size(); i++) {
				Block prevBlock = this.chainBlocks.get(height - 1).get(i);
				if (prevBlock.hash().equals(block.getPreviousBlockHash())) {
					previousBlockIndex = i;
					break;
				}
			}
			if (previousBlockIndex == -1) {
				System.out.println("Previous block hash not found along chain");
				return false;
			} else {
				block.setPreviousBlockIndex(previousBlockIndex);
				if(height >= this.chainBlocks.size()) {
					this.chainBlocks.add(new ArrayList<Block>());
				}
				this.chainBlocks.get(height).add(block);
				System.out.println("New Block added");
				System.out.println(this);
				return true;
			}
		} else {

			System.out.println("Block is invalid");
			return false;
		}
	}

	private boolean isBlockValid(Block block) {
		boolean powCorrect = block.hash().substring(0, difficulty).equals(Blockchain.target);
		return powCorrect;
	}

	public Block mine(Block block) {
		int maxBlockHeight = this.getChainBlocks().size();
		for (int i = 0; i < Blockchain.maxNonce; i++) {
			if (this.getChainBlocks().size() > maxBlockHeight) {
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
		int maxHeight = this.chainBlocks.size() - 1;
		return this.chainBlocks.get(maxHeight).get(0);
	}

	public ArrayList<Block> getLongestChain() {
		ArrayList<Block> longestChain = new ArrayList<Block>();
		Block latestBlock = this.getLatestBlock();
		longestChain.add(latestBlock);

		Block currentBlock = latestBlock;
		while (currentBlock.getBlockHeight() > 0) {
			currentBlock = this.chainBlocks.get(currentBlock.getBlockHeight() - 1).get(currentBlock.getPreviousBlockIndex());
			longestChain.add(currentBlock);
		}
		return longestChain;
	}

	public String toString() {
		String result = "";
		for (Block block : this.getLongestChain()) {
			result += "---------------------------------------------------------\n";
			result += "Hash: " + block.hash() + "\n";
			result += "Height: "+ block.getBlockHeight() + "\n";
			result += "Previous Hash: " + block.getPreviousBlockHash() + "\n";
			result += "Messages: " + block.getBlockMessages() + "\n";
		}
		result += "---------------------------------------------------------\n";
		return result;
	}

}
