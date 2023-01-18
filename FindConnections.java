
import java.util.*;
import java.io.*;

public class FindConnections {
	private static TreeMap<String, ArrayList<Route>> gateData;
	private static String city;
	private static String gate;
	private static String time;
	static int pairsCount;
	
	public static Time converted(String s) {
		int colonIndex = 0;
		int mIndex = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == ':') {
				colonIndex = i;
			} if (s.charAt(i) == 'M') {
				mIndex = i;
			}
		}
		String hour = s.substring(0, colonIndex);
		String minute = s.substring(colonIndex + 1, colonIndex + 3);
		if (s.charAt(mIndex - 1) == 'P') {
			if (toInt(hour) == 12) {
				return new Time(toInt(hour), toInt(minute));
			} else {
				return new Time(toInt(hour) + 12, toInt(minute));
			}
		} else if (s.charAt(mIndex - 1) == 'A') {
			if (toInt(hour) == 12) {
				return new Time(0, toInt(minute));
			} else {
				return new Time(toInt(hour), toInt(minute));
			}
		} else {
			return new Time();
		}
	}
	
	/**
	 * Determines whether or not a string represents a positive integer.
	 * @param s - a string.
	 * @return true if the string represents an integer (e.g., "345"), false if it doesn't (e.g., "2rs9")
	 */
	public static boolean isNumeric(String s) {
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			/* if any character of the string is not a digit, this string does not represent a positive integer;
			thus, in such a scenario, return false */
			if (!Character.isDigit(ch)) {
				return false;
			}
		}
		/* if we are here, it means that all characters of the string must have been digits,
		so return true, given that this string does represent an integer */
		return true;
	}
	
	/**
	 * For a string with digits, converts this string to the corresponding integer. Returns 0 as a placeholder if the string
	 doesn't represent an integer. This method is useful in this PA for extracting invoice numbers from the job completion lines of the work log.
	 * @param s - a string.
	 * @return the integer that this string represents if it contains entirely digits (e.g., "345" would return 345); 0 otherwise
	 */
	public static int toInt(String s) {
		// initialize the integer to be computed to 0
		int integer = 0;
		// if the string does not represent an integer, we want to return 0, so just return integer
		if (!isNumeric(s)) {
			return integer;
		} else {
			// loop through the characters of the string to get its digits
			for (int i = 0; i < s.length(); i++) {
				// extract the digit at index i by subtracting the 0 character from the character at this digit
				int digit = s.charAt(i) - '0';
				/* recognizing that a positive integer is a sum of each of its digits times consecutive powers of 10,
				multiply the digit at character i by the proper power of 10 to increment integer up toward its final value */
				integer += digit * Math.pow(10, s.length() - i - 1);
			}
			/* having added all the digits times corresponding powers of 10 to integer, integer is now the positive integer
			we are seeking; return it */
			return integer;
		}
	}
	
	
	public static Route processLine(String line) {
		boolean cityExtracted = false;
		boolean gateExtracted = false;
		boolean timeExtracted = false;
		for (int i = 0; i < line.length(); i++) {
			char c1 = line.charAt(i);
			char c2 = line.charAt(i + 1);
			if (Character.isLetter(c1) && Character.isDigit(c2)) {
				if (!gateExtracted) {
					gate = "";
					gate += c1;
					int j = 1;
					while (Character.isDigit(line.charAt(i + j))) {
						gate += line.charAt(i + j);
						j++;
					}
					gateExtracted = true;
				}
			} else if (Character.isLetter(c1) && Character.isLetter(c2)) {
				if (!cityExtracted) {
					city = "";
					int k = 0;
					boolean whitespaces = false;
					boolean comma = false;
					char neighbor1 = c1;
					char neighbor2 = c2;
					boolean done = false;
					while (!done) {
						city += neighbor1;
						k++;
						neighbor1 = line.charAt(i + k);
						neighbor2 = line.charAt(i + k + 1);
						whitespaces = (neighbor1 == ' ' && neighbor2 == '?');
						comma = (neighbor1 == ',');
						done = whitespaces || comma;
					}
					cityExtracted = true;
				}
			} else if (line.charAt(i - 1) == ' ') {
				boolean condition1 = Character.isDigit(c1) && Character.isDigit(c2);
				boolean condition2 = Character.isDigit(c1) && c2 == ':';
				time = "";
				if (condition1) {
					time = line.substring(i, i + 8);
					timeExtracted = true;
				} else if (condition2) {
					time = line.substring(i, i + 7);
					timeExtracted = true;
				}
			} if (cityExtracted && gateExtracted && timeExtracted) {
				if (Character.isDigit(line.charAt(1))) {
					return new Route(city, gate, -1, converted(time));
				} else {
					return new Route(city, gate, 1, converted(time));
				}
			}
		}
		return new Route();
	}
	
	public static void processFile() throws FileNotFoundException {
		Scanner input = new Scanner(new File("Delta A321 Schedule for Atlanta Jan 7 2022.txt"));
		gateData = new TreeMap<String, ArrayList<Route>>();
		while (input.hasNextLine()) {
			String line = input.nextLine();
			Route data = processLine(line);
			boolean gateAlreadyFound = gateData.containsKey(data.getGate());
			if (!gateAlreadyFound) {
				gateData.put(data.getGate(), new ArrayList<Route>());
			}
			gateData.get(data.getGate()).add(data);
		}
	}
	
	public static Time max(Time t1, Time t2) {
		if (t1.compare(t2) >= 0) {
			return t1;
		} else {
			return t2;
		}
	}
	
	public static Time min(Time t1, Time t2) {
		if (t1.compare(t2) <= 0) {
			return t1;
		} else {
			return t2;
		}
	}
	
	public static void processData() {
		pairsCount = 0;
		for (String g: gateData.keySet()) {
			ArrayList<Route> routes = gateData.get(g);
			Collections.sort(routes, new SortByTime());
			ArrayList<Route> unmatched = new ArrayList<Route>();
			int start = 0;
			if (routes.get(0).getDiscriminant() == -1) {
				unmatched.add(routes.get(0));
				start = 1;
			}
			for (int i = start; i < routes.size() - 1; i++) {
				Route route1 = routes.get(i);
				Route route2 = routes.get(i + 1);
				int identifier1 = route1.getDiscriminant();
				int identifier2 = route2.getDiscriminant();
				if (identifier1 == 1) {
					if (identifier2 == 1) {
						unmatched.add(route1);
					} else {
						Time time1 = route1.getTime();
						Time time2 = route2.getTime();
						if (time2.subtract(time1).compare(new Time(2, 45)) < 0) {
							System.out.println(new Connection(route1.getCity(), g, route2.getCity()));
							pairsCount++;
						} else {
							unmatched.add(route1);
							unmatched.add(route2);
						}
					}
				} else {
					if (routes.get(i - 1).getDiscriminant() == -1) {
						unmatched.add(route1);
					}
				}
			}
			if (routes.get(routes.size() - 1).getDiscriminant() == 1) {
				unmatched.add(routes.get(routes.size() - 1));
			}
			gateData.put(g, unmatched);
		}
	}


	public static void main(String[] args) throws FileNotFoundException {
		processFile();
		processData();
		System.out.println(pairsCount);
		for (String x: gateData.keySet()) {
			System.out.println(gateData.get(x));
		}
	}
}
