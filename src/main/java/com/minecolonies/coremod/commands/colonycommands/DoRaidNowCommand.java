package com.minecolonies.coremod.commands.colonycommands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.coremod.colony.Colony;
import com.minecolonies.coremod.colony.ColonyManager;
import com.minecolonies.coremod.commands.AbstractSingleCommand;
import com.minecolonies.coremod.commands.ActionMenuState;
import com.minecolonies.coremod.commands.IActionCommand;
import com.minecolonies.coremod.entity.ai.mobs.util.MobEventsUtils;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

/**
 * Trigger a raid event at run
 */
public class DoRaidNowCommand extends AbstractSingleCommand implements IActionCommand
{

    public static final  String              DESC                       = "raid";
    private static final TextComponentString NO_COLONY_FOUND_MESSAGE = new TextComponentString("No Colony found.");
    private static final TextComponentString NO_ARGUMENTS               = new TextComponentString("Please define a colony to raid.");
    private static final TextComponentString SUCCESSFUL                 = new TextComponentString("Command Successful");

    /**
     * no-args constructor called by new CommandEntryPoint executer.
     */
    public DoRaidNowCommand()
    {
        super();
    }

    /**
     * Initialize this SubCommand with it's parents.
     *
     * @param parents an array of all the parents.
     */
    public DoRaidNowCommand(@NotNull final String... parents)
    {
        super(parents);
    }

    @NotNull
    @Override
    public String getCommandUsage(@NotNull final ICommandSender sender)
    {
        return super.getCommandUsage(sender) + "<ColonyId>";
    }

    @Override
    public void execute(@NotNull final MinecraftServer server, @NotNull final ICommandSender sender, @NotNull final ActionMenuState actionMenuState) throws CommandException
    {
        final Colony colony = actionMenuState.getColonyForArgument("colony");
        if (colony == null)
        {
            sender.sendMessage(NO_COLONY_FOUND_MESSAGE);
            return;
        }
        executeShared(server, sender, colony);
    }

    @Override
    public void execute(@NotNull final MinecraftServer server, @NotNull final ICommandSender sender, @NotNull final String... args) throws CommandException
    {
        Colony colony = null;
        if (args.length != 0)
        {
            colony = ColonyManager.getColony(Integer.parseInt(args[0]));
            if (colony == null)
            {
                sender.sendMessage(NO_COLONY_FOUND_MESSAGE);
                return;
            }
        }
        else
        {
            sender.sendMessage(NO_ARGUMENTS);
            return;
        }

        executeShared(server, sender, colony);
    }

    private void executeShared(@NotNull final MinecraftServer server, @NotNull final ICommandSender sender, @Nullable final Colony colony)
    {
        if (sender instanceof EntityPlayer && !isPlayerOpped(sender))
        {
            sender.sendMessage(new TextComponentString("Must be OP to use command"));
            return;
        }

        MobEventsUtils.barbarianEvent(colony.getWorld(), colony);
        sender.sendMessage(SUCCESSFUL);
    }

    @NotNull
    @Override
    public List<String> getTabCompletionOptions(
                                                 @NotNull final MinecraftServer server,
                                                 @NotNull final ICommandSender sender,
                                                 @NotNull final String[] args,
                                                 @Nullable final BlockPos pos)
    {
        return Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(@NotNull final String[] args, final int index)
    {
        return index == 0
                 && args.length > 0
                 && !args[0].isEmpty()
                 && getIthArgument(args, 0, Integer.MAX_VALUE) == Integer.MAX_VALUE;
    }
}
