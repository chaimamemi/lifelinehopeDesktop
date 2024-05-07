package Controllers.user;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager {
    private static final String HISTORY_FILE_PATH = System.getProperty("user.home") + File.separator + "chat_history.ser";  // Save in user home directory

    public static void saveHistory(List<SearchAction> history) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(HISTORY_FILE_PATH))) {
            out.writeObject(history);
            System.out.println("History saved successfully to " + HISTORY_FILE_PATH);
        } catch (IOException e) {
            System.err.println("Failed to save history: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<SearchAction> loadHistory() {
        File historyFile = new File(HISTORY_FILE_PATH);
        if (historyFile.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(historyFile))) {
                return (List<SearchAction>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Failed to load history: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("No existing history file found.");
        }
        return new ArrayList<>();
    }
}
