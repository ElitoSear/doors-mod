package elito.doors.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.DataCommandStorage;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.literal;

public class ExportStructureCommand {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(literal("exportStructure").executes(context -> {
                    ServerCommandSource source = context.getSource();
                    MinecraftServer server = source.getServer();

                    DataCommandStorage dataCommandStorage = server.getDataCommandStorage();

                    NbtCompound nbt = dataCommandStorage.get(Identifier.of("structure_block_tools:main")).getCompoundOrEmpty("export");

                    String nbtString = nbt.toString();

                    String structureName = nbt.getString("name", "no_structure_name_found");

                    ClickEvent clickEvent = new ClickEvent.CopyToClipboard( "data modify entity @s data merge value " + nbtString);

                    HoverEvent hoverEvent = new HoverEvent.ShowText(Text.literal("Click here to copy the structure: " + structureName + " into your clipboard."));

                    Text copyableText = Text.literal("\n\n\n\n[Exported]!!!\n\n\n\n")
                            .styled(style -> style
                                    .withColor(0xFFFFFF)
                                    .withClickEvent(clickEvent).withHoverEvent(hoverEvent)
                                    .withBold(true)
                            );


                    Entity serverPlayerEntity = source.getEntity();

                    if (serverPlayerEntity instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity) serverPlayerEntity).sendMessageToClient(copyableText, false);
                    }

                    return 1;
                }))
        );
    }

}
