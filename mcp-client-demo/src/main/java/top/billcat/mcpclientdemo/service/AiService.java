package top.billcat.mcpclientdemo.service;

import org.springframework.ai.chat.messages.Message;
import reactor.core.publisher.Flux;

import java.util.List;

public interface AiService {
    Flux<String> complete(String message);
    List<Message> messages(String conversationId);
}
