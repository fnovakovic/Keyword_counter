package org.example.commands;


import org.example.results.FileScannerResult;


import java.util.Map;

public class GetFileResultCommand implements Command {
    private String args;

    private final Map<String, FileScannerResult> scannedFileResults;
    private Map<String, Map<String, Integer>> cacheFileSummery;

    public GetFileResultCommand (
            Map<String, FileScannerResult> scannedFileResults,
            Map<String, Map<String, Integer>> cacheFileSummery
    ) {
        this.scannedFileResults = scannedFileResults;
        this.cacheFileSummery = cacheFileSummery;
    }

    @Override
    public void execute() {
        if (args.equals("summary")) {
            this.callSummary();
            return;
        }

        Map<String, Integer> result = null;

        FileScannerResult fsr = scannedFileResults.get(this.args);
        if (fsr != null) {
            result = fsr.getResult();
        }

        if (result == null) {
            System.out.println("Corpus: " + this.args + " doesnt exist");
            return;
        }

        System.out.println(result);
    }

    private void callSummary () {
        if (!cacheFileSummery.isEmpty()) {
            for (Map.Entry<?, ?> entry : cacheFileSummery.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            return;
        }

        for (Map.Entry<String, FileScannerResult> entry: scannedFileResults.entrySet()) {
            FileScannerResult fileScannerResult = entry.getValue();
            cacheFileSummery.put(fileScannerResult.getCorpusName(), fileScannerResult.getResult());
            System.out.println(fileScannerResult.getCorpusName() + ": " + fileScannerResult.getResult());
        }

    }

    @Override
    public void setArguments(String args) {
        this.args = args;
    }

    @Override
    public String getCommandName() {
        return "file";
    }

    @Override
    public boolean stop() {
        return false;
    }
}
