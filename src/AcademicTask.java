import java.time.LocalDateTime;

public class AcademicTask {

    private long id;
    private long subjectId;
    private String subjectCode;
    private String title;
    private String notes;
    private TaskType type;
    private LocalDateTime dueDate;
    private TaskStatus status;

    public AcademicTask() {
    }

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}