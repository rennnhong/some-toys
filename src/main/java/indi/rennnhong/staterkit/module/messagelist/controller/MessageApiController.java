package indi.rennnhong.staterkit.module.messagelist.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import indi.rennnhong.staterkit.module.messagelist.controller.request.MessageDTO;
import indi.rennnhong.staterkit.module.messagelist.entity.Attachment;
import indi.rennnhong.staterkit.module.messagelist.entity.Message;
import indi.rennnhong.staterkit.module.messagelist.repository.ImageRepository;
import indi.rennnhong.staterkit.module.messagelist.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/messages")
public class MessageApiController {

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    ImageRepository imageRepository;

    @PostMapping("queries")
    public DataTablesOutput getMessages(@Valid @RequestBody DataTablesInput input) {
        return messageRepository.findAll(input, new Specification<Message>() {
            @Override
            public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return null;
            }
        });
    }

    @PostMapping(value = "upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveMessage(HttpServletRequest servletRequest,
                              @ModelAttribute MessageDTO messageDTO, BindingResult bindingResult) throws JsonProcessingException {

        //获取上传的图片文件(可以多个文件)
        List<MultipartFile> files = messageDTO.getFiles();

        Message message = new Message();
        message.setName(messageDTO.getName());
        message.setDescription(messageDTO.getDescription());
        message.setAttachments(new ArrayList<>());

        //检验是否有文件？
        if (null != files && files.size() > 0) {
            //遍历
            for (MultipartFile multipartFile : files) {

//                System.out.println("fileName: " + multipartFile.getOriginalFilename());
//                System.out.println("size: " + multipartFile.getSize());

                Attachment attachment = new Attachment();
                try {
                    attachment.setFile(multipartFile.getBytes());
                    attachment.setSize(multipartFile.getSize());
                    attachment.setName(multipartFile.getOriginalFilename());
                    message.getAttachments().add(attachment);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //获取应用/image虚拟路径在文件系统上对应的真实路径 + 文件名  并创建File对象
                File imageFile = new File("/Users/kdcc-005/temp/", multipartFile.getOriginalFilename());
                try {
                    //将上传的文件保存到目标目录下
                    multipartFile.transferTo(imageFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        messageRepository.save(message);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        result.put("message","success");
        return ResponseEntity.ok(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
    }

    @GetMapping("{messageId}/images/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable("id") UUID id) throws UnsupportedEncodingException {
        Attachment attachment = imageRepository.findById(id).get();
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION,
                MessageFormat.format("attachment; filename={0}", URLEncoder.encode(attachment.getName(), StandardCharsets.UTF_8.name())));
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(attachment.getFile()));

        return ResponseEntity.ok()
                .headers(header)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

}
