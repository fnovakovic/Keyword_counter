package org.example.main;


import org.example.commands.*;
import org.example.jobs.JobDispatcher;
import org.example.results.FileScannerResult;
import org.example.results.ResultRetriever;
import org.example.fileScanner.FileScanner;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class App {
    private final List<Command> commands;

    public App(
            CopyOnWriteArrayList<String> paths,
            Map<String, FileScannerResult> scannedFileResults,
            Map<String, Map<String, Integer>> cacheFileSummery,
            ResultRetriever resultRetriever,
            DirectoryCrawler directoryCrawler,
            JobDispatcher jobDispatcher,
            FileScanner fileScanner
    ) {
        commands = List.of(
                new CommandQuery(scannedFileResults, cacheFileSummery),
                new CommandCFS(cacheFileSummery),
                new CommandStop(resultRetriever, directoryCrawler, jobDispatcher, fileScanner),
                new CommandAD(paths, directoryCrawler),
                new CommandGet(scannedFileResults,cacheFileSummery)
        );
    }

    public Command getCommand (String str) {
        Command command = new CommandNotFound(); // ad putanja

        String[] args = str.split("\\s+");

        for (Command c: commands) {
            if (c.getCommandName() != null && c.getCommandName().equals(args[0])) {
                command = c;

                if (args.length >= 2) {
                    command.setArguments(args[1]);
                }
            }
        }

        return command;
    }
}