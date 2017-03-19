package client;

import java.io.*;

public class CacheLoader {

    private static final String CACHE_PACKAGE = "cache";
    private static final String CACHE_FILE_PATCH = "tasks.data";

    private static String getAbsoluteCacheDir() {
        return new File("").getAbsolutePath() + "\\" + CACHE_PACKAGE;
    }

    private static String getAbsoluteCacheFilePatch() {
        return getAbsoluteCacheDir() + "\\" + CACHE_FILE_PATCH;
    }

    private static File getCacheFile() {
        if (isCacheExists()) {
            return new File(getAbsoluteCacheFilePatch());
        }
        return null;
    }

    public static File createCacheFile() {
        File catalog = new File(getAbsoluteCacheDir());
        if (!catalog.exists()) {
            catalog.mkdir();
        }
        File file = new File(getAbsoluteCacheFilePatch());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static boolean isCacheExists() {

        File catalog = new File(getAbsoluteCacheDir());
        if (catalog.exists()) {
            File file = new File(getAbsoluteCacheFilePatch());
            if (file.exists()) {
                return true;
            }
        }

        return false;

    }

    public static Object readCache() {

        if (!isCacheExists()) return null;

        Object cacheData = null;

        ObjectInputStream in = null;
        try {
            File file = getCacheFile();
            if (file.length() > 0) {
                in = new ObjectInputStream(new FileInputStream(file));
                cacheData = in.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return cacheData;

    }

    public static void writeCache(Object object) {

        File file = getCacheFile();

        ObjectOutputStream out = null;
        try {

            out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(object);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
