package org.metrostate.ics.ordertrackingapp;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The Directory Enum holds all data directories
 */
public enum Directory {
    savedOrders("orderFiles/savedOrders"),
    testOrders("orderFiles/testOrders"),
    importOrders("orderFiles/importOrders");
    /**
     * holds the path after src/main/
     */
    private final String path;

    Directory(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    /**
     * Will create the directory if it doesn't exist
     * @param directory
     * @return
     */
    private static String createDirectory(String directory) {

        File f = new File(directory);
        if(!f.exists()) {
            f.mkdirs();
        }

        return directory;
    }

    /**
     * will return the directory of the enum Directory
     * @param directory
     * @return
     */
    public static String getDirectory(Directory directory) {
        Path dirPath  = Paths.get(System.getProperty("user.dir"), "src", "main")
                .resolve(directory.getPath());
        createDirectory(dirPath.toString());
        return dirPath.toString();

    }
}
