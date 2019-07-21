package im.joshua.finder.finder;


import im.joshua.finder.finder.rule.UnfinishFileRule;

import java.io.*;

public class VideoSweeper extends FileFinder {
    public VideoSweeper(File file,int percent) {
        super(file);
        addRule(new UnfinishFileRule(percent));
    }
}
