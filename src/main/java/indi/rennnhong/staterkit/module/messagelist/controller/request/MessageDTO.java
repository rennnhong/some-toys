package indi.rennnhong.staterkit.module.messagelist.controller.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class MessageDTO {

    @NotNull
    @Size(min=1, max=10)
    private String name;

    private String description;

    private long price;

    private List<MultipartFile> files;

}
