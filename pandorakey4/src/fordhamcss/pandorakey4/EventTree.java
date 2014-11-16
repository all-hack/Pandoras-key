package fordhamcss.pandorakey4;

public class EventTree {
	
	private EventNode root;
	
	// constructor method
	public EventTree() {
		root = null;
	}
	
    void insertLocation(Event x) {
        root = insertLocation(x, root);
    }
	
	private EventNode insertLocation(Event newLocation, EventNode t ) {
		// base case
		if(t == null)
			return new EventNode(newLocation);
		else
			t.right = insertLocation(newLocation, t.right);
        return t;
    }
	
    void insertEvent(Event x) {
        root = insertEvent(x, root);
    }
	
	private EventNode insertEvent(Event newEvent, EventNode t) {
		// base case
		if(t == null)
			return new EventNode(newEvent);
		else if(t.right != null)
			t.right = insertEvent(newEvent, t.right);
		else if(t.right == null && t.left == null)
			t.left = insertEvent(newEvent, t.left);
		else
			t.left.theEvent.add(newEvent);
        return t;
    }
	
}
