package client;

public class Test {

	public static void main(String[] args) {
		Blockchain blockchain = new Blockchain();

		Message m1 = new Message("Hello Blockchain");
		Block block1 = new Block(1, blockchain.getLatestBlock().hash());
		block1.addMessage(m1);
		System.out.println("Mining Block 1");
		blockchain.mine(block1);
		System.out.println("Done Mining Block 1");

		Message m2 = new Message("Hello Blockchain #2");
		Message m3 = new Message("Hello Blockchain #3");
		Message m4 = new Message("Hello Blockchain #3");
		Block block2 = new Block(2, blockchain.getLatestBlock().hash());
		block2.addMessage(m2);
		block2.addMessage(m3);
		block2.addMessage(m4);
		System.out.println("Mining Block 2");
		blockchain.mine(block2);
		System.out.println("Done Mining Block 2");

		System.out.println(blockchain);
	}

}
