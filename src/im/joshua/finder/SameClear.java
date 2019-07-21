package im.joshua.finder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SameClear {
    private File srcDir, referDir;

    public SameClear(File srcDir, File referDir) {
        this.srcDir = srcDir.getAbsoluteFile();
        this.referDir = referDir.getAbsoluteFile();
    }

    public void start() {
        if (srcDir == null || !srcDir.exists() || !srcDir.isDirectory())
            return;

        if (referDir == null || !referDir.exists() || !referDir.isDirectory())
            return;

        File[] referFiles = referDir.listFiles();
        File[] srcFiles = srcDir.listFiles();
        List<File> deleteList = new ArrayList<>();

        for (File file : srcFiles) {
            if (isFound(file, referFiles)) {
                deleteList.add(file);
            }
        }

        for (int i = 0, len = deleteList.size(); i < len; i++) {
            File file = deleteList.get(i);
            boolean res = file.delete();
            System.out.println(String.format("[delete file]:%s %b", file.getName(), res));
        }
    }

    private boolean isFound(File file, File[] dir) {
        if (dir == null || file == null || !file.exists())
            return false;

        for (File f : dir) {
            if (f == null || !f.exists())
                continue;

            if (f.getName().toLowerCase().equals(file.getName().toLowerCase())
                    && f.isDirectory() == file.isDirectory()
                    && f.isFile() == file.isFile()) {
                return true;
            }
        }

        return false;
    }
}
