package elito.doors.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class MathCommand {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("math")
                .then(
                        literal("atan2")
                                .then(
                                        argument("y", DoubleArgumentType.doubleArg())
                                                .then(
                                                        argument("x", DoubleArgumentType.doubleArg())
                                                                .then(
                                                                        argument("scale", DoubleArgumentType.doubleArg())
                                                                                .executes(
                                                                                        commandContext -> {
                                                                                            double y = DoubleArgumentType.getDouble(commandContext, "y");
                                                                                            double x = DoubleArgumentType.getDouble(commandContext, "x");
                                                                                            double scale = DoubleArgumentType.getDouble(commandContext, "scale");
                                                                                            return (int) Math.round(Math.atan2(y, x) * scale);
                                                                                        }
                                                                                )
                                                                )
                                                                .executes(
                                                                        commandContext -> {
                                                                            double y = DoubleArgumentType.getDouble(commandContext, "y");
                                                                            double x = DoubleArgumentType.getDouble(commandContext, "x");
                                                                            return (int) Math.round(Math.atan2(y, x));
                                                                        }
                                                                )
                                                )
                                )
                )));
    }
}
