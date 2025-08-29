package as.pw.candee.event.events.render;

import as.pw.candee.event.CandeeEvent;
import net.minecraft.entity.EntityLivingBase;

public class SwingAnimationEvent extends CandeeEvent {
    private final EntityLivingBase entity;
    private int speed;

    public SwingAnimationEvent(EntityLivingBase entity, Integer speed) {
        this.entity = entity;
        this.speed = speed;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public EntityLivingBase getEntity() {
        return this.entity;
    }
}