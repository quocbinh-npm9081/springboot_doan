package vn.eztek.springboot3starter.common.quartz.impl;

import com.google.gson.Gson;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.utils.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vn.eztek.springboot3starter.common.quartz.JobService;
import vn.eztek.springboot3starter.common.quartz.job.DeleteAccountJob;
import vn.eztek.springboot3starter.common.quartz.job.DeleteProjectJob;
import vn.eztek.springboot3starter.common.redis.messages.AccountDeleteMessage;
import vn.eztek.springboot3starter.common.redis.messages.ProjectDeleteMessage;
import vn.eztek.springboot3starter.common.redis.messages.VerifyAccountMessage;
import vn.eztek.springboot3starter.shared.util.BuildKeyQuartz;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

  private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);
  private final Gson gson;
  private final Scheduler scheduler;

  private static String getUniqueIdentity(String name) {
    return Key.createUniqueName(name);
  }

  @Override
  public void cancelSchedule(JobKey jobKey, TriggerKey triggerKey) throws SchedulerException {
    if (scheduler.checkExists(jobKey) && scheduler.checkExists(triggerKey)) {
      scheduler.unscheduleJob(triggerKey);
      scheduler.deleteJob(jobKey);
      log.info("Cancel job delete successfully");
    } else {
      log.error("Cannot find job or trigger");
      throw new RuntimeException("cannot-find-job-or-trigger");
    }
  }

  @Override
  public void schedulePermanentDeleteUser(AccountDeleteMessage message) throws SchedulerException {
//    Scheduler scheduler = schedulerFactoryBean.getScheduler();
    var id = message.getId();
    JobDetail job = JobBuilder.newJob(DeleteAccountJob.class)
        .withIdentity(BuildKeyQuartz.buildJobKey(BuildKeyQuartz.USER, id))
        .usingJobData("userId", id).usingJobData("username", message.getName())
        .usingJobData("userProjects", gson.toJson(message.getUserProjects())).build();

    Date triggerDate = DateBuilder.futureDate(30, IntervalUnit.DAY);
    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity(BuildKeyQuartz.buildTriggerKey(BuildKeyQuartz.USER, id)).startAt(triggerDate)
        .build();

    scheduler.scheduleJob(job, trigger);
    // TODO send notification to member project that delete

    log.info("Start job delete user with id {}", id);
  }


  @Override
  public void schedulePermanentDeleteProject(ProjectDeleteMessage message)
      throws SchedulerException {
    var projectId = message.getId();

    JobDetail job = JobBuilder.newJob(DeleteProjectJob.class)
        .withIdentity(BuildKeyQuartz.buildJobKey(BuildKeyQuartz.PROJECT, projectId))
        .usingJobData("userProjects", gson.toJson(message)).build();

    Date triggerDate = DateBuilder.futureDate(30, IntervalUnit.DAY);
    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity(BuildKeyQuartz.buildTriggerKey(BuildKeyQuartz.PROJECT, projectId))
        .startAt(triggerDate).build();

    scheduler.scheduleJob(job, trigger);

    // TODO send notification to member
    log.info("Start job delete project with id {}", projectId);
  }

  @Override
  public void scheduleUpdateStatusAccount(VerifyAccountMessage message) throws SchedulerException {

    JobDetail job = JobBuilder.newJob(DeleteProjectJob.class).withIdentity(
            BuildKeyQuartz.buildJobKey(BuildKeyQuartz.VERIFY_ACCOUNT, message.getUserId()))
        .usingJobData("userProjects", gson.toJson(message)).build();

    Date triggerDate = DateBuilder.futureDate(7, IntervalUnit.DAY);
    Trigger trigger = TriggerBuilder.newTrigger().withIdentity(
            BuildKeyQuartz.buildTriggerKey(BuildKeyQuartz.VERIFY_ACCOUNT, message.getUserId()))
        .startAt(triggerDate).build();

    scheduler.scheduleJob(job, trigger);

    // TODO send notification to member
    log.info("Start job update user status safe disable with user id {}", message.getUserId());
  }

}
