package im.joshua.finder.finder.rule;

import im.joshua.finder.Utils;

import java.io.*;

public class UnfinishFileRule extends Rule<File, Float> {
    public final int BUFFER_SIZE = 100 << 20;
    public final int PART_SIZE = 4 << 20;

    private final int percent;

    public UnfinishFileRule(int percent) {
        super(String.format("下载完成度 < %d%%", percent));
        this.percent = percent;
    }

    @Override
    public boolean isSatisfyRule(File target) {
        System.out.println(String.format("[start rule]:%s [file]:%s [size]:%s", getName(),
                target.getAbsolutePath(), Utils.formatFileSize(target.length())));
        float res = runRule(target);
        if (res < 100.f)
            System.out.println(String.format("[finish rule]:%s [file]:%s [percent]:%.01f%%",
                    getName(), target.getAbsolutePath(), res));
        return res < percent;
    }

    @Override
    public Float runRule(File target) {
        long fileSize = target.length();
        InputStream is = null;
        float percent = 0.f;
        byte[] buffer;
        byte[] tmp;
        try {
            is = new FileInputStream(target);
            buffer = new byte[BUFFER_SIZE];
            int len = 0;
            long readSize = len;
            int start, residue;

            read:
            while ((len = is.read(buffer)) > 0) {
                readSize += len;
                percent = readSize * 1.f / fileSize * 100;
                for (; len > 0; len -= PART_SIZE) {
                    residue = len > PART_SIZE ? PART_SIZE : len;
                    start = len - residue;
                    tmp = new byte[residue];
                    System.arraycopy(buffer, start, tmp, 0, residue);
                    for (int i = 0; i < residue; i++) {
                        if (tmp[i] != 0x0)
                            continue read;
                    }

                    return percent;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.closeAll(is);
            buffer = null;
            tmp = null;
            System.gc();
        }
        return percent;
    }
}
