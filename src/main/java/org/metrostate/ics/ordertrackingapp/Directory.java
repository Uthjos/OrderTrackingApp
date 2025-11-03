package org.metrostate.ics.ordertrackingapp;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum Directory {
    savedOrders("orderFiles/savedOrders"),
    testOrders("orderFiles/testOrders"),
    importOrders("orderFiles/importOrders");

    private final String path;

    Directory(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    private static String createDirectory(String directory) {

        File f = new File(directory);
        if(!f.exists()) {
            f.mkdirs();
        }

        return directory;
    }

    public static String getDirectory(Directory directory) {
        Path dirPath  = Paths.get(System.getProperty("user.dir"), "src", "main")
                .resolve(directory.getPath());
        createDirectory(dirPath.toString());
        return dirPath.toString();

    }
}
