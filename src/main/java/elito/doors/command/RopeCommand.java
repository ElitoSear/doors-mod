package elito.doors.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import elito.doors.utils.Interpolation;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RopeCommand {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("rope")
                .then(
                        argument(
                                "origin", Vec3ArgumentType.vec3()
                        ).then(
                                argument(
                                        "destination", Vec3ArgumentType.vec3()
                                ).executes(
                                        context -> {
                                            ServerCommandSource source = context.getSource();

                                            Vec3d origin = Vec3ArgumentType.getVec3(context, "origin");
                                            Vec3d destination = Vec3ArgumentType.getVec3(context, "destination");
                                            double distance = origin.distanceTo(destination);
                                            double curve = distance / 4;
                                            int segments = (int) (distance * 2);

                                            generateCurve(source, origin, destination, curve, segments);
                                            return 1;
                                        }
                                ).then(
                                        argument(
                                                "curve", DoubleArgumentType.doubleArg()
                                        ).executes(
                                                context -> {

                                                    ServerCommandSource source = context.getSource();

                                                    Vec3d origin = Vec3ArgumentType.getVec3(context, "origin");
                                                    Vec3d destination = Vec3ArgumentType.getVec3(context, "destination");
                                                    double distance = origin.distanceTo(destination);
                                                    double curve = DoubleArgumentType.getDouble(context, "curve");
                                                    int segments = (int) (distance * 2);

                                                    generateCurve(source, origin, destination, curve, segments);
                                                    return 1;
                                                }
                                        ).then(
                                                argument(
                                                        "segments", IntegerArgumentType.integer(1)
                                                ).executes(
                                                        context -> {

                                                            ServerCommandSource source = context.getSource();

                                                            Vec3d origin = Vec3ArgumentType.getVec3(context, "origin");
                                                            Vec3d destination = Vec3ArgumentType.getVec3(context, "destination");
                                                            double curve = DoubleArgumentType.getDouble(context, "curve");
                                                            int segments = IntegerArgumentType.getInteger(context, "segments");

                                                            generateCurve(source, origin, destination, curve, segments);
                                                            return 1;
                                                        }
                                                )
                                        )
                                )
                        )
                )));
    }



    public static void generateCurve(ServerCommandSource source, Vec3d origin, Vec3d destination, double curve, int segments) {
        World world = source.getWorld();
        Vec3d middle = Interpolation.linear(origin, destination, 0.5).add(0, -curve, 0);

        for(int i = 0; i < segments; i++) {

            double t = (double) i / (segments - 1);

            Vec3d interpolation = Interpolation.cubic(origin, destination, middle, t);
            Vec3d nextInterpolation = Interpolation.cubic(origin, destination, middle, t + (1.0 / segments));

            DisplayEntity.ItemDisplayEntity itemDisplay = new DisplayEntity.ItemDisplayEntity(EntityType.ITEM_DISPLAY, world);
            itemDisplay.setPosition(interpolation);
            itemDisplay.addCommandTag("decoration");
            itemDisplay.addCommandTag("curved");
            ItemStack itemStack = new ItemStack(Items.IRON_BLOCK, 1);
            itemStack.set(DataComponentTypes.ITEM_MODEL, Identifier.of("doors:decoration/chain"));
            itemDisplay.setItemStack(itemStack);

            Vec2f rotation = getRotationFromTo(interpolation, nextInterpolation);
            itemDisplay.rotate(rotation.x, rotation.y);

            world.spawnEntity(itemDisplay);
        }
    }

    public static Vec2f getRotationFromTo(Vec3d origin, Vec3d destination) {
        Vec3d direction = destination.subtract(origin).normalize();

        float yaw = (float) Math.toDegrees(Math.atan2(-direction.x, direction.z));
        float pitch = (float) Math.toDegrees(Math.asin(-direction.y));

        return new Vec2f(yaw, pitch);
    }

}
