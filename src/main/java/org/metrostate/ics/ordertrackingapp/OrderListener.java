package org.metrostate.ics.ordertrackingapp;

import javafx.application.Platform;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <a href="https://docs.oracle.com/javase/tutorial/essential/io/notification.html">...</a>
 * Monitors a directory for new order files (JSON and XML) using multithreading.
 * When a new file is detected, it notifies the registered listener.
 */
public class OrderListener implements Runnable {
    private final Path directoryPath;
    private final OrderFileCallback callback;
    private final ExecutorService executorService;
    private volatile boolean running = true;
    private final Set<String> processedFiles;

    /**
     * Interface for callbacks when new order files are detected
     * Notify the GUI to update when new files arrive
     */
    public interface OrderFileCallback {
        void onNewOrderFile(File file);
    }

    /**
     * Creates a new OrderListener for the specified directory
     *
     * @param directoryPath The path to the directory to monitor
     * @param callback The callback to invoke when new files are detected
     */
    public OrderListener(String directoryPath, OrderFileCallback callback) {
        this.directoryPath = Paths.get(directoryPath);
        this.callback = callback;
        this.executorService = Executors.newSingleThreadExecutor();
        this.processedFiles = new HashSet<>();

        loadExistingFiles();
    }

    /**
     * Loads existing files in the directory on statrtup
     */
    private void loadExistingFiles() {
        File directory = directoryPath.toFile();
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".json")); // || name.toLowerCase().endsWith(".xml"));

            if (files != null) {
                for (File file : files) {
                    processedFiles.add(file.getName());
                    // notify callback for existing files to populate GUI
                    Platform.runLater(() -> callback.onNewOrderFile(file));
                }
            }
        }
    }

    // starts monitoring the directory in a separate thread for new files
    public void start() {
        executorService.submit(this); // executorService runs the run() method for the current thread.
        // not sure why this is needed instead of just calling run() but this is suggested.
    }
    // stop
    public void stop() {
        running = false;
        executorService.shutdown();
    }

    /**
     * The main monitoring loop - multithreaded
     * Uses WatchService to monitor the directory for new or modified files
     * <a href="https://docs.oracle.com/javase/8/docs/api/java/nio/file/WatchService.html">...</a>
     */
    @Override
    public void run() { // run method mostly from Oracle docs
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            directoryPath.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY);

            while (running) {
                WatchKey key;
                try {
                    // wait for events
                    key = watchService.take();
                } catch (InterruptedException e) {
                    break;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();
                    String fileName = filename.toString();

                    if (fileName.toLowerCase().endsWith(".json")){ //||
                        //fileName.toLowerCase().endsWith(".xml")) {

                        // check if this is a new file
                        if (!processedFiles.contains(fileName)) {
                            processedFiles.add(fileName);

                            File newFile = directoryPath.resolve(filename).toFile();

                            // sleep to give time for file to be fully written
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }

                            // notify callback on JavaFX Application Thread
                            // update the GUI
                            Platform.runLater(() -> {
                                callback.onNewOrderFile(newFile);
                            });
                        }
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }

            watchService.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}