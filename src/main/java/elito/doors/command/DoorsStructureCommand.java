package elito.doors.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;

import static net.minecraft.server.command.CommandManager.literal;

public class DoorsStructureCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(literal("doors_structures").then(
                                literal("update").executes((context) -> {

                                    ServerCommandSource source =  context.getSource();

                                    Iterable<ServerWorld> serverWorlds = source.getServer().getWorlds();

                                    for (ServerWorld serverWorld : serverWorlds) {
                                        serverWorld.getChunkManager();
                                    }

                                    return 1;
                                })
                        )
                ));
    }

}
