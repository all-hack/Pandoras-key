package fordhamcss.pandorakey4;

public class ContactEvent extends Event {

	public ContactEvent(long inTime, String inPlace, String inName) {
		super(inTime, inPlace, "Contact", inName);
	}
}