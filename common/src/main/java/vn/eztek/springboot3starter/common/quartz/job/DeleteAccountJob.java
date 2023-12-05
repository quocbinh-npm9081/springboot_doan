package vn.eztek.springboot3starter.common.quartz.job;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.eztek.springboot3starter.common.email.EmailService;
import vn.eztek.springboot3starter.common.property.SendGridProperties;
import vn.eztek.springboot3starter.common.storage.StorageService;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.project.repository.ProjectRepository;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.shared.util.BuildPath;

public class DeleteAccountJob implements Job {

  private static final Logger log = LoggerFactory.getLogger(DeleteAccountJob.class);

  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private ProjectRepository projectRepository;
  @Autowired
  private StorageService storageService;
  @Autowired
  private EmailService emailService;
  @Autowired
  private SendGridProperties sendGridProperties;
  @Autowired
  private UserRepository userRepository;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
    var userId = UUID.fromString(jobDataMap.getString("userId"));

    var user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
    if (user.getDeletedAt() == null) {
      return;
    }
    List<Project> projects = projectRepository.findByOwnerId(userId);

    // call proc
    jdbcTemplate.update("CALL deletePermanentAccount(?)", userId);

    // delete all attachments involve projects
    projects.forEach(project -> {
      storageService.deleteFolder(BuildPath.buildFolder(project.getId().toString()));
    });

    // send email
    String username = jobDataMap.getString("username");
    var templateData = new HashMap<String, String>();
    templateData.put("name", user.getFirstName() + " " + user.getLastName());
    emailService.sendHtmlMessage(username,
        sendGridProperties.getDynamicTemplateId().getNotificationCompleteDeleteAccount(),
        templateData);

    // TODO send notification for member project that delete
    log.info("Deleted user with id {}", userId);
  }
}
