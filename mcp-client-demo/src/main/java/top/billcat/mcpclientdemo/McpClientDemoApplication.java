package top.billcat.mcpclientdemo;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import top.billcat.mcpclientdemo.service.AiService;

import java.util.List;

@SpringBootApplication
public class McpClientDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpClientDemoApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(List<McpSyncClient> clients, AiService aiService) {
        return args -> {
            // 测试 McpSyncClient
//            McpSyncClient mcpSyncClient = clients.get(0);
//            McpSchema.ListToolsResult listToolsResult = mcpSyncClient.listTools();
//            listToolsResult.tools().stream().map(tool -> tool.name()).forEach(System.out::println);
//
//            McpSchema.CallToolResult getCurrentDateTime = mcpSyncClient.callTool(new McpSchema.CallToolRequest("getCurrentDateTime", "{}"));
//            // TextContent[audience=null, priority=null, text="2025-06-23T13:40:45.720610+08:00[Asia/Shanghai]"]
//            getCurrentDateTime.content().stream().map(Object::toString).forEach(System.out::println);
//
//            String params= """
//                    {
//                     "a": "5",
//                     "b": "4"
//                    }
//                    """;
//            McpSchema.CallToolResult add = mcpSyncClient.callTool(new McpSchema.CallToolRequest("add", params));
//
//            // TextContent[audience=null, priority=null, text=9]
//            add.content().stream().map(Object::toString).forEach(System.out::println);

            // 使用AI Service
            aiService.complete("简短回答你的名字?").subscribe(System.out::println);
        };
    }
}
