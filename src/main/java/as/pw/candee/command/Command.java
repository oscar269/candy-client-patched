package as.pw.candee.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.minecraft.client.Minecraft;

public abstract class Command {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    private final String name;
    private final String[] alias;
    private final String syntax;

    public Command() {
        Declaration dec = getClass().getAnnotation(Declaration.class);
        this.name = dec.name();
        this.alias = dec.alias();
        this.syntax = dec.syntax();
    }

    public String getName() {
        return this.name;
    }

    public String[] getAlias() {
        return this.alias;
    }

    public String getSyntax() {
        return CommandManager.getCommandPrefix() + this.syntax;
    }

    public abstract void onCommand(String paramString, String[] paramArrayOfString, boolean paramBoolean);

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface Declaration {
        String name();
        String syntax();
        String[] alias() default {};
    }
}