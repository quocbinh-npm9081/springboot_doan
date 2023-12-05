package vn.eztek.springboot3starter.common.quartz;

import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import vn.eztek.springboot3starter.common.redis.messages.AccountDeleteMessage;
import vn.eztek.springboot3starter.common.redis.messages.ProjectDeleteMessage;
import vn.eztek.springboot3starter.common.redis.messages.VerifyAccountMessage;

public interface JobService {

  void cancelSchedule(JobKey jobKey, TriggerKey triggerKey) throws SchedulerException;

  void schedulePermanentDeleteUser(AccountDeleteMessage user) throws SchedulerException;

  void schedulePermanentDeleteProject(ProjectDeleteMessage project) throws SchedulerException;

  void scheduleUpdateStatusAccount(VerifyAccountMessage project) throws SchedulerException;

}
