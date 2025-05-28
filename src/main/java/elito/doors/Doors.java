package elito.doors;

import elito.doors.command.ExportStructureCommand;
import elito.doors.command.MathCommand;
import elito.doors.command.RopeCommand;
import elito.doors.command.StructureBlockCommand;
import elito.doors.event.UsedItemWithCommand;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Doors implements ModInitializer {

    String MOD_ID = "doors";
    Identifier IDENTIFIER = Identifier.of(MOD_ID);

    @Override
    public void onInitialize() {
        UsedItemWithCommand.register();
        ExportStructureCommand.register();
        StructureBlockCommand.register();
        RopeCommand.register();
        MathCommand.register();
    }
}
