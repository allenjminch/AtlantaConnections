import java.util.*;

public class SortByTime implements Comparator<Route> {
	
	public int compare(Route r1, Route r2) {
		return r1.getTime().compare(r2.getTime());
	}

}
