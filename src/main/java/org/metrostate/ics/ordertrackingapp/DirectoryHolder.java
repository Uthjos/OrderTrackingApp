package org.metrostate.ics.ordertrackingapp;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryHolder {
    public static String getDirectory(Directory directory) {
        String projectPath = System.getProperty("user.dir");
        Path currentDirPath = Paths.get(projectPath, "src",  "main");

        switch (directory) {
            case saveState:
                currentDirPath = currentDirPath.resolve(Paths.get("saveState"));
                break;
            case testOrders:
                currentDirPath = currentDirPath.resolve(Paths.get("testOrders"));
                break;
            case completeOrders:
                currentDirPath = currentDirPath.resolve(Paths.get("completeOrders"));
                break;
            default:
                currentDirPath = currentDirPath.resolve(Paths.get("errorFolder"));
                break;
        }
    createDirectory(currentDirPath.toString());

    return currentDirPath.toString();
    }

    public static String createDirectory(String directory) {

        File f = new File(directory);
        if(!f.exists()) {
            f.mkdirs();
        }

        return directory;
    }
}
