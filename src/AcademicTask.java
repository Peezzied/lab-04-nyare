import java.time.LocalDateTime;

/**
 * Represents an academic task in the study planner system.
 * This is a data model class that holds information about subject IDs, subject codes,
 * task titles, detailed notes, task types, due dates, and completion status.
 */
public class AcademicTask {

    private long id;
    private long subjectId;
    private String subjectCode;
    private String title;
    private String notes;
    private TaskType type;
    private LocalDateTime dueDate;
    private TaskStatus status;

    /**
     * Default constructor for AcademicTask.
     */
    public AcademicTask() {
    }

    /**
     * Constructs a new AcademicTask with the specified properties.
     *
     * @param id          the unique identifier of the task
     * @param subjectId   the database ID of the associated course
     * @param subjectCode the catalog code of the course (e.g. CCS 201)
     * @param title       the brief description/header of the task
     * @param notes       the detailed instruction text, class diaries, or notes
     * @param type        the type of task (e.g. Activity, Exam)
     * @param dueDate     the deadline timestamp for completion
     * @param status      the status indicating PENDING or COMPLETED
     */
    public AcademicTask(
            long id,
            long subjectId,
            String subjectCode,
            String title,
            String notes,
            TaskType type,
            LocalDateTime dueDate,
            TaskStatus status) {

        this.id = id;
        this.subjectId = subjectId;
        this.subjectCode = subjectCode;
        this.title = title;
        this.notes = notes;
        this.type = type;
        this.dueDate = dueDate;
        this.status = status;
    }

    /**
     * Gets the unique identifier of this academic task.
     *
     * @return the task ID
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this academic task.
     *
     * @param id the task ID to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the ID of the course associated with this task.
     *
     * @return the subject database ID
     */
    public long getSubjectId() {
        return subjectId;
    }

    /**
     * Sets the ID of the course associated with this task.
     *
     * @param subjectId the subject database ID to set
     */
    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    /**
     * Gets the course catalog code of this task.
     *
     * @return the subject code string
     */
    public String getSubjectCode() {
        return subjectCode;
    }

    /**
     * Sets the course catalog code of this task.
     *
     * @param subjectCode the subject code string to set
     */
    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    /**
     * Gets the title of this academic task.
     *
     * @return the task title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this academic task.
     *
     * @param title the task title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the detailed notes, directions, or diary entry of this task.
     *
     * @return the task notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the detailed notes, directions, or diary entry of this task.
     *
     * @param notes the task notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Gets the type category of this task.
     *
     * @return the task type enum
     */
    public TaskType getType() {
        return type;
    }

    /**
     * Sets the type category of this task.
     *
     * @param type the task type enum to set
     */
    public void setType(TaskType type) {
        this.type = type;
    }

    /**
     * Gets the deadline timestamp for this task.
     *
     * @return the due date and time
     */
    public LocalDateTime getDueDate() {
        return dueDate;
    }

    /**
     * Sets the deadline timestamp for this task.
     *
     * @param dueDate the due date and time to set
     */
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Gets the current completion status of this task.
     *
     * @return the task status enum
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Sets the current completion status of this task.
     *
     * @param status the task status enum to set
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}