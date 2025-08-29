package as.pw.candee.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class CandeeEvent extends Event {
    private boolean cancelled = false;
    private final EventStage stage;

    public CandeeEvent() {
        this(EventStage.PRE);
    }

    public CandeeEvent(EventStage stage) {
        this.stage = stage;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }

    public EventStage getStage() {
        return this.stage;
    }
}