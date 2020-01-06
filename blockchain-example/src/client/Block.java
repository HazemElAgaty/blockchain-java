package client;

import java.util.ArrayList;

import utils.SHA256;

public class Block {
	private int nonce = 0;
	private ArrayList<Message> blockMessages;
	private int blockHeight;
	private String previousBlockHash;
	private int previousBlockIndex = 0;

	public Block(int blockHeight, String previousBlockHash) {
		this.blockHeight = blockHeight;
		this.blockMessages = new ArrayList<Message>();
		this.previousBlockHash = previousBlockHash;
	}

	public int getNonce() {
		return nonce;
	}

	public void setNonce(int nonce) {
		this.nonce = nonce;
	}

	public String getPreviousBlockHash() {
		return previousBlockHash;
	}

	public void setPreviousBlockHash(String previousBlockHash) {
		this.previousBlockHash = previousBlockHash;
	}

	public ArrayList<Message> getBlockMessages() {
		return blockMessages;
	}

	public int getBlockHeight() {
		return blockHeight;
	}

	public String hash() {
		String messagesHashes = "";
		for (Message message : this.blockMessages) {
			messagesHashes += message.hash();
		}
		return SHA256.hash(this.previousBlockHash + this.blockHeight + messagesHashes + this.nonce);
	}

	public void addMessage(Message message) {
		this.blockMessages.add(message);
	}

	public int getPreviousBlockIndex() {
		return previousBlockIndex;
	}

	public void setPreviousBlockIndex(int previousBlockIndex) {
		this.previousBlockIndex = previousBlockIndex;
	}
}
