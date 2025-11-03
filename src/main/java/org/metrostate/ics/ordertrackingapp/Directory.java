package org.metrostate.ics.ordertrackingapp;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.createDirectory;

public enum Directory {
    saveOrders("orderFiles/SaveOrders"),
    testOrders("orderFiles/TestOrders"),
    importOrders("orderFiles/ImportOrders");

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
