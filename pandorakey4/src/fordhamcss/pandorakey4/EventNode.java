package fordhamcss.pandorakey4;

import java.util.LinkedList;

public class EventNode {
	
	// creates three fields of an ExpressionNode object
	public LinkedList<Event> theEvent;
	public EventNode left;
	public EventNode right;
	
	// constructor method, creates a new ExpressionNode
	public EventNode(Event x) {
		// sets data field equal to the event passed in
		theEvent.add(x);
		// a new node has left and right fields initially null
		left = null;
		right = null;
	}
}