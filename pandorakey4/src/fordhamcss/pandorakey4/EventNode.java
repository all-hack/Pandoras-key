package fordhamcss.pandorakey4;

import java.util.LinkedList;

public class EventNode {
	
	public LinkedList<Event> theEvent;
	public EventNode left;
	public EventNode right;
	
	public EventNode(Event x) {
		// creates a new linked list to hold events
		theEvent = new LinkedList<Event>();
		// sets data field equal to the event passed in
		theEvent.add(x);
		// a new node has left and right fields initially null
		left = null;
		right = null;
	}
}