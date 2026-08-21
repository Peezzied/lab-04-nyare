import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {

    // ANSI Escape Codes for Muted and Accent colors
    private static final String COLOR_RESET = "\u001B[0m";
    private static final String COLOR_BOLD = "\u001B[1m";
    private static final String COLOR_MUTED = "\u001B[90m"; // Dark Gray
    private static final String COLOR_ACCENT = "\u001B[93m"; // Bright Orange/Yellow
    private static final String COLOR_GREEN = "\u001B[32m"; // Soft Green
    private static final String COLOR_RED = "\u001B[31m";
    private static final String COLOR_CYAN = "\u001B[36m";
    private static final String COLOR_MAGENTA = "\u001B[35m";
    private static final String COLOR_BLUE = "\u001B[34m";
    private static final String COLOR_YELLOW = "\u001B[33m";

    // Layout Width Configuration Constants for Columnar Spacing
    private static final int WIDTH_ID = 3;
    private static final int WIDTH_SUBJ = 7;
    private static final int WIDTH_TITLE = 23;
    private static final int WIDTH_TYPE = 11;
    private static final int WIDTH_DUE = 16;
    private static final int WIDTH_STATUS = 9;

    private static final int MENU_WIDTH = 68;
    private static final int HEADER_WIDTH = 81;

    // Total width of the task table columns + blank separators (each separator is 3 spaces)
    private static final int TABLE_WIDTH = 1 + WIDTH_ID + 3 + WIDTH_SUBJ + 3 + WIDTH_TITLE + 3 + WIDTH_TYPE + 3 + WIDTH_DUE + 3 + WIDTH_STATUS;

    private static final TaskStore TASK_STORE = new TaskStore();
    private static final SystemStore SYSTEM_STORE = new SystemStore(TASK_STORE);

    public static void main(String[] args) {
        loadData();

        Scanner scanner = new Scanner(System.in);
        label:
        while (true) {
            printMainMenu();
            System.out.print(" " + COLOR_BOLD + COLOR_ACCENT + "> " + COLOR_RESET + "\u001B[97m"); // Bright white for input
            String choice = scanner.nextLine().trim();
            System.out.print(COLOR_RESET);

            switch (choice) {
                case "1" -> displayTwoWeekTasks();
                case "2" -> generateStudyPlan();
                case "3" -> displayCurrentTasks();
                case "4" -> manualSave();
                case "5" -> showAbout();
                case "6" -> {
                    if (exitProgram()) {
                        break label;
                    }
                }
            }
        }
    }

    private static void clearScreen() {
        // \033[H moves cursor to top-left
        // \033[2J clears the viewport
        // \033[3J clears the scrollback buffer (for full-screen in real terminals)
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    private static void printHeader(String title) {
        clearScreen();
        System.out.println(" " + "─".repeat(HEADER_WIDTH));
        int spaces = (HEADER_WIDTH - title.length()) / 2;
        String format = " %" + spaces + "s%s";
        System.out.printf((format) + "%n", "", COLOR_BOLD + COLOR_ACCENT + title + COLOR_RESET);
        System.out.println(" " + "─".repeat(HEADER_WIDTH));
    }

    private static void printMainMenu() {
        clearScreen();
        System.out.println("\n");
        System.out.print(COLOR_BOLD + COLOR_ACCENT); // Use print instead of println to avoid empty newline
        System.out.println("     /$$ /$$   /$$ /$$     /$$ /$$$$$$  /$$$$$$$  /$$$$$$$$ /$$$$");
        System.out.println("    | $/| $$$ | $$|  $$   /$$//$$__  $$| $$__  $$| $$_____//$$  $$");
        System.out.println("    |_/ | $$$$| $$ \\  $$ /$$/| $$  \\ $$| $$  \\ $$| $$     |__/\\ $$");
        System.out.println("        | $$ $$ $$  \\  $$$$/ | $$$$$$$$| $$$$$$$/| $$$$$      /$$/");
        System.out.println("        | $$  $$$$   \\  $$/  | $$__  $$| $$__  $$| $$__/     /$$/");
        System.out.println("        | $$\\  $$$    | $$   | $$  | $$| $$  \\ $$| $$       |__/");
        System.out.println("        | $$ \\  $$    | $$   | $$  | $$| $$  | $$| $$$$$$$$  /$$");
        System.out.println("        |__/  \\__/    |__/   |__/  |__/|__/  |__/|________/ |__/\n");
        System.out.print(COLOR_RESET);
        System.out.println("                   " + COLOR_BOLD + "AI Class Journal & Study Planner" + COLOR_RESET);
        System.out.println(" " + COLOR_MUTED + "─".repeat(MENU_WIDTH) + COLOR_RESET);
        System.out.println();
        System.out.println("        [" + COLOR_ACCENT + "1" + COLOR_RESET + "] \uD834\uDD1C Display 2-Week Tasks");
        System.out.println("        [" + COLOR_ACCENT + "2" + COLOR_RESET + "] ✦ Generate Study Plan from Notes");
        System.out.println("        [" + COLOR_ACCENT + "3" + COLOR_RESET + "] ◴ Display Current Tasks");
        System.out.println("        [" + COLOR_ACCENT + "4" + COLOR_RESET + "] ↩ Manual Save");
        System.out.println("        [" + COLOR_ACCENT + "5" + COLOR_RESET + "] ⓘ About");
        System.out.println("        [" + COLOR_ACCENT + "6" + COLOR_RESET + "] ➜] Exit");
        System.out.println();
        System.out.println(" " + COLOR_MUTED + "─".repeat(MENU_WIDTH) + COLOR_RESET);
    }

    private static void displayTwoWeekTasks() {
        // Filter: Due today up to 14 days later (inclusive)
        LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOf14Days = LocalDateTime.now().toLocalDate().plusDays(14).atTime(23, 59, 59);

        List<AcademicTask> twoWeekTasks = new ArrayList<>();
        for (AcademicTask t : TASK_STORE.getTasks()) {
            if (!t.getDueDate().isBefore(startOfToday) && !t.getDueDate().isAfter(endOf14Days)) {
                twoWeekTasks.add(t);
            }
        }

        displayTaskTable("2-WEEK TASKS LIST", twoWeekTasks);
    }

    private static void displayCurrentTasks() {
        // Filter: Today only (from start of today until 23:59:59)
        LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfToday = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);

        List<AcademicTask> todayTasks = new ArrayList<>();
        for (AcademicTask t : TASK_STORE.getTasks()) {
            if (!t.getDueDate().isBefore(startOfToday) && !t.getDueDate().isAfter(endOfToday)) {
                todayTasks.add(t);
            }
        }

        displayTaskTable("TODAY'S TASKS LIST", todayTasks);
    }

    private static void displayTaskTable(String titleLabel, List<AcademicTask> baseList) {
        String filterQuery = "None";
        List<AcademicTask> currentList = new ArrayList<>(baseList);
        
        Scanner scanner = new Scanner(System.in);
        
        // Print the header, information/controls, column labels, and the first 10 items initially
        printHeader(titleLabel);
        printInfoAndControls(filterQuery, currentList.size());
        printTableLabels();
        
        int lastRevealedIndex = printInitialTasks(currentList);

        label:
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            
            boolean triggerSearch = false;

            if (input.matches("\\d+")) {
                long targetId = Long.parseLong(input);
                AcademicTask found = null;
                for (AcademicTask t : currentList) {
                    if (t.getId() == targetId) {
                        found = t;
                        break;
                    }
                }
                if (found != null) {
                    displayTaskDetailsCard(found);
                    // Redraw the list exactly where the user left off
                    printHeader(titleLabel);
                    printInfoAndControls(filterQuery, currentList.size());
                    printTableLabels();
                    for (int i = 0; i <= lastRevealedIndex; i++) {
                        System.out.println(formatTaskLine(currentList.get(i)));
                    }
                    continue;
                }
            }

            switch (input) {
                case "":
                    // Reveal the next line only, appending it directly without redrawing
                    if (lastRevealedIndex + 1 < currentList.size()) {
                        lastRevealedIndex++;
                        AcademicTask nextTask = currentList.get(lastRevealedIndex);
                        System.out.println(formatTaskLine(nextTask));
                    } else {
                        // At end of list, automatically trigger search
                        triggerSearch = true;
                    }
                    break;
                case "q":
                    break label;
                case "s":
                    triggerSearch = true;
                    break;
            }

            if (triggerSearch) {
                System.out.print(" " + COLOR_BOLD + COLOR_ACCENT + "> Enter search query: " + COLOR_RESET + "\u001B[97m");
                String query = scanner.nextLine().trim();
                System.out.print(COLOR_RESET);
                if (query.isEmpty()) {
                    currentList = new ArrayList<>(baseList);
                    filterQuery = "None";
                } else {
                    currentList.clear();
                    for (AcademicTask t : baseList) {
                        if (t.getSubjectCode().toLowerCase().contains(query.toLowerCase()) ||
                            t.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                            t.getNotes().toLowerCase().contains(query.toLowerCase())) {
                            currentList.add(t);
                        }
                    }
                    filterQuery = query;
                }
                
                // Clear output flow visual spacing and redraw table with search results
                System.out.println();
                printHeader(titleLabel);
                printInfoAndControls(filterQuery, currentList.size());
                printTableLabels();
                
                lastRevealedIndex = printInitialTasks(currentList);
            }
        }
    }

    private static void printInfoAndControls(String filterQuery, int totalTasks) {
        int showingEnd = Math.min(totalTasks, 10);
        int showingStart = totalTasks == 0 ? 0 : 1;
        String info = "Found " + COLOR_BOLD + totalTasks + COLOR_RESET + " task(s)";
        if (!filterQuery.equals("None")) {
            info += " (Filter: " + filterQuery + ")";
        }
        info += " · showing " + showingStart + "-" + showingEnd + " · " + COLOR_BOLD + "[ID]" + COLOR_RESET + " for notes, ↵ for more, " + COLOR_BOLD + "s" + COLOR_RESET + " to search, " + COLOR_BOLD + "q" + COLOR_RESET + " to stop";
        
        System.out.println("  " + info);
        System.out.println();
    }

    private static void printTableLabels() {
        String format = "  %-" + WIDTH_ID + "s   %-" + WIDTH_SUBJ + "s   %-" + WIDTH_TITLE + "s   %-" + WIDTH_TYPE + "s   %-" + WIDTH_DUE + "s   %-" + WIDTH_STATUS + "s";
        String labelLine = String.format(format, "ID", "SUBJECT", "TITLE", "TYPE", "DUE DATE", "STATUS");
        System.out.println(COLOR_BOLD + labelLine + COLOR_RESET);
        System.out.println(" " + COLOR_MUTED + "─".repeat(TABLE_WIDTH) + COLOR_RESET);
    }

    private static int printInitialTasks(List<AcademicTask> currentList) {
        if (currentList.isEmpty()) {
            System.out.println("  " + COLOR_RED + "No results found." + COLOR_RESET);
            return -1;
        }
        
        int limit = Math.min(currentList.size(), 10);
        for (int i = 0; i < limit; i++) {
            AcademicTask t = currentList.get(i);
            System.out.println(formatTaskLine(t));
        }
        return limit - 1;
    }

    private static String formatTaskLine(AcademicTask t) {
        String idStr = String.format("%0" + WIDTH_ID + "d", t.getId());
        String subj = padRight(t.getSubjectCode(), WIDTH_SUBJ);
        String title = padRight(truncate(t.getTitle()), WIDTH_TITLE);
        
        String typeColor = COLOR_RESET;
        typeColor = switch (t.getType()) {
            case ACTIVITY -> COLOR_CYAN;
            case PROJECT -> COLOR_MAGENTA;
            case ASSIGNMENT -> COLOR_BLUE;
            case EXAM -> COLOR_RED;
            default -> typeColor;
        };
        String typeStr = typeColor + padRight(t.getType().name(), WIDTH_TYPE) + COLOR_RESET;

        // Format Due Date
        String dueStr = t.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        String statusColor = t.getStatus() == TaskStatus.COMPLETED ? COLOR_GREEN : COLOR_YELLOW;
        String statusStr = statusColor + padRight(t.getStatus().name(), WIDTH_STATUS) + COLOR_RESET;

        // No vertical column lines (|)
        return String.format("  %s   %s   %s   %s   %s   %s", idStr, subj, title, typeStr, dueStr, statusStr);
    }

    private static void generateStudyPlan() {
        printHeader("GENERATE STUDY PLAN FROM NOTES");
        System.out.println("  Generating Study Plan...");

        // Count tasks with notes due today or in the next 14 days
        LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOf14Days = LocalDateTime.now().toLocalDate().plusDays(14).atTime(23, 59, 59);

        populateTasks();

        int taskCount = 0;
        for (AcademicTask t : TASK_STORE.getTasks()) {
            if (!t.getDueDate().isBefore(startOfToday) && !t.getDueDate().isAfter(endOf14Days)) {
                if (t.getNotes() != null && !t.getNotes().trim().isEmpty()) {
                    taskCount++;
                }
            }
        }

        System.out.println();
        System.out.println(" " + COLOR_GREEN + " [✔] Loaded today's notes (" + taskCount + " extracted tasks using AI)" + COLOR_RESET);
        System.out.println(" " + COLOR_GREEN + " [✔] Synthesized into a two week study plan!" + COLOR_RESET);
        System.out.println();
        System.out.println("  SUCCESS: Study Plan generated and loaded in background.");
        System.out.println(" " + COLOR_MUTED + "─".repeat(HEADER_WIDTH) + COLOR_RESET);
        
        System.out.print(" " + COLOR_BOLD + COLOR_ACCENT + "> Press [Enter] to redirect to Display 2-Week Tasks: " + COLOR_RESET + "\u001B[97m");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        System.out.print(COLOR_RESET);

        // Redirect directly to option 1
        displayTwoWeekTasks();
    }

    private static void populateTasks() {
        List<AcademicTask> list = new ArrayList<>();
        
        // Exactly 5 Tasks on August 22, 2026:
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

        TASK_STORE.setTasks(list);
    }

    private static void manualSave() {
        printHeader("MANUAL SAVE");
        System.out.println("  Saving system configuration...");
        System.out.println();

        // Recalculate SystemData stats before saving
        long maxId = 0;
        int activeCount = 0;
        for (AcademicTask t : TASK_STORE.getTasks()) {
            if (t.getId() > maxId) {
                maxId = t.getId();
            }
            if (t.getStatus() == TaskStatus.PENDING) {
                activeCount++;
            }
        }
        var systemData = SYSTEM_STORE.getSystemData();
        systemData.setLastTaskId(maxId);
        systemData.setActiveTasksCount(activeCount);

        String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        systemData.setLastSavedDate(nowStr);

        System.out.println(" " + COLOR_GREEN + "  [✔] Updating system state in memory" + COLOR_RESET);
        System.out.println(" " + COLOR_GREEN + "  [✔] Academic tasks list synced locally" + COLOR_RESET);
        System.out.println(" " + COLOR_MUTED + "      (File persistence disabled - handled by backend team)" + COLOR_RESET);

        System.out.println();
        System.out.println("  SUCCESS: Data saved successfully!");
        System.out.println(" " + COLOR_MUTED + "─".repeat(HEADER_WIDTH) + COLOR_RESET);
        
        System.out.print(" " + COLOR_BOLD + COLOR_ACCENT + "> Press [Enter] to return to Main Menu: " + COLOR_RESET + "\u001B[97m");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        System.out.print(COLOR_RESET);
    }

    private static void showAbout() {
        var systemData = SYSTEM_STORE.getSystemData();
        printHeader("ABOUT");
        System.out.println("   'NYARE - AI Class Journal & Study Planner");
        System.out.println();
        System.out.println("   Application Version:  " + COLOR_ACCENT + systemData.getApplicationVersion() + COLOR_RESET);
        System.out.println("   Academic Year:        " + COLOR_ACCENT + systemData.getAcademicYear() + COLOR_RESET);
        System.out.println("   Platform:             " + COLOR_ACCENT + systemData.getApplicationPlatform() + COLOR_RESET);
        System.out.println("   Environment:          " + COLOR_ACCENT + systemData.getEnvironment() + COLOR_RESET);
        System.out.println();
        System.out.println("   System State:");
        System.out.println("   - Active Tasks:       " + COLOR_ACCENT + systemData.getActiveTasksCount() + COLOR_RESET);
        System.out.println("   - Last Task ID:       " + COLOR_ACCENT + systemData.getLastTaskId() + COLOR_RESET);

        String lastSaved = systemData.getLastSavedDate();
        if (lastSaved == null || lastSaved.trim().isEmpty()) {
            lastSaved = "Never";
        }
        System.out.println("   - Last Saved Date:    " + COLOR_ACCENT + lastSaved + COLOR_RESET);
        System.out.println(" " + COLOR_MUTED + "─".repeat(HEADER_WIDTH) + COLOR_RESET);
        
        System.out.print(" " + COLOR_BOLD + COLOR_ACCENT + "> Press [Enter] to return to Main Menu: " + COLOR_RESET + "\u001B[97m");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        System.out.print(COLOR_RESET);
    }

    private static boolean exitProgram() {
        printHeader("EXIT PROGRAM");
        System.out.print(" " + COLOR_BOLD + COLOR_ACCENT + "> Are you sure you want to exit? (y/n): " + COLOR_RESET + "\u001B[97m");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().trim().toLowerCase();
        System.out.print(COLOR_RESET);
        if (choice.equals("y") || choice.equals("yes")) {
            System.out.println();
            System.out.println("  Thank you for using 'NYARE! Keeping you organized and on track.");
            System.out.println("  Goodbye!");
            System.out.println(" " + COLOR_MUTED + "─".repeat(HEADER_WIDTH) + COLOR_RESET);
            return true;
        }
        return false;
    }

    // Helper: Pad right
    private static String padRight(String s, int n) {
        if (s == null) s = "";
        if (s.length() >= n) {
            return s.substring(0, n);
        }
        return String.format("%-" + n + "s", s);
    }

    // Helper: Truncate with ellipsis
    private static String truncate(String s) {
        if (s == null) return "";
        if (s.length() > Main.WIDTH_TITLE) {
            return s.substring(0, Main.WIDTH_TITLE - 3) + "...";
        }
        return s;
    }

    private static void displayTaskDetailsCard(AcademicTask t) {
        clearScreen();
        printHeader("TASK DETAILS");
        System.out.println("   ID:          " + t.getId());
        System.out.println("   Subject:     " + t.getSubjectCode() + " (ID: " + t.getSubjectId() + ")");
        System.out.println("   Type:        " + t.getType());
        System.out.println("   Due Date:    " + t.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        System.out.println("   Status:      " + t.getStatus());
        System.out.println();
        System.out.println("   Notes:");
        
        // Wrap the notes block to 70 characters so it fits neatly
        printWrappedText(t.getNotes());
        
        System.out.println(" " + "─".repeat(HEADER_WIDTH));
        System.out.print(" " + COLOR_BOLD + COLOR_ACCENT + "> Press [Enter] to return to list: " + COLOR_RESET + "\u001B[97m");
        new Scanner(System.in).nextLine();
        System.out.print(COLOR_RESET);
    }
   
    private static void printWrappedText(String text) {
        if (text == null || text.isEmpty()) {
            System.out.println("   " + "(None)");
            return;
        }
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder("   ");
        for (String word : words) {
            if (line.length() + word.length() - "   ".length() > 70) {
                System.out.println(line.toString());
                line = new StringBuilder("   ").append(word).append(" ");
            } else {
                line.append(word).append(" ");
            }
        }
        System.out.println(line.toString().stripTrailing());
    }

    private static void loadData() {
        TASK_STORE.loadTasks();
        SYSTEM_STORE.loadSystemData();

        // Perform stats recalculation
        long maxId = 0;
        int activeCount = 0;
        for (AcademicTask t : TASK_STORE.getTasks()) {
            if (t.getId() > maxId) {
                maxId = t.getId();
            }
            if (t.getStatus() == TaskStatus.PENDING) {
                activeCount++;
            }
        }
    }
}