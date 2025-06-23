package top.billcat.mcpclientdemo.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import top.billcat.mcpclientdemo.pojo.MessageDTO;
import top.billcat.mcpclientdemo.service.AiService;


@RestController
@CrossOrigin(origins = "*")
public class ChatController {
    private final AiService aiService;

    public ChatController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping(value = "/chat")
    public Flux<String> advisorChat(HttpServletResponse response, @RequestBody MessageDTO messageDTO) {
        response.setCharacterEncoding("UTF-8");
        // .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, messageDTO.getId()))
        return aiService.complete(messageDTO.getQuery());
    }
}