package indi.rennnhong.staterkit.module.messagelist.repository;

import indi.rennnhong.staterkit.module.messagelist.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<Attachment, UUID>{

}
