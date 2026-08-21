public class SystemData {

    private long lastTaskId;
    private String lastSavedDate;
    private int activeTasksCount;
    private String academicYear;
    private String applicationVersion;
    private String applicationPlatform;
    private String environment;

    public SystemData() {
    }

    public SystemData(
            long lastTaskId,
            String lastSavedDate,
            int activeTasksCount,
            String academicYear,
            String applicationVersion,
            String applicationPlatform,
            String environment) {

        this.lastTaskId = lastTaskId;
        this.lastSavedDate = lastSavedDate;
        this.activeTasksCount = activeTasksCount;
        this.academicYear = academicYear;
        this.applicationVersion = applicationVersion;
        this.applicationPlatform = applicationPlatform;
        this.environment = environment;
    }

    public long getLastTaskId() {
        return lastTaskId;
    }

    public void setLastTaskId(long lastTaskId) {
        this.lastTaskId = lastTaskId;
    }

    public String getLastSavedDate() {
        return lastSavedDate;
    }

    public void setLastSavedDate(String lastSavedDate) {
        this.lastSavedDate = lastSavedDate;
    }

    public int getActiveTasksCount() {
        return activeTasksCount;
    }

    public void setActiveTasksCount(int activeTasksCount) {
        this.activeTasksCount = activeTasksCount;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getApplicationPlatform() {
        return applicationPlatform;
    }

    public void setApplicationPlatform(String applicationPlatform) {
        this.applicationPlatform = applicationPlatform;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}