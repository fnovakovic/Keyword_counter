package org.example.commands;

public interface Command {
    public void execute();
    public void setArguments(String args);
    public String getCommandName();
    public boolean stop();
}
