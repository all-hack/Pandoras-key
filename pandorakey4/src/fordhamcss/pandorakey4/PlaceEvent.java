package fordhamcss.pandorakey4;


public class PlaceEvent extends Event {

	private String locationName;

	public PlaceEvent(long inTime, String inPlace, String inName) {
		super(inTime, inPlace, "Location");
		locationName = inName;
	}
}