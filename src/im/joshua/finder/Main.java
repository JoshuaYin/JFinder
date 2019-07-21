package im.joshua.finder;

import im.joshua.finder.finder.VideoSweeper;

import java.io.File;
import java.util.concurrent.Executors;

public class Main {
    public static long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        File src = new File("");
        File dir = new File("");

        new VideoSweeper(src, 80)
                .setThreadPool(Executors.newFixedThreadPool(5))
                .find(file -> {
                    boolean res = file.delete();
                    System.out.println(String.format("[delete]: %s [res]: %b",
                            file.getAbsolutePath(), res));
                    return false;
                });

        new SameClear(src, dir).start();
    }


}