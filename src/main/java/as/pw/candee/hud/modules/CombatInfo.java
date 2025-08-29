package as.pw.candee.hud.modules;

import as.pw.candee.CandeePlusRewrite;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;

import as.pw.candee.hud.Hud;
import as.pw.candee.module.Module;
import as.pw.candee.module.combat.AutoMend;
import as.pw.candee.module.combat.Blocker;
import as.pw.candee.module.combat.CevBreaker;
import as.pw.candee.module.combat.CivBreaker;
import as.pw.candee.module.combat.HoleFill;
import as.pw.candee.module.combat.PistonAura;
import as.pw.candee.setting.Setting;
import as.pw.candee.utils.ColorUtil;
import as.pw.candee.utils.RenderUtil;

public class CombatInfo extends Hud {
    public final Setting<Boolean> shadow;
    public final Setting<Color> color;
    public final Setting<Boolean> mend;
    public final Setting<Boolean> blocker;
    public final Setting<Boolean> cev;
    public final Setting<Boolean> civ;
    public final Setting<Boolean> holefill;
    public final Setting<Boolean> pa;

    public CombatInfo() {
        super("CombatInfo", 50.0F, 10.0F);
        this.shadow = register(new Setting("Shadow", Boolean.TRUE));
        this.color = register(new Setting("Color", new Color(255, 255, 255, 255)));
        this.mend = register(new Setting("AutoMend", Boolean.TRUE));
        this.blocker = register(new Setting("Blocker", Boolean.TRUE));
        this.cev = register(new Setting("CevBreaker", Boolean.TRUE));
        this.civ = register(new Setting("CivBreaker", Boolean.TRUE));
        this.holefill = register(new Setting("HoleFill", Boolean.TRUE));
        this.pa = register(new Setting("PistonAura", Boolean.TRUE));
    }

    public void onRender() {
        try {
            Module mend = getModule(AutoMend.class);
            Module blocker = getModule(Blocker.class);
            Module cev = getModule(CevBreaker.class);
            Module civ = getModule(CivBreaker.class);
            Module holefill = getModule(HoleFill.class);
            Module pa = getModule(PistonAura.class);
            float width = 0.0F;
            float height = 0.0F;
            if (this.mend.getValue()) {
                float _width = drawModuleInfo(mend, height);
                if (width < _width) {
                    width = _width;
                }
                height += RenderUtil.getStringHeight(1.0F) + 5.0F;
            }
            if (this.blocker.getValue()) {
                float _width = drawModuleInfo(blocker, height);
                if (width < _width) {
                    width = _width;
                }
                height += RenderUtil.getStringHeight(1.0F) + 5.0F;
            }
            if (this.cev.getValue()) {
                float _width = drawModuleInfo(cev, height);
                if (width < _width) {
                    width = _width;
                }
                height += RenderUtil.getStringHeight(1.0F) + 5.0F;
            }
            if (this.civ.getValue()) {
                float _width = drawModuleInfo(civ, height);
                if (width < _width) {
                    width = _width;
                }
                height += RenderUtil.getStringHeight(1.0F) + 5.0F;
            }
            if (this.holefill.getValue()) {
                float _width = drawModuleInfo(holefill, height);
                if (width < _width) {
                    width = _width;
                }
                height += RenderUtil.getStringHeight(1.0F) + 5.0F;
            }
            if (this.pa.getValue()) {
                float _width = RenderUtil.drawString(pa.name + " : " + (pa.isEnable ? (ChatFormatting.GREEN + "ON") : (ChatFormatting.RED + "OFF")), this.x.getValue(), this.y.getValue() + height, ColorUtil.toRGBA(this.color.getValue()), this.shadow.getValue(), 1.0F);
                if (width > _width) {
                    width = _width;
                }
                height += RenderUtil.getStringHeight(1.0F) + 5.0F;
            }
            this.width = width - this.x.getValue();
            this.height = height;
        }
        catch (Exception ignored) {}
    }

    public float drawModuleInfo(Module module, float offset) {
        return RenderUtil.drawString(module.name + " : " + (module.isEnable ? (ChatFormatting.GREEN + "ON") : (ChatFormatting.RED + "OFF")), this.x.getValue(), this.y.getValue() + offset, ColorUtil.toRGBA(this.color.getValue()), this.shadow.getValue(), 1.0F);
    }

    public Module getModule(Class clazz) {
        return CandeePlusRewrite.m_module.getModuleWithClass(clazz);
    }
}