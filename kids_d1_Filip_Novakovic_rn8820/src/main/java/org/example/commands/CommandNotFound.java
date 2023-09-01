package org.example.commands;

public class CommandNotFound implements Command {
    @Override
    public void execute() {
        System.out.println("Not found command");
    }

    @Override
    public void setArguments(String args) {
    }

    @Override
    public String getCommandName() {
        return null;
    }

    @Override
    public boolean stop() {
        return false;
    }
}
