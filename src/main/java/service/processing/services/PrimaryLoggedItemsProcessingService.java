package service.processing.services;

import com.papaya.cycleactivitylog.service.data.LoggedItemsChannel;
import com.papaya.cycleactivitylog.service.data.LoggedItemsWriteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PrimaryLoggedItemsProcessingService implements LoggedItemsProcessingService {

    private final LoggedItemsChannel channel;

    private final LoggedItemsWriteRepository repository;

    @Autowired
    public PrimaryLoggedItemsProcessingService(LoggedItemsChannel channel, LoggedItemsWriteRepository repository) {
        this.channel = channel;
        this.repository = repository;
    }

    @Override
    public void processLoggedItems() {
        log.info(">>>>>>>> Starting to process a new batch of items <<<<<<<<");
        var batch = this.channel.nextBatch();

        log.info("Consuming batch from channel");
        var items = batch.getItems();
        log.info("Consumed {} items from channel: {}", items.size(), items);

        log.info("Saving {} items to repository: {}", items.size(), items);
        this.repository.save(batch);
        log.info("Saved {} items to repository: {}", items.size(), items);

        log.info("Deleting {} items from channel: {}", items.size(), items);
        batch.delete();
        log.info("Deleted {} items from channel: {}", items.size(), items);
    }

}
