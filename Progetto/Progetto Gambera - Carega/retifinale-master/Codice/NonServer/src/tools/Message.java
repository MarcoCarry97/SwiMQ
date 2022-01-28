package tools;

public class Message {
	private String topic;
	private String body;

	public Message(String topic, String body) {
		this.topic = topic;
		this.body = body;
	}

	public String getTopic() {
		return this.topic;
	}

	public String getBody() {
		return this.body;
	}

}
