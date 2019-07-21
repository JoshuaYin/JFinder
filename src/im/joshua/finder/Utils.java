package im.joshua.finder;

import java.io.Closeable;
import java.io.IOException;

public class Utils {
    public static void closeAll(Closeable... closeables) {
        if (closeables == null)
            return;

        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String formatHex(byte[] bytes) {
        if (bytes == null || bytes.length == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = 0, len = bytes.length; i < len; i++) {
            sb.append(String.format("%02X", Byte.toUnsignedInt(bytes[i])));
            if ((++count) % 8 == 0)
                sb.append(" ");
        }

        return sb.toString();
    }

    public static String formatInteger(byte[] bytes) {
        if (bytes == null || bytes.length == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = 0, len = bytes.length; i < len; i++) {
            sb.append(String.format("%02d", Byte.toUnsignedInt(bytes[i])));
            if ((++count) % 8 == 0)
                sb.append(" ");
        }

        return sb.toString();
    }

    public static String formatFileSize(long size) {
        if (size < 1 << 10)
            return String.format("%d Byte(s)", size);

        if (size < 1 << 20)
            return String.format("%.02f KB", (size * 1.f / (1 << 10)));

        if (size < 1 << 30)
            return String.format("%.02f MB", (size * 1.f / (1 << 20)));

        return String.format("%.02f GB", (size * 1.f / (1 << 30)));
    }

    public static String formatTime(long start, long end) {
        if (start > end)
            return "";

        return formatTime(end - start);
    }

    public static String formatTime(long dlt) {
        if (dlt < 0)
            return "";

        if (dlt < 1000)
            return String.format("%d 毫秒", dlt);

        if (dlt < 60 * 1000)
            return String.format("%d 秒", (dlt / 1000));

        int hour, min, sec;
        hour = (int) Math.floor(dlt * 1.f / (60 * 60 * 1000));
        long minF = dlt % (60 * 60 * 1000);
        min = (int) Math.floor(minF * 1.f / (60 * 1000));
        long secF = minF % (60 * 1000);
        sec = (int) (secF/1000);
        StringBuffer sb = new StringBuffer();
        if (hour > 0)
            sb.append(hour).append("小时");

        if (min > 0)
            sb.append(min).append("分钟");

        if (sec > 0)
            sb.append(sec).append("秒");
        return sb.toString();
    }
}
