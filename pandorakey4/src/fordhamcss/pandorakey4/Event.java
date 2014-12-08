package fordhamcss.pandorakey4;

public class Event {
	
	// All events have below fields
	private long time;
	public String place;
	private String type;
	public String contactName;
	
	public Event(long inTime, String inPlace, String inType) {
		time = inTime;
		place = inPlace;
		type = inType;
	}
	
	public Event(long inTime, String inPlace, String inType, String inName) {
		time = inTime;
		place = inPlace;
		type = inType;
		contactName = inName;
	}
	
	public String getContactName()
	{
		return contactName;
	}
	
	public long getTime() {
		return time;
	}
	
	public String getPlace() {
		return place;
	}
	
	public String getType() {
		return type;
	}
	
}
