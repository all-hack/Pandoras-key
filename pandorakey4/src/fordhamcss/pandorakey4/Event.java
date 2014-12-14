package fordhamcss.pandorakey4;

public class Event {
	// All events have below fields
	private String time =null;
	public String place=null;
	private String type =null;
	public String contactName=null;
	
	public Event(String inTime, String inPlace, String inType) {
        //Constructor for location
        time = inTime;
		place = inPlace;
		type = inType;
	}
	
	public Event(String inTime, String inPlace, String inType, String inName) {
        //Constructor for contact
		time = inTime;
		place = inPlace;
		type = inType;
		contactName = inName;
	}
	
	public String getContactName()
	{
		return contactName;
	}
	
	public String getTime() {
		return time;
	}
	
	public String getPlace() {
		return place;
	}
	
	public String getType() {
		return type;
	}
	
}
