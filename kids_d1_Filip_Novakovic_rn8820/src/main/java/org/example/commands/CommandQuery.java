package org.example.commands;


import org.example.results.FileScannerResult;


import java.util.List;
import java.util.Map;

public class CommandQuery implements Command {
    private String args;

    private final List<Command> subCommands;

    public CommandQuery(
            Map<String, FileScannerResult> scannedFileResults,
            Map<String, Map<String, Integer>> cacheFileSummery

    ) {
        this.subCommands = List.of(
                new QueryFileResultCommand(scannedFileResults, cacheFileSummery)

        );
    }

    @Override
    public void execute() {
        String[] argsList = this.args.split("\\|");

        if (argsList.length < 2) {
            System.out.println("Wrong command params");
            return;
        }

        Command subCommand = new CommandNotFound();
        for (Command c: subCommands) {
            if (c.getCommandName() != null && c.getCommandName().equals(argsList[0])) {
                subCommand = c;
                subCommand.setArguments(argsList[1]);
            }
        }

        subCommand.execute();
    }

    @Override
    public void setArguments(String args) {
        this.args = args;
    }

    @Override
    public String getCommandName() {
        return "query";
    }

    @Override
    public boolean stop() {
        return false;
    }
}