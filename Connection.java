
public class Connection {
	private String origin;
	private String gate;
	private String destination;
	
	public Connection(String originCity, String g, String finalCity) {
		this.origin = originCity;
		this.gate = g;
		this.destination = finalCity;
	}
	
	public String toString() {
		return (this.origin + " --> " + this.gate + " --> " + this.destination);
	}
}
