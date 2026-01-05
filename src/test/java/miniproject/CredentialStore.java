package miniproject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class CredentialStore {

    private static final Path FILE = Path.of("target", "last-account.txt");

    private CredentialStore() {
        // utility
    }

    public static void saveEmail(String email) {
        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, email);
        } catch (IOException e) {
            System.err.println("Could not save email: " + e.getMessage());
        }
    }

    public static String loadEmail() {
        try {
            if (Files.exists(FILE)) {
                return Files.readString(FILE).trim();
            }
        } catch (IOException e) {
            System.err.println("Could not load email: " + e.getMessage());
        }
        return "";
    }
}
