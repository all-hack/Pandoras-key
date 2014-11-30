package fordhamcss.pandorakey4;

public class ContactEvent extends Event {

	public ContactEvent(long inTime, String inPlace, String inName) {
		super(inTime, inPlace, "Contact", inName);
	}
	
	public ContactEvent(long inTime, String inName) {
		super(inTime, "Contact", inName);
	}
}