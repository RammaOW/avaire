package com.avairebot.orion.commands;

import com.avairebot.orion.contracts.commands.AbstractCommand;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.utils.Checks;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandHandler {

    private static final Map<List<String>, CommandContainer> COMMANDS = new HashMap<>();

    public static CommandContainer getCommand(AbstractCommand command) {
        for (Map.Entry<List<String>, CommandContainer> entry : COMMANDS.entrySet()) {
            if (entry.getValue().getCommand().isSame(command)) {
                return entry.getValue();
            }
        }

        return null;
    }

    public static CommandContainer getCommand(Message message) {
        return getCommand(message.getContent().split(" ")[0].toLowerCase());
    }

    public static CommandContainer getCommand(String commandTrigger) {
        for (Map.Entry<List<String>, CommandContainer> entry : COMMANDS.entrySet()) {
            for (String trigger : entry.getKey()) {
                if (commandTrigger.equalsIgnoreCase(entry.getValue().getDefaultPrefix() + trigger)) {
                    return entry.getValue();
                }
            }
        }

        return null;
    }

    public static boolean register(AbstractCommand command) {
        for (String trigger : command.getTriggers()) {
            for (Map.Entry<List<String>, CommandContainer> entry : COMMANDS.entrySet()) {
                if (entry.getKey().contains(trigger.toLowerCase())) {
                    return false;
                }
            }
        }

        Category category = Category.fromCommand(command);

        Checks.notNull(category, String.format("%s :: %s", command.getName(), "Invalid command category, command category"));
        Checks.notNull(command.getDescription(), String.format("%s :: %s", command.getName(), "Command description"));

        COMMANDS.put(command.getTriggers(), new CommandContainer(command, category));
        return true;
    }

    public static Collection<CommandContainer> getCommands() {
        return COMMANDS.values();
    }
}
