package vn.eztek.springboot3starter.common.quartz.job;

import com.google.gson.Gson;
import java.util.UUID;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.eztek.springboot3starter.common.redis.messages.ProjectDeleteMessage;
import vn.eztek.springboot3starter.common.storage.StorageService;
import vn.eztek.springboot3starter.domain.project.repository.ProjectRepository;
import vn.eztek.springboot3starter.shared.util.BuildPath;

public class DeleteProjectJob implements Job {

  private static final Logger log = LoggerFactory.getLogger(DeleteProjectJob.class);

  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private StorageService storageService;
  @Autowired
  private ProjectRepository projectRepository;
  @Autowired
  private Gson gson;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
    ProjectDeleteMessage message = gson.fromJson(jobDataMap.getString("userProjects"),
        ProjectDeleteMessage.class);

    var project = projectRepository.findById(UUID.fromString(message.getId()))
        .orElseThrow(() -> new RuntimeException("User not found"));
    if (project.getDeletedAt() == null) {
      return;
    }
    // call proc
    jdbcTemplate.update("CALL deletePermanentProject(?)", UUID.fromString(message.getId()));

    // delete all attachments involve projects
    storageService.deleteFolder(BuildPath.buildFolder(message.getId()));

    // TODO send notification for member project that delete

    log.info("Deleted project with id {}", message.getId());
  }
}
