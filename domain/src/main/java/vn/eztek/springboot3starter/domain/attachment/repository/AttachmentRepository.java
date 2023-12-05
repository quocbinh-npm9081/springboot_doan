package vn.eztek.springboot3starter.domain.attachment.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.eztek.springboot3starter.domain.attachment.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {

  Boolean existsByNameContainingIgnoreCaseAndTaskId(String name, UUID taskId);

  List<Attachment> findAllByTaskId(UUID taskId);

}
