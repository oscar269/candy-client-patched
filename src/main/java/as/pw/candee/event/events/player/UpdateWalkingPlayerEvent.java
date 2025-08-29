package as.pw.candee.event.events.player;

import as.pw.candee.event.CandeeEvent;

public class UpdateWalkingPlayerEvent
    extends CandeeEvent {
    public final int stage;

    public UpdateWalkingPlayerEvent(int stage) {
     this.stage = stage;
 }
}