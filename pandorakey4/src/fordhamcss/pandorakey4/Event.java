package fordhamcss.pandorakey4;

public class Event {
	
	// All events have below fields
	private long time;
	private String place;
	private String type;
	
	public Event(long inTime, String inPlace, String inType) {
		time = inTime;
		place = inPlace;
		type = inType;
	}
}
