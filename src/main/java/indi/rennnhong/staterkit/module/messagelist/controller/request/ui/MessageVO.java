package indi.rennnhong.staterkit.module.messagelist.controller.request.ui;

import indi.rennnhong.staterkit.module.messagelist.entity.Attachment;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class MessageVO {

    private UUID id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;

    private List<MultipartFile> files;

}
