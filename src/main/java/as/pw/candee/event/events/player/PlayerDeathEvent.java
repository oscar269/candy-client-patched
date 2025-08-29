package as.pw.candee.event.events.player;

import as.pw.candee.event.CandeeEvent;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerDeathEvent extends CandeeEvent {
    public final EntityPlayer player;

    public PlayerDeathEvent(EntityPlayer player) {
        this.player = player;
    }
}