/**
 * Represents system-level metadata, configuration, and diagnostics for the study planner.
 * This class tracks task metrics and keeps information about the environment, platform,
 * database keys, and save dates.
 */
public class SystemData {

    private long lastTaskId;
    private String lastSavedDate;
    private int activeTasksCount;
    private String academicYear;
    private String applicationVersion;
    private String applicationPlatform;
    private String environment;

    /**
     * Default constructor for SystemData.
     */
    public SystemData() {
    }

    /**
     * Constructs a new SystemData metadata record with the specified properties.
     *
     * @param lastTaskId          the last assigned numerical ID in the task repository
     * @param lastSavedDate       the formatted timestamp of the last local save operation
     * @param activeTasksCount    the number of pending tasks remaining in the system
     * @param academicYear        the current target academic year (e.g. 2026-2027)
     * @param applicationVersion the current semver release version
     * @param applicationPlatform the operating environment platform identifier
     * @param environment          the runtime environment stage (e.g. development, production)
     */
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

    /**
     * Gets the last assigned database task ID.
     *
     * @return the last task ID
     */
    public long getLastTaskId() {
        return lastTaskId;
    }

    /**
     * Sets the last assigned database task ID.
     *
     * @param lastTaskId the last task ID to set
     */
    public void setLastTaskId(long lastTaskId) {
        this.lastTaskId = lastTaskId;
    }

    /**
     * Gets the formatted string representation of the last manual save date.
     *
     * @return the last saved date string
     */
    public String getLastSavedDate() {
        return lastSavedDate;
    }

    /**
     * Sets the formatted string representation of the last manual save date.
     *
     * @param lastSavedDate the last saved date string to set
     */
    public void setLastSavedDate(String lastSavedDate) {
        this.lastSavedDate = lastSavedDate;
    }

    /**
     * Gets the count of active (pending) tasks currently tracked.
     *
     * @return the active tasks count
     */
    public int getActiveTasksCount() {
        return activeTasksCount;
    }

    /**
     * Sets the count of active (pending) tasks currently tracked.
     *
     * @param activeTasksCount the active tasks count to set
     */
    public void setActiveTasksCount(int activeTasksCount) {
        this.activeTasksCount = activeTasksCount;
    }

    /**
     * Gets the current academic year settings.
     *
     * @return the academic year string
     */
    public String getAcademicYear() {
        return academicYear;
    }

    /**
     * Sets the current academic year settings.
     *
     * @param academicYear the academic year string to set
     */
    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    /**
     * Gets the application release version.
     *
     * @return the application version
     */
    public String getApplicationVersion() {
        return applicationVersion;
    }

    /**
     * Sets the application release version.
     *
     * @param applicationVersion the application version to set
     */
    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    /**
     * Gets the target platform identifier.
     *
     * @return the application platform
     */
    public String getApplicationPlatform() {
        return applicationPlatform;
    }

    /**
     * Sets the target platform identifier.
     *
     * @param applicationPlatform the application platform to set
     */
    public void setApplicationPlatform(String applicationPlatform) {
        this.applicationPlatform = applicationPlatform;
    }

    /**
     * Gets the runtime configuration stage environment.
     *
     * @return the runtime environment
     */
    public String getEnvironment() {
        return environment;
    }

    /**
     * Sets the runtime configuration stage environment.
     *
     * @param environment the runtime environment to set
     */
    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}