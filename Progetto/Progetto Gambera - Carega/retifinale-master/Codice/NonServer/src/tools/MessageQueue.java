package tools;

import java.util.ArrayList;

public class MessageQueue {
	protected ArrayList<Message> queue;
	protected int numeroMessaggi;

	public MessageQueue() {
		this.queue = new ArrayList<Message>();
		this.numeroMessaggi = 0;
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public boolean isFull() {
		return queue.size() >= numeroMessaggi;
	}

	public synchronized void send(Message m) {
		while (isFull()) {
			try {
				System.out.println("FULL LOCK");
				wait();
			} catch (InterruptedException ex) {
				System.out.println("Interrupted Exception");
			}
		}
		// Coda non piena
		queue.add(m);
		notifyAll();
	}

	public synchronized Message receive() {
		while (isEmpty()) {
			try {
				System.out.println("EMPTY LOCK");
				wait();
			} catch (InterruptedException ex) {
				System.out.println("Interrupted exception");
			}
		}
		// Coda non vuota
		Message messaggio = queue.remove(0);
		notifyAll();
		return messaggio;
	}
}
