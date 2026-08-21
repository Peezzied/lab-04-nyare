import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service class that manages loading, updating, and saving system-level configurations and data.
 * This class persists system states to a binary DAT file format.
 */
public class SystemStore {

    private static final String FILE_NAME = "system.dat";
    private SystemData systemData;
    private final TaskStore taskStore;

    /**
     * Constructs a new SystemStore and injects the active TaskStore repository.
     *
     * @param taskStore the TaskStore instance mapping the task list database
     */
    public SystemStore(TaskStore taskStore) {
        this.taskStore = taskStore;
    }

    /**
     * Gets the system configuration details currently loaded in memory.
     *
     * @return the SystemData configuration instance
     */
    public SystemData getSystemData() {
        return systemData;
    }

    /**
     * Updates diagnostics state and saves the SystemData to the system.dat file using binary streams.
     */
    public void saveSystemData() {
        updateStatefulSystemData();
        String formattedDate = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a"))
                .replace("AM", "am")
                .replace("PM", "pm");
        systemData.setLastSavedDate(formattedDate);

        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeLong(systemData.getLastTaskId());
            out.writeUTF(systemData.getLastSavedDate());
            out.writeInt(systemData.getActiveTasksCount());
            out.writeUTF(systemData.getAcademicYear());
            out.writeUTF(systemData.getApplicationVersion());
            out.writeUTF(systemData.getApplicationPlatform());
            out.writeUTF(systemData.getEnvironment());
        } catch (IOException e) {
            System.out.println("Error saving system data: " + e.getMessage());
        }
    }

    /**
     * Recalculates stats such as today's active tasks and the last task ID from the live TaskStore.
     */
    private void updateStatefulSystemData() {
        List<AcademicTask> tasks = taskStore.getTasks();
        long activeCount = tasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.PENDING
                        && t.getDueDate().toLocalDate().isEqual(LocalDate.now()))
                .count();
        if (!taskStore.getTasks().isEmpty()) {
            systemData.setLastTaskId(tasks.getLast().getId());
        }
        systemData.setActiveTasksCount((int) activeCount);
    }

    /**
     * Loads the SystemData object from system.dat. If the file is missing or corrupted,
     * it initializes the system with default data configurations.
     */
    public void loadSystemData() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            this.systemData = createDefault();
            updateStatefulSystemData();
        }

        try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
            long lastTaskId = in.readLong();
            String lastSavedDate = in.readUTF();
            int activeTasksCount = in.readInt();
            String academicYear = in.readUTF();
            String applicationVersion = in.readUTF();
            String applicationPlatform = in.readUTF();
            String environment = in.readUTF();

            this.systemData = new SystemData(
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
            this.systemData = createDefault();
        }
    }

    /**
     * Builds default system configurations when launching for the first time.
     */
    private static SystemData createDefault() {
        return new SystemData(
                0L,
                "N/A",
                0,
                "2026-2027",
                "1.0.0",
                System.getProperty("os.name"),
                "development"
        );
    }
}