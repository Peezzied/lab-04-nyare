import java.io.*;
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

    // Local prototype data storage
    private static final List<AcademicTask> tasks = new ArrayList<>();
    private static SystemData systemData;

    public static void main(String[] args) {
        // Load data on startup
        loadData();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMainMenu();
            System.out.print(" " + COLOR_BOLD + COLOR_ACCENT + "> " + COLOR_RESET + "\u001B[97m"); // Bright white for input
            String choice = scanner.nextLine().trim();
            System.out.print(COLOR_RESET);

            if (choice.equals("1")) {
                displayTwoWeekTasks();
            } else if (choice.equals("2")) {
                generateStudyPlan();
            } else if (choice.equals("3")) {
                displayCurrentTasks();
            } else if (choice.equals("4")) {
                manualSave();
            } else if (choice.equals("5")) {
                showAbout();
            } else if (choice.equals("6")) {
                if (exitProgram()) {
                    break;
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
        System.out.println(" " + "\u2500".repeat(HEADER_WIDTH));
        int spaces = (HEADER_WIDTH - title.length()) / 2;
        String format = " %" + spaces + "s%s";
        System.out.println(String.format(format, "", COLOR_BOLD + COLOR_ACCENT + title + COLOR_RESET));
        System.out.println(" " + "\u2500".repeat(HEADER_WIDTH));
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
        System.out.println(" " + COLOR_MUTED + "\u2500".repeat(MENU_WIDTH) + COLOR_RESET);
        System.out.println();
        System.out.println("        [" + COLOR_ACCENT + "1" + COLOR_RESET + "] \uD834\uDD1C Display 2-Week Tasks");
        System.out.println("        [" + COLOR_ACCENT + "2" + COLOR_RESET + "] ✦ Generate Study Plan from Notes");
        System.out.println("        [" + COLOR_ACCENT + "3" + COLOR_RESET + "] ◴ Display Current Tasks");
        System.out.println("        [" + COLOR_ACCENT + "4" + COLOR_RESET + "] ↩ Manual Save");
        System.out.println("        [" + COLOR_ACCENT + "5" + COLOR_RESET + "] ⓘ About");
        System.out.println("        [" + COLOR_ACCENT + "6" + COLOR_RESET + "] ➜] Exit");
        System.out.println();
        System.out.println(" " + COLOR_MUTED + "\u2500".repeat(MENU_WIDTH) + COLOR_RESET);
    }

    private static void displayTwoWeekTasks() {
        // Filter: Due today up to 14 days later (inclusive)
        LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOf14Days = LocalDateTime.now().toLocalDate().plusDays(14).atTime(23, 59, 59);

        List<AcademicTask> twoWeekTasks = new ArrayList<>();
        for (AcademicTask t : tasks) {
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
        for (AcademicTask t : tasks) {
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

        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            
            boolean triggerSearch = false;

            if (input.isEmpty()) {
                // Reveal the next line only, appending it directly without redrawing
                if (lastRevealedIndex + 1 < currentList.size()) {
                    lastRevealedIndex++;
                    AcademicTask nextTask = currentList.get(lastRevealedIndex);
                    System.out.println(formatTaskLine(nextTask));
                } else {
                    // At end of list, automatically trigger search
                    triggerSearch = true;
                }
            } else if (input.equals("q")) {
                break;
            } else if (input.equals("s")) {
                triggerSearch = true;
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
        info += " \u00B7 showing " + showingStart + "-" + showingEnd + " \u00B7 \u21B5 for more, " + COLOR_BOLD + "s" + COLOR_RESET + " to search, " + COLOR_BOLD + "q" + COLOR_RESET + " to stop";
        
        System.out.println("  " + info);
        System.out.println();
    }

    private static void printTableLabels() {
        String format = "  %-" + WIDTH_ID + "s   %-" + WIDTH_SUBJ + "s   %-" + WIDTH_TITLE + "s   %-" + WIDTH_TYPE + "s   %-" + WIDTH_DUE + "s   %-" + WIDTH_STATUS + "s";
        String labelLine = String.format(format, "ID", "SUBJECT", "TITLE", "TYPE", "DUE DATE", "STATUS");
        System.out.println(COLOR_BOLD + labelLine + COLOR_RESET);
        System.out.println(" " + COLOR_MUTED + "\u2500".repeat(TABLE_WIDTH) + COLOR_RESET);
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
        String title = padRight(truncate(t.getTitle(), WIDTH_TITLE), WIDTH_TITLE);
        
        String typeColor = COLOR_RESET;
        switch (t.getType()) {
            case ACTIVITY: typeColor = COLOR_CYAN; break;
            case PROJECT: typeColor = COLOR_MAGENTA; break;
            case ASSIGNMENT: typeColor = COLOR_BLUE; break;
            case EXAM: typeColor = COLOR_RED; break;
        }
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
        System.out.println("  Generating AI Study Plan...");

        // Count tasks with notes due today or in the next 14 days
        LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOf14Days = LocalDateTime.now().toLocalDate().plusDays(14).atTime(23, 59, 59);

        int taskCount = 0;
        for (AcademicTask t : tasks) {
            if (!t.getDueDate().isBefore(startOfToday) && !t.getDueDate().isAfter(endOf14Days)) {
                if (t.getNotes() != null && !t.getNotes().trim().isEmpty()) {
                    taskCount++;
                }
            }
        }

        System.out.println();
        System.out.println(" " + COLOR_GREEN + " [✔] Loaded today's notes (" + taskCount + " detected tasks)" + COLOR_RESET);
        System.out.println(" " + COLOR_GREEN + " [✔] Synthesized into a two week study plan!" + COLOR_RESET);
        System.out.println();
        System.out.println("  SUCCESS: Study Plan generated and loaded in background.");
        System.out.println(" " + COLOR_MUTED + "\u2500".repeat(HEADER_WIDTH) + COLOR_RESET);
        
        System.out.print(" " + COLOR_BOLD + COLOR_ACCENT + "> Press [Enter] to redirect to Display 2-Week Tasks: " + COLOR_RESET + "\u001B[97m");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        System.out.print(COLOR_RESET);

        // Redirect directly to option 1
        displayTwoWeekTasks();
    }

    private static void manualSave() {
        printHeader("MANUAL SAVE");
        System.out.println("  Saving system configuration...");
        System.out.println();

        // Recalculate SystemData stats before saving
        long maxId = 0;
        int activeCount = 0;
        for (AcademicTask t : tasks) {
            if (t.getId() > maxId) {
                maxId = t.getId();
            }
            if (t.getStatus() == TaskStatus.PENDING) {
                activeCount++;
            }
        }
        systemData.setLastTaskId(maxId);
        systemData.setActiveTasksCount(activeCount);

        String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        systemData.setLastSavedDate(nowStr);

        System.out.println(" " + COLOR_GREEN + "  [\u2714] Updating system state in memory" + COLOR_RESET);
        System.out.println(" " + COLOR_GREEN + "  [\u2714] Academic tasks list synced locally" + COLOR_RESET);
        System.out.println(" " + COLOR_MUTED + "      (File persistence disabled - handled by backend team)" + COLOR_RESET);

        System.out.println();
        System.out.println("  SUCCESS: Data saved successfully!");
        System.out.println(" " + COLOR_MUTED + "\u2500".repeat(HEADER_WIDTH) + COLOR_RESET);
        
        System.out.print(" " + COLOR_BOLD + COLOR_ACCENT + "> Press [Enter] to return to Main Menu: " + COLOR_RESET + "\u001B[97m");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        System.out.print(COLOR_RESET);
    }

    private static void showAbout() {
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
        System.out.println(" " + COLOR_MUTED + "\u2500".repeat(HEADER_WIDTH) + COLOR_RESET);
        
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
            System.out.println(" " + COLOR_MUTED + "\u2500".repeat(HEADER_WIDTH) + COLOR_RESET);
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
    private static String truncate(String s, int n) {
        if (s == null) return "";
        if (s.length() > n) {
            return s.substring(0, n - 3) + "...";
        }
        return s;
    }

    private static void loadData() {
        // Persistence temporarily disabled - handled by other developers
        systemData = new SystemData(
            0,
            "Never",
            0,
            "2026-2027",
            "1.0.0-poc",
            "Java Console (SE)",
            "Development"
        );

        // Seed with dummy tasks for the in-memory prototype
        seedTasks();
        
        // Perform stats recalculation
        long maxId = 0;
        int activeCount = 0;
        for (AcademicTask t : tasks) {
            if (t.getId() > maxId) {
                maxId = t.getId();
            }
            if (t.getStatus() == TaskStatus.PENDING) {
                activeCount++;
            }
        }
        systemData.setLastTaskId(maxId);
        systemData.setActiveTasksCount(activeCount);
    }

    private static void seedTasks() {
        LocalDateTime now = LocalDateTime.now();
//
//        // Task 1: Due today (for Today's Tasks display)
//        tasks.add(new AcademicTask(
//            1, 101, "CCS201", "Setup Git Repository",
//            "Initialize local git repository, configure gitignore, and commit the initial structure.",
//            TaskType.ACTIVITY, now.plusHours(2), TaskStatus.PENDING
//        ));
//
//        // Task 2: Due in 4 days (for 2-Week Tasks display)
//        tasks.add(new AcademicTask(
//            2, 101, "CCS201", "Console UI Prototype",
//            "Implement the console interface, including menu navigation, viewport scrolling, search, and ANSI colors.",
//            TaskType.PROJECT, now.plusDays(4), TaskStatus.PENDING
//        ));
//
//        // Task 3: Due in 9 days (for 2-Week Tasks display)
//        tasks.add(new AcademicTask(
//            3, 102, "MATH102", "Calculus Homework 3",
//            "Complete exercise set 3.2 on pages 120-125 of the textbook. Show all derivation steps clearly.",
//            TaskType.ASSIGNMENT, now.plusDays(9), TaskStatus.PENDING
//        ));
//
//        // Task 4: Due in 18 days (Excluded from 2-Week Tasks)
//        tasks.add(new AcademicTask(
//            4, 103, "COMP302", "Final Exam Review",
//            "Comprehensive study of graph algorithms, minimum spanning trees, and dynamic programming.",
//            TaskType.EXAM, now.plusDays(18), TaskStatus.PENDING
//        ));
//
//        // Additional dummy tasks to showcase viewport scrolling (total tasks > 10)
//        for (int i = 1; i <= 8; i++) {
//            tasks.add(new AcademicTask(
//                4 + i, 101, "GEN10" + i, "General Lecture Task " + i,
//                "This is dummy task " + i + " to demonstrate list scrolling behavior.",
//                TaskType.ACTIVITY, now.plusDays(1 + i), TaskStatus.PENDING
//            ));
//        }
    }
}