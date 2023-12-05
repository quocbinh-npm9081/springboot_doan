package vn.eztek.springboot3starter.shared.util;

public class BuildPath {

  public static final String PROJECTS_FOLDER_NAME = "projects/%s/";
  public static final String TASKS_FOLDER_NAME = "tasks/%s/";
  public static final String COMMENTS_FOLDER_NAME = "comments/%s/";
  public static final String CSV_NAME = "key-emulator/%s.csv";

  public static String buildCsvName() {
    return CSV_NAME.formatted(GeneratorUtils.genNameCsv());
  }

  public static String build(String projectId, String taskId, String name) {
    String projectName = PROJECTS_FOLDER_NAME.formatted(projectId);
    String taskName = TASKS_FOLDER_NAME.formatted(taskId);
    return projectName + taskName + name;
  }


  public static String build(String projectId, String taskId, String commentId, String name) {
    String projectName = PROJECTS_FOLDER_NAME.formatted(projectId);
    String taskName = TASKS_FOLDER_NAME.formatted(taskId);
    String commentPath = COMMENTS_FOLDER_NAME.formatted(commentId);
    return projectName + taskName + commentPath + name;
  }

  public static String buildFolder(String projectId) {
    return PROJECTS_FOLDER_NAME.formatted(projectId);
  }

  public static String buildFolder(String projectId, String taskId) {
    String projectName = PROJECTS_FOLDER_NAME.formatted(projectId);
    String taskName = TASKS_FOLDER_NAME.formatted(taskId);
    return projectName + taskName;
  }

  public static String buildFolder(String projectId, String taskId, String commentId) {
    String projectName = PROJECTS_FOLDER_NAME.formatted(projectId);
    String taskName = TASKS_FOLDER_NAME.formatted(taskId);
    String commentPath = COMMENTS_FOLDER_NAME.formatted(commentId);
    return projectName + taskName + commentPath;
  }
}
