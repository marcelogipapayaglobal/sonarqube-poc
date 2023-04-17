package com.papaya.cycleactivitylog.service.data.sqs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.papaya.cycleactivitylog.service.data.Batch;
import com.papaya.cycleactivitylog.service.model.LoggedItem;
import lombok.SneakyThrows;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.List;

public class SqsLoggedItemsBatch implements Batch<LoggedItem> {
    private final SqsLoggedItemsChannel channel;

    private final ObjectMapper objectMapper;

    private List<Message> messages;

    public SqsLoggedItemsBatch(SqsLoggedItemsChannel provider, ObjectMapper objectMapper) {
        this.channel = provider;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<LoggedItem> getItems() {
        if (this.messages == null) {
            this.messages = this.channel.nextMessages();
        }
        return this.messages.stream()
                .map(this::deserialize)
                .toList();
    }

    @Override
    public boolean isEmpty() {
        return this.messages == null || this.messages.isEmpty();
    }

    @Override
    public void delete() {
        this.channel.deleteMessages(this.messages);
    }


    @SneakyThrows
    private LoggedItem deserialize(Message message) {
        var loggedItem = this.objectMapper.readValue(message.body(), LoggedItem.class);
        loggedItem.setHash(message.md5OfBody());
        return loggedItem;
    }

}
