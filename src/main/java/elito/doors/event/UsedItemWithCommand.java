package elito.doors.event;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;

import java.util.Objects;

public class UsedItemWithCommand {

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (!world.isClient()) {
                ItemStack stack = player.getStackInHand(hand);

                if (stack.contains(DataComponentTypes.CUSTOM_DATA)) {
                    NbtCompound nbt = Objects.requireNonNull(stack.get(DataComponentTypes.CUSTOM_DATA)).copyNbt();

                    String command = nbt.getString("command", "");

                    if (nbt.contains("command")) {
                        CommandManager commandManager = Objects.requireNonNull(player.getServer()).getCommandManager();
                        ServerCommandSource commandSource = new ServerCommandSource(
                                Objects.requireNonNull(player.getServer()),
                                player.getPos(),
                                player.getRotationClient(),
                                world instanceof ServerWorld ? (ServerWorld) world : null,
                                4,
                                player.getName().getString(),
                                player.getDisplayName(),
                                player.getServer(),
                                player
                        );
                        commandManager.executeWithPrefix(commandSource, command);

                        return ActionResult.SUCCESS;
                    }
                }

            }
            return ActionResult.PASS;
        });
    }
}
