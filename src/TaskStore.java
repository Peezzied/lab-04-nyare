import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TaskStore {

    private final List<AcademicTask> AcademicTasks = new ArrayList<>();

    private static final String FILE_PATH = "academic_tasks.csv";

    private static final String HEADER = "id,subjectId,subjectCode,title,notes,type,dueDate,status";

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

//     ---------------------------------------------------------------
//     Accessors for other components (e.g. data model / UI) to use.
//     ---------------------------------------------------------------

    public List<AcademicTask> getTasks() {
        return AcademicTasks;
    }

    public void addTask(AcademicTask task) {
        AcademicTasks.add(task);
    }

    public void setTasks(List<AcademicTask> tasks) {
        AcademicTasks.clear();
        AcademicTasks.addAll(tasks);
    }

    // ---------------------------------------------------------------
    // Persistence: text/CSV via character streams
    // ---------------------------------------------------------------

    public void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(HEADER);
            writer.newLine();

            for (AcademicTask task : AcademicTasks) {
                writer.write(toCsvRow(task));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks to " + FILE_PATH + ": " + e.getMessage());
        }
    }



    public void loadTasks() {
        java.io.File file = new java.io.File(FILE_PATH);
        if (!file.exists()) {
            // First launch / no saved data yet — nothing to load.
            return;
        }

        List<AcademicTask> loaded = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // skip header row
            if (line == null) {
                return; // empty file
            }

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                AcademicTask task = fromCsvRow(line);
                if (task != null) {
                    loaded.add(task);
                }
            }

            AcademicTasks.clear();
            AcademicTasks.addAll(loaded);

        } catch (IOException e) {
            System.out.println("Error loading tasks from " + FILE_PATH + ": " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // CSV row conversion helpers
    // ---------------------------------------------------------------

    private static String toCsvRow(AcademicTask task) {
        return String.join(",",
                String.valueOf(task.getId()),
                String.valueOf(task.getSubjectId()),
                escape(task.getSubjectCode()),
                escape(task.getTitle()),
                escape(task.getNotes()),
                task.getType() != null ? task.getType().name() : "",
                task.getDueDate() != null ? task.getDueDate().format(DATE_FORMAT) : "",
                task.getStatus() != null ? task.getStatus().name() : ""
        );
    }

    private static AcademicTask fromCsvRow(String line) {
        List<String> fields = parseCsvLine(line);

        if (fields.size() != 8) {
            System.out.println("Skipping malformed row: " + line);
            return null;
        }

        try {
            long id = Long.parseLong(fields.get(0));
            long subjectId = Long.parseLong(fields.get(1));
            String subjectCode = fields.get(2);
            String title = fields.get(3);
            String notes = fields.get(4);
            TaskType type = fields.get(5).isEmpty() ? null : TaskType.valueOf(fields.get(5));
            LocalDateTime dueDate = fields.get(6).isEmpty() ? null : LocalDateTime.parse(fields.get(6), DATE_FORMAT);
            TaskStatus status = fields.get(7).isEmpty() ? null : TaskStatus.valueOf(fields.get(7));

            return new AcademicTask(id, subjectId, subjectCode, title, notes, type, dueDate, status);

        } catch (Exception e) {
            System.out.println("Skipping row due to parse error: " + line + " (" + e.getMessage() + ")");
            return null;
        }
    }

    // Wraps a field in double quotes and escapes embedded quotes,
    // only when needed (field contains comma, quote, or newline).
    private static String escape(String field) {
        if (field == null) {
            return "";
        }
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }

    // Manual CSV parser that respects quoted fields (so commas/quotes
    // inside "notes" don't break column alignment).
    private static List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        current.append('"');
                        i++; // skip the escaped quote pair
                    } else {
                        inQuotes = false;
                    }
                } else {
                    current.append(c);
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                } else if (c == ',') {
                    fields.add(current.toString());
                    current.setLength(0);
                } else {
                    current.append(c);
                }
            }
        }
        fields.add(current.toString());

        return fields;
    }
}