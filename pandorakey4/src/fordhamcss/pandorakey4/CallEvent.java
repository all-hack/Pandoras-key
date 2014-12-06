package fordhamcss.pandorakey4;

/**
 * Created by srubio on 12/6/14.
 */
public class CallEvent extends Event {

    public CallEvent(long inTime, String inPlace, String inName) {
        super(inTime, inPlace, "Call", inName);
    }

    public CallEvent(long inTime, String inName) {
        super(inTime, "Call", inName);
    }
}