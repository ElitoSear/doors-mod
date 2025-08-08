package elito.doors.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.block.enums.StructureBlockMode;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class StructureBlockCommand {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(literal("structure_block").then(
                                argument("pos", BlockPosArgumentType.blockPos())
                                        .then(
                                                literal("save")
                                                        .executes(
                                                                commandContext -> {
                                                                    ServerCommandSource source = commandContext.getSource();

                                                                    BlockPos blockPos = BlockPosArgumentType.getBlockPos(commandContext, "pos");

                                                                    BlockEntity blockEntity = source.getWorld().getBlockEntity(blockPos);

                                                                    StructureBlockBlockEntity selectedStructureBlock = (StructureBlockBlockEntity) blockEntity;

                                                                    if (selectedStructureBlock == null) {
                                                                        source.sendFeedback(() -> Text.literal("Structure block not found"), false);
                                                                        return 0;
                                                                    }

                                                                    Vec3i size = selectedStructureBlock.getSize();

                                                                    String name = selectedStructureBlock.getTemplateName();

                                                                    StructureBlockMode mode = selectedStructureBlock.getMode();

                                                                    if (getSize(size) < 1) {
                                                                        source.sendFeedback(() -> Text.literal("Invalid size for structure block"), false);
                                                                        return 0;
                                                                    }

                                                                    if (name == null || name.isEmpty()) {
                                                                        source.sendFeedback(() -> Text.literal("Invalid name for structure block"), false);
                                                                        return 0;
                                                                    }

                                                                    if (mode != StructureBlockMode.SAVE) {
                                                                        source.sendFeedback(() -> Text.literal("Structure block must be in save mode"), false);
                                                                        return 0;
                                                                    }

                                                                    source.sendFeedback(() -> Text.literal("Saved structure block successfully"), false);
                                                                    selectedStructureBlock.saveStructure(true);

                                                                    return 1;
                                                                }
                                                        )
                                        ).then(
                                                literal("load")
                                                        .executes(
                                                                commandContext -> {
                                                                    ServerCommandSource source = commandContext.getSource();

                                                                    BlockPos blockPos = BlockPosArgumentType.getBlockPos(commandContext, "pos");

                                                                    BlockEntity blockEntity = source.getWorld().getBlockEntity(blockPos);

                                                                    StructureBlockBlockEntity selectedStructureBlock = (StructureBlockBlockEntity) blockEntity;

                                                                    if (selectedStructureBlock == null) {
                                                                        source.sendFeedback(() -> Text.literal("Structure block not found"), false);
                                                                        return 0;
                                                                    }

                                                                    Vec3i size = selectedStructureBlock.getSize();

                                                                    String name = selectedStructureBlock.getTemplateName();

                                                                    StructureBlockMode mode = selectedStructureBlock.getMode();

                                                                    if (getSize(size) < 1) {
                                                                        source.sendFeedback(() -> Text.literal("Invalid size for structure block"), false);
                                                                        return 0;
                                                                    }

                                                                    if (name == null || name.isEmpty()) {
                                                                        source.sendFeedback(() -> Text.literal("Invalid name for structure block"), false);
                                                                        return 0;
                                                                    }

                                                                    if (mode != StructureBlockMode.LOAD) {
                                                                        source.sendFeedback(() -> Text.literal("Structure block must be in load mode"), false);
                                                                        return 0;
                                                                    }

                                                                    if (selectedStructureBlock.loadAndTryPlaceStructure(source.getWorld())) {
                                                                        source.sendFeedback(() -> Text.literal("Loaded structure block successfully"), false);
                                                                        return 1;
                                                                    } else {
                                                                        source.sendFeedback(() -> Text.literal("Failed to load structure block"), false);
                                                                        return 0;
                                                                    }
                                                                }
                                                        )
                                        )
                        )
                ));
    }

    static int getSize(Vec3i size) {
        return size.getX() * size.getY() * size.getZ();
    }
}
