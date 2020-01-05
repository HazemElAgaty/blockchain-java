package client;

import utils.SHA256;

public class Message {
	private String text;

	public Message(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public String hash() {
		return SHA256.hash(this.text);
	}
	
	public String toString() {
		return this.text;
	}
}
