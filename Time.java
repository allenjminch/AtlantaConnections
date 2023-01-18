
public class Time {
	private int hour;
	private int minute;
	
	public Time() {
		this.hour = 0;
		this.minute = 0;
	}
	
	public Time(int hour, int minute) {
		if (this.hour < 0 || this.hour > 23) {
			throw new IllegalArgumentException();
		} else if (this.minute < 0 || this.minute > 59) {
			throw new IllegalArgumentException();
		}
		this.hour = hour;
		this.minute = minute;
	}
	
	public String toString() {
		boolean zeroForHours = (this.hour < 10);
		boolean zeroForMinutes = (this.minute < 10);
		if (zeroForHours) {
			if (zeroForMinutes) {
				return "0" + this.hour + ":" + "0" + this.minute;
			} else {
				return "0" + this.hour + ":" + this.minute;
			}
		} else {
			if (zeroForMinutes) {
				return this.hour + ":" + "0" + this.minute;
			} else {
				return this.hour + ":" + this.minute;
			}
		}
	}
	
	public int getHour() {
		return this.hour;
	}
	
	public int getMinute() {
		return this.minute;
	}
	
	public Time add(Time other) {
		int newMinute = this.minute + other.getMinute();
		int adjustedNewMinute = newMinute % 60;
		int newHour = this.hour + other.getHour() + newMinute / 60;
		int adjustedNewHour = newHour % 24;
		return new Time(adjustedNewHour, adjustedNewMinute);
	}

	public Time subtract(Time other) {
		int newMinute = this.minute - other.getMinute();
		int adjustedNewMinute = 0;
		if (newMinute >= 0) {
			adjustedNewMinute = newMinute;
		} else {
			adjustedNewMinute = newMinute + 60;
		}
		int newHour = this.hour - other.getHour();
		if (newMinute < 0) {
			newHour--;
		}
		if (newHour >= 0) {
			return new Time(newHour, adjustedNewMinute);
		} else {
			return new Time(newHour + 24, adjustedNewMinute);
		}
	}
	
	public int compare(Time other) {
		if (other.getHour() != this.hour) {
			return this.hour - other.getHour();
		} else {
			return this.minute - other.getMinute();
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
	
	public boolean equals(Object o) {
		if (o instanceof Time) {
			Time other = (Time) o;
			return this.compare(other) == 0;
		} else {
			return false;
		}
	}
	
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
	
	

}
