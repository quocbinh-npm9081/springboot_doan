package vn.eztek.springboot3starter.common.quartz.job;

import java.util.UUID;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;

public class UpdateStatusAccountJob implements Job {

  private static final Logger log = LoggerFactory.getLogger(UpdateStatusAccountJob.class);

  @Autowired
  private UserRepository userRepository;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
    var userId = UUID.fromString(jobDataMap.getString("userId"));
    var user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("cannot-find-user-with-id %s".formatted(userId)));

    if (!user.getStatus().equals(UserStatus.ACTIVE)) {
      throw new RuntimeException("user-not-already-active");
    }

    user.setStatus(UserStatus.SAFE_DISABLE);
    userRepository.save(user);

    log.info("User with id {} has been safe disable", userId);
  }
}
