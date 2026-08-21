import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class SystemStore {

    private static final String FILE_NAME = "system.dat";

    // Saves the current SystemData object into system.dat using binary streams.
    // Called on Manual Save (menu 4) and on Exit (menu 6).
    public static void save(SystemData data) {
        // Keep activeTasksCount in sync with the live TaskStore state
        List<AcademicTask> tasks = TaskStore.getTasks();
        long activeCount = tasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.PENDING)
                .count();
        data.setActiveTasksCount((int) activeCount);

        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeLong(data.getLastTaskId());
            out.writeUTF(data.getLastSavedDate());
            out.writeInt(data.getActiveTasksCount());
            out.writeUTF(data.getAcademicYear());
            out.writeUTF(data.getApplicationVersion());
            out.writeUTF(data.getApplicationPlatform());
            out.writeUTF(data.getEnvironment());
        } catch (IOException e) {
            System.out.println("Error saving system data: " + e.getMessage());
        }
    }

    // Loads the SystemData object from system.dat.
    // If the file does not exist yet (first launch), returns default data instead.
    public static SystemData load() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return createDefault();
        }

        try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
            long lastTaskId = in.readLong();
            String lastSavedDate = in.readUTF();
            int activeTasksCount = in.readInt();
            String academicYear = in.readUTF();
            String applicationVersion = in.readUTF();
            String applicationPlatform = in.readUTF();
            String environment = in.readUTF();

            return new SystemData(
                    lastTaskId,
                    lastSavedDate,
                    activeTasksCount,
                    academicYear,
                    applicationVersion,
                    applicationPlatform,
                    environment
            );
        } catch (IOException e) {
            System.out.println("Error loading system data: " + e.getMessage());
            return createDefault();
        }
    }

    // Default system-level data used on first launch or if system.dat is missing/corrupted.
    private static SystemData createDefault() {
        return new SystemData(
                0L,
                "N/A",
                0,
                "2025-2026",
                "1.0.0",
                System.getProperty("os.name"),
                "development"
        );
    }
}