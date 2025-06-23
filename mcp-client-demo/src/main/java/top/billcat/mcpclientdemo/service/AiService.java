package top.billcat.mcpclientdemo.service;

import reactor.core.publisher.Flux;

public interface AiService {
    Flux<String> complete(String message);
}
