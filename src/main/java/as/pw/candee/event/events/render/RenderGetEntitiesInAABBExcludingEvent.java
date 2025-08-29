package as.pw.candee.event.events.render;

import com.google.common.base.Predicate;
import as.pw.candee.event.CandeeEvent;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;

public class RenderGetEntitiesInAABBExcludingEvent extends CandeeEvent {
    public RenderGetEntitiesInAABBExcludingEvent(WorldClient worldClient, Entity entityIn, AxisAlignedBB boundingBox, Predicate predicate) {}
}