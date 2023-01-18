
public class Route {
	private String otherCity;
	private String gate;
	private int discriminant;
	private Time time;
	
	public Route() {
		this.otherCity = "";
		this.gate = "";
		this.discriminant = 0;
		this.time = new Time();
	}
	
	
	public Route(String cityName, String g, int i, Time t) {
		this.otherCity = cityName;
		this.gate = g;
		this.discriminant = i;
		this.time = t;
	}
	
	public String getCity() {
		return this.otherCity;
	}
	
	public String getGate() {
		return this.gate;
	}
	
	public int getDiscriminant() {
		return this.discriminant;
	}
	
	public Time getTime() {
		return this.time;
	}
	
	public String type() {
		if (this.discriminant == 1) {
			return "arrival";
		} else if (this.discriminant == -1) {
			return "departure";
		} else {
			return "";
		}
	}
	
	public String toString() {
		return this.otherCity + " " + this.gate + " " + this.type() + " " + this.time;
	}
}
