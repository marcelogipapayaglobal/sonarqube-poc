package service.processing.controllers;

import com.papaya.cycleactivitylog.service.processing.services.LoggedItemsProcessingService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@EnableAsync
@Slf4j
public class LoggedItemsProcessingController {

    private final LoggedItemsProcessingService service;

    @Autowired
    public LoggedItemsProcessingController(LoggedItemsProcessingService service) {
        this.service = service;
    }

    @PostConstruct
    public void init() {
        CompletableFuture.runAsync(this::consume);
    }

    protected void consume() {
        while (true) try {
            this.service.processLoggedItems();
        } catch (Exception e) {
            log.error("Error processing logged items", e);
        }
    }
}
