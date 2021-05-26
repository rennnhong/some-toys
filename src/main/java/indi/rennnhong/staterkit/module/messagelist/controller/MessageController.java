package indi.rennnhong.staterkit.module.messagelist.controller;

import indi.rennnhong.staterkit.module.messagelist.controller.request.ui.AttachmentVO;
import indi.rennnhong.staterkit.module.messagelist.controller.request.ui.MessageVO;
import indi.rennnhong.staterkit.module.messagelist.entity.Attachment;
import indi.rennnhong.staterkit.module.messagelist.entity.Message;
import indi.rennnhong.staterkit.module.messagelist.repository.ImageRepository;
import indi.rennnhong.staterkit.module.messagelist.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    ImageRepository imageRepository;

//    @PostMapping("queries")
//    @ResponseBody
//    public DataTablesOutput getMessages(@Valid @RequestBody DataTablesInput input) {
//        return messageRepository.findAll(input, new Specification<Message>() {
//            @Override
//            public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                return null;
//            }
//        });
//    }
//
//    @PostMapping(value = "api/upload", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public ResponseEntity<String> saveMessage(HttpServletRequest servletRequest,
//                              @ModelAttribute MessageDTO messageDTO, BindingResult bindingResult) throws JsonProcessingException {
//
//        //获取上传的图片文件(可以多个文件)
//        List<MultipartFile> files = messageDTO.getFiles();
//
//        Message message = new Message();
//        message.setName(messageDTO.getName());
//        message.setDescription(messageDTO.getDescription());
//        message.setAttachments(new ArrayList<>());
//
//        //检验是否有文件？
//        if (null != files && files.size() > 0) {
//            //遍历
//            for (MultipartFile multipartFile : files) {
//
//                System.out.println("fileName: " + multipartFile.getOriginalFilename());
//                System.out.println("size: " + multipartFile.getSize());
//
//                Attachment attachment = new Attachment();
//                try {
//                    attachment.setFile(multipartFile.getBytes());
//                    attachment.setSize(multipartFile.getSize());
//                    attachment.setName(multipartFile.getOriginalFilename());
//                    message.getAttachments().add(attachment);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //获取应用/image虚拟路径在文件系统上对应的真实路径 + 文件名  并创建File对象
//                File imageFile = new File("/Users/kdcc-005/temp/", multipartFile.getOriginalFilename());
//                try {
//                    //将上传的文件保存到目标目录下
//                    multipartFile.transferTo(imageFile);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        messageRepository.save(message);
//
//        ObjectMapper mapper = new ObjectMapper();
//        ObjectNode result = mapper.createObjectNode();
//        result.put("message","success");
//        return ResponseEntity.ok(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
//    }

    @PostMapping
    public String doCreate(
            HttpServletRequest request,
            @Valid @ModelAttribute("formData") MessageVO formData,
            BindingResult bindingResult, Model model) {

        if (!bindingResult.hasErrors()) {
            return "forward:api/messages/upload";
        }
        return "page/message_fragments :: create_form";
    }

//    @GetMapping("{messageId}/images")
//    @ResponseBody
//    public List<UUID> getImagesId(@PathVariable("messageId") UUID messageId) {
//        Message message = messageRepository.findById(messageId).get();
//        List<Attachment> attachments = message.getAttachments();
//        return attachments.stream().map(attachment -> attachment.getId()).collect(Collectors.toList());
//    }

//    @GetMapping("{messageId}/images/{id}")
//    @ResponseBody
//    public ResponseEntity<Resource> getImage(@PathVariable("id") UUID id) throws UnsupportedEncodingException {
//        Attachment attachment = imageRepository.findById(id).get();
//        HttpHeaders header = new HttpHeaders();
//        header.add(HttpHeaders.CONTENT_DISPOSITION,
//                MessageFormat.format("attachment; filename={0}", URLEncoder.encode(attachment.getName(), StandardCharsets.UTF_8.name())));
//        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        header.add("Pragma", "no-cache");
//        header.add("Expires", "0");
//        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(attachment.getFile()));
//
//        return ResponseEntity.ok()
//                .headers(header)
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .body(resource);
//    }


    @GetMapping
    public String showDrawLots() {
        return "page/message_list";
    }

    @GetMapping("/fragment/create")
    public String getCreateFragment(Model model) {
        MessageVO formData = new MessageVO();
        model.addAttribute("formData", formData);
        return "page/message_fragments :: create_form";
    }

    @GetMapping("{messageId}/fragment/attachemts")
    public String getAttachmentsFragment(@PathVariable("messageId") UUID messageId, Model model) {
        Message message = messageRepository.findById(messageId).get();
        List<Attachment> attachments = message.getAttachments();

        List<AttachmentVO> attachmentVOS = attachments.stream().map(attachment -> {
            AttachmentVO attachmentVO = new AttachmentVO();
            attachmentVO.setName(attachment.getName());
            attachmentVO.setSize(attachment.getSize());
            attachmentVO.setLink(MessageFormat.format("api/messages/{0}/images/{1}", messageId, attachment.getId()));
            return attachmentVO;
        }).collect(Collectors.toList());

        model.addAttribute("attachments", attachmentVOS);
        return "page/message_fragments :: list_attachemts";
    }

}
