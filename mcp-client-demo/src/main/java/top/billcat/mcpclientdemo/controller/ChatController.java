package top.billcat.mcpclientdemo.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import top.billcat.mcpclientdemo.pojo.MessageDTO;


@RestController
@CrossOrigin(origins = "*")
public class ChatController {
    private final ChatClient dashScopeChatClient;
    private final ToolCallbackProvider toolCallbackProvider;

    public ChatController(ChatClient.Builder chatClientBuilder, ToolCallbackProvider toolCallbackProvider) {
        this.toolCallbackProvider = toolCallbackProvider;
        this.dashScopeChatClient = chatClientBuilder
                .defaultSystem("你是一个智能助手，你的输出为纯文本，而不是markdown")
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder().build()).build(),
                        new SimpleLoggerAdvisor()
                )
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .withTopP(0.7)
                                .build()
                )
                .build();
    }

    @PostMapping(value = "/chat")
    public Flux<String> advisorChat(HttpServletResponse response, @RequestBody MessageDTO messageDTO) {
        response.setCharacterEncoding("UTF-8");
        return this.dashScopeChatClient.prompt(messageDTO.getQuery())
                .advisors(
                        a -> a.param(ChatMemory.CONVERSATION_ID, messageDTO.getId())
                )
                .tools(toolCallbackProvider.getToolCallbacks())
                .stream()
                .content();
    }
}