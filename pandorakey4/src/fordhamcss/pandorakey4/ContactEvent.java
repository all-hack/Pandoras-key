package fordhamcss.pandorakey4;

public class ContactEvent extends Event {

	private String contactName;

	public ContactEvent(long inTime, String inPlace, String inName) {
		super(inTime, inPlace, "Contact");
		contactName = inName;
	}
}