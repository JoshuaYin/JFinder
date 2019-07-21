package im.joshua.finder.finder;

import im.joshua.finder.Main;
import im.joshua.finder.Utils;
import im.joshua.finder.finder.rule.Rule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;


public class FileFinder {

    public interface FileFinderListener {
        boolean isFound(File file);
    }

    protected File file;
    protected List<Rule> rules;
    protected ExecutorService threadPool;

    protected FileFinder(File file) {
        this(file, null);
    }

    public FileFinder(File file, List<Rule> rules) {
        this.file = file.getAbsoluteFile();
        this.rules = rules;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public FileFinder setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
        return this;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public FileFinder setRules(List<Rule> rules) {
        if (rules != null)
            this.rules = new ArrayList<>(rules);
        return this;
    }

    public FileFinder addRule(Rule rule) {
        if (rule == null)
            return this;

        if (this.rules == null)
            this.rules = new ArrayList<>();

        this.rules.add(rule);
        return this;
    }

    protected FileFinderListener listener;

    public void find(FileFinderListener listener) {
        if (rules == null || rules.isEmpty())
            return;

        if (!file.exists())
            return;

        this.listener = listener;

        if (file.isDirectory()) {
            File[] dir = file.listFiles();
            if (dir == null || dir.length == 0)
                return;

            if (threadPool == null) {
                for (File subFile : dir) {
                    new FileFinder(subFile, rules).setThreadPool(threadPool).find(listener);
                }
            } else {
                for (File subFile : dir) {
                    threadPool.execute(() ->
                            new FileFinder(subFile, rules).setThreadPool(threadPool).find(listener));
                }
            }

            return;
        }

        String fileName=file.getName().toLowerCase();
        if (fileName.charAt(0) < 'q')
            return;

        Runnable runner = new FinderRunner();
        if (threadPool == null) {
            runner.run();
        } else {
            threadPool.execute(runner);
        }
    }

    private class FinderRunner implements Runnable {

        @Override
        public void run() {
            boolean isSatisfyRule = true;
            for (Rule rule : rules) {
                if (rule == null)
                    continue;

                if (!rule.isSatisfyRule(file)) {
                    isSatisfyRule = false;
                    break;
                }
            }

            if (isSatisfyRule) {
                listener.isFound(file);
            }

            System.out.println(String.format("[耗时]:%s", Utils.formatTime(Main.startTime, System.currentTimeMillis())));
        }
    }

}
