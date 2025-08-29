package as.pw.candee.event.events.render;

import as.pw.candee.event.CandeeEvent;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class RenderEntityModelEvent extends CandeeEvent {
    public final ModelBase modelBase;
    public final Entity entity;
    public final float limbSwing;
    public final float limbSwingAmount;
    public final float age;
    public final float headYaw;
    public final float headPitch;
    public final float scale;

    public RenderEntityModelEvent(ModelBase modelBase, Entity entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch, float scale) {
        this.modelBase = modelBase;
        this.entity = entity;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.age = age;
        this.headYaw = headYaw;
        this.headPitch = headPitch;
        this.scale = scale;
    }
}