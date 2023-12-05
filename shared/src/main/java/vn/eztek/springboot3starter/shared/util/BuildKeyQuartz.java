package vn.eztek.springboot3starter.shared.util;

public class BuildKeyQuartz {

  public static final String USER = "user";
  public static final String PROJECT = "project";
  public static final String VERIFY_ACCOUNT = "verify-account";
  private static final String TRIGGER = "trigger-%s-%s";
  private static final String JOB = "job-%s-%s";

  public static String buildJobKey(String type, String id) {
    return JOB.formatted(type, id);
  }

  public static String buildTriggerKey(String type, String id) {
    return TRIGGER.formatted(type, id);
  }
}
