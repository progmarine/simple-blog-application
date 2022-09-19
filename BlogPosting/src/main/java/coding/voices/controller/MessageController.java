package coding.voices.controller;

import coding.voices.enums.EventType;
import coding.voices.enums.ObjectType;
import coding.voices.models.Message;
import coding.voices.repo.MessageRepo;
import coding.voices.util.WsSender;
import coding.voices.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;

@RestController
@RequestMapping("message")
public class MessageController {
    private final MessageRepo messageRepo;
    private final BiConsumer<EventType, Message> wsSender;

    @Autowired
    public MessageController(MessageRepo messageRepo, WsSender wsSender){
        this.messageRepo = messageRepo;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.IdName.class);
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Message> list() {
        return messageRepo.findAll();
    }

    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message getMessage(@PathVariable("id") Message message){
        return message;
    }

    @PostMapping
    public Message createMessage(@RequestBody Message message){
        message.setCreatedAt(LocalDateTime.now());
        Message updatedMessage = messageRepo.save(message);
        wsSender.accept(EventType.CREATE, updatedMessage);
        return updatedMessage;
    }

    @PutMapping("{id}")
    public Message updateMessage(@PathVariable("id") Message messageFromDb,
                                             @RequestBody Message message){
        BeanUtils.copyProperties(message, messageFromDb, "id");
        Message updatedMessage = messageRepo.save(messageFromDb);
        wsSender.accept(EventType.UPDATE, updatedMessage);
        return updatedMessage;
    }

    @DeleteMapping("{id}")
    public void deleteMessage(@PathVariable("id") Message message){
        messageRepo.delete(message);
        wsSender.accept(EventType.REMOVE, message);
    }

}
