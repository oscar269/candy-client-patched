package as.pw.candee.event.events.world;

import as.pw.candee.event.CandeeEvent;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class BlockEvent extends CandeeEvent {
    public final BlockPos pos;
    public final EnumFacing facing;
    public int stage;

    public BlockEvent(int stage, BlockPos pos, EnumFacing facing) {
        this.stage = 0;
        this.stage = stage;
        this.pos = pos;
        this.facing = facing;
    }
}