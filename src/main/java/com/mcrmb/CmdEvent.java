package com.mcrmb;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CmdEvent extends Event {
    private final String[] commands;
    private final String command;
    private final CommandSender sender;
    private static final HandlerList handlers = new HandlerList();

    public CmdEvent(String[] commands, String command, CommandSender sender) {
        this.commands = commands;
        this.command = command;
        this.sender = sender;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String[] getCommands() {
        return this.commands;
    }

    public String getCommand() {
        return this.command;
    }

    @Deprecated
    public String[] getCmds() {
        return this.commands;
    }

    @Deprecated
    public String getCmd() {
        return this.command;
    }

    public CommandSender getSender() {
        return this.sender;
    }
}
