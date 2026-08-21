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

    public static List<AcademicTask> getDummyData() {
        List<AcademicTask> list = new ArrayList<>();

        list.add(new AcademicTask(1, 101, "CCS 201", "Polymorphism Lab",
                "hala magpa-activity si maam bukas raw about polymorphism. kailangan daw matapos before end of class and submit sa LMS. medyo nalilito pako dun sa method overriding part pero re-read ko nlng notes mamaya",
                TaskType.ACTIVITY, LocalDateTime.of(2026, 8, 22, 10, 0), TaskStatus.PENDING));

        list.add(new AcademicTask(2, 103, "GEC 002", "Philippine Revolt Readings",
                "pabasa ni maam yung document about sa Cavite Mutiny. may oral recitation daw or short quiz next meeting. need to memorize dates and key people kundi yari nanaman",
                TaskType.ASSIGNMENT, LocalDateTime.of(2026, 8, 22, 13, 0), TaskStatus.COMPLETED));

        list.add(new AcademicTask(3, 105, "MATH 019A", "Integration Homework 1",
                "yung exercise set 2 sa Integration by substitution paki sagutan raw page 45 of book. submit by saturday evening. di ko pa nasisimulan huhu",
                TaskType.ASSIGNMENT, LocalDateTime.of(2026, 8, 22, 15, 30), TaskStatus.PENDING));

        list.add(new AcademicTask(4, 106, "CIT 306", "Figma Prototype Layout",
                "yung layout daw ng prototype dapat responsive sa android and ios. gawa muna mockups sa figma bago icode sa flutter. submit daw progress update by wednesday sabi ni ma'am.",
                TaskType.PROJECT, LocalDateTime.of(2026, 8, 22, 18, 0), TaskStatus.PENDING));

        list.add(new AcademicTask(5, 108, "GEE 002B", "IT Era Essay",
                "gawan ng reflection paper yung video tutorial about digital privacy and artificial intelligence. minimum of 500 words and submit as pdf sa portal tonight",
                TaskType.ACTIVITY, LocalDateTime.of(2026, 8, 22, 23, 59), TaskStatus.COMPLETED));

        // 20 more tasks spanning August 23 to mid-September, with detailed Taglish notes.
        list.add(new AcademicTask(6, 102, "CCS 202", "Syntax Trees Quiz",
                "Meron daw kaming test sa parses trees at regular expressions sa monday morning. reviewhin yung derivation rules kasi parang malilito ako sa ambiguity",
                TaskType.EXAM, LocalDateTime.of(2026, 8, 23, 9, 30), TaskStatus.PENDING));

        list.add(new AcademicTask(7, 104, "CCS 203", "MIPS Assembly Practice",
                "Sulat daw ng code sa assembly para sa calculator. basic addition and subraction lang naman pero kailangan gumana sa mars simulator. submit file by monday noon",
                TaskType.ACTIVITY, LocalDateTime.of(2026, 8, 24, 11, 0), TaskStatus.COMPLETED));

        list.add(new AcademicTask(8, 107, "PE 003", "PATHFit Exercise Plan",
                "Gumawa ng daily exercise plan na customized sa fitness goals natin base dun sa screening test last week. write in table format and upload before pathfit class sa tuesday",
                TaskType.ASSIGNMENT, LocalDateTime.of(2026, 8, 25, 14, 0), TaskStatus.PENDING));

        list.add(new AcademicTask(9, 101, "CCS 201", "Abstract Classes HW",
                "may assignment kami to differentiate abstract classes and interfaces. mag-cite daw ng scenarios when to use which. submit na raw before class on wednesday",
                TaskType.ASSIGNMENT, LocalDateTime.of(2026, 8, 26, 16, 0), TaskStatus.COMPLETED));

        list.add(new AcademicTask(10, 102, "CCS 202", "Grammar Analysis Proj",
                "yung group project natin kailangan gumawa ng custom grammar parser in java or python. code repository and documentation link paki-email sa instructor on thursday deadline",
                TaskType.PROJECT, LocalDateTime.of(2026, 8, 27, 23, 59), TaskStatus.PENDING));

        list.add(new AcademicTask(11, 105, "MATH 019A", "Trigo Integrals Quiz",
                "Trigonometric Integrals exam daw sa Friday. review methods lalo na pag non-basic forms. aralin yung techniques and standard formulas para di ma-zero",
                TaskType.EXAM, LocalDateTime.of(2026, 8, 28, 8, 0), TaskStatus.PENDING));

        list.add(new AcademicTask(12, 103, "GEC 002", "Primary Source Analysis",
                "analysis paper for the primary sources of historical events in philippines. pick one event like 1872 mutiny or cry of pugad lawin. double check citation style guidelines",
                TaskType.ASSIGNMENT, LocalDateTime.of(2026, 8, 29, 13, 0), TaskStatus.COMPLETED));

        list.add(new AcademicTask(13, 104, "CCS 203", "Cache Mapping Problems",
                "Cache mapping problems: direct mapping, associative, and set-associative calculations. may worksheets na binigay si sir, scan and upload hand-written computations by sunday",
                TaskType.ACTIVITY, LocalDateTime.of(2026, 8, 30, 10, 0), TaskStatus.PENDING));

        list.add(new AcademicTask(14, 106, "CIT 306", "State Management Setup",
                "State management using Provider or Riverpod sa flutter app natin. check raw kung responsive and clean code without redundant rebuilds. share github repo by end of august",
                TaskType.PROJECT, LocalDateTime.of(2026, 8, 31, 15, 0), TaskStatus.PENDING));

        list.add(new AcademicTask(15, 108, "GEE 002B", "Cybersecurity Forum",
                "post a response dun sa discussion board on Cybersecurity policies and threats in the Philippines. reply dynamically to at least two classmates' perspectives before tuesday midnight",
                TaskType.ACTIVITY, LocalDateTime.of(2026, 9, 1, 17, 0), TaskStatus.COMPLETED));

        list.add(new AcademicTask(16, 107, "PE 003", "PATHFit 3 Dance Video",
                "record dynamic aerobic dance routine showing basic steps of pathfit 3. minimum of 2 mins and maximum of 3 mins. upload to google drive and submit drive link sa form by wednesday",
                TaskType.PROJECT, LocalDateTime.of(2026, 9, 2, 23, 59), TaskStatus.PENDING));

        list.add(new AcademicTask(17, 101, "CCS 201", "Exception Handling Quiz",
                "short test raw on custom exceptions and try-catch-finally block rules sa thursday. review how exception propagation works in stack frame hierarchy",
                TaskType.EXAM, LocalDateTime.of(2026, 9, 3, 10, 30), TaskStatus.PENDING));

        list.add(new AcademicTask(18, 102, "CCS 202", "Scope Rules Exercises",
                "resolve static and dynamic scoping problems given in code snippets. compare how environment bindings change across different execution trees. submit sheet before Friday class",
                TaskType.ASSIGNMENT, LocalDateTime.of(2026, 9, 4, 14, 0), TaskStatus.COMPLETED));

        list.add(new AcademicTask(19, 105, "MATH 019A", "Partial Fractions HW",
                "Integration by Partial Fractions exercise sheet from module 4. make sure to expand algebraic fractions cleanly before integrating. submit scanned copy sa LMS by saturday",
                TaskType.ASSIGNMENT, LocalDateTime.of(2026, 9, 5, 23, 59), TaskStatus.PENDING));

        list.add(new AcademicTask(20, 104, "CCS 203", "Datapath Layout Quiz",
                "test sa control unit and datapath structures sa monday. draw simple single-cycle control line signals for select instructions like load word or jump branch",
                TaskType.EXAM, LocalDateTime.of(2026, 9, 7, 9, 0), TaskStatus.PENDING));

        list.add(new AcademicTask(21, 106, "CIT 306", "API Integration Demo",
                "Connect local flutter app to openweather or any public REST API. print weather details dynamically based on user input. live demo to instructor next tuesday class",
                TaskType.ACTIVITY, LocalDateTime.of(2026, 9, 8, 16, 0), TaskStatus.COMPLETED));

        list.add(new AcademicTask(22, 103, "GEC 002", "Agrarian Reform Review",
                "magbasa raw on land ownership models during spanish and american occupations. discuss policies in our group presentation on Wednesday morning. no late slides paki-cooperate guys",
                TaskType.ACTIVITY, LocalDateTime.of(2026, 9, 9, 11, 0), TaskStatus.PENDING));

        list.add(new AcademicTask(23, 108, "GEE 002B", "E-Waste Case Study",
                "case study analysis report on e-waste management in electronic hubs. outline key ecological problems and potential green computing solutions. submit draft by thursday night",
                TaskType.ASSIGNMENT, LocalDateTime.of(2026, 9, 10, 23, 59), TaskStatus.COMPLETED));

        list.add(new AcademicTask(24, 107, "PE 003", "Heart Rate Log",
                "weekly target heart rate calculation and logging activity sheet. record resting and maximum heart rate during workout sessions. submit signed workout log before friday ends",
                TaskType.ASSIGNMENT, LocalDateTime.of(2026, 9, 11, 15, 0), TaskStatus.PENDING));

        list.add(new AcademicTask(25, 101, "CCS 201", "OOP Final Project",
                "oop console task planner final project proposal. include class diagrams, data models, file handling layers, and console UI flow. repository must be fully documented on github",
                TaskType.PROJECT, LocalDateTime.of(2026, 9, 12, 23, 59), TaskStatus.PENDING));

        return list;
    }

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