package top.billcat.mcpclientdemo.service;

import com.alibaba.cloud.ai.memory.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class AiServiceImpl implements AiService {
    private final ChatClient chatClient;
    private final MessageWindowChatMemory messageWindowChatMemory;

    public AiServiceImpl(ChatClient.Builder builder,
                         JdbcChatMemoryRepository jdbcChatMemoryRepository,
                         ToolCallbackProvider toolCallbackProvider) {
        this.messageWindowChatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(100).build();
        this.chatClient = builder
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(messageWindowChatMemory).build(),
                        new SimpleLoggerAdvisor()
                )
                .build();
    }

    @Override
    public Flux<String> complete(String message) {
        return this.chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }

    @Override
    public List<Message> messages(String conversationId) {
        return messageWindowChatMemory.get(conversationId);
    }
}
