package v1;

import com.papaya.cycleactivitylog.service.apis.services.PrimaryLoggedItemsApiService;
import com.papaya.cycleactivitylog.service.apis.v1.controllers.LoggedItemsApiController;
import com.papaya.cycleactivitylog.service.apis.v1.requests.Pageable;
import com.papaya.cycleactivitylog.service.data.LoggedItemsReadRepository;
import com.papaya.cycleactivitylog.service.model.LoggedItem;
import com.papaya.cycleactivitylog.service.model.LoggedItemEventType;
import com.papaya.cycleactivitylog.service.model.LoggedItemSeverity;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {
        LoggedItemsApiController.class,
        PrimaryLoggedItemsApiService.class
})
class LoggedItemsControllerTest {


    @Autowired
    private LoggedItemsApiController controller;

    @Test
    void one_item() throws Exception {
        var page = controller.searchByCycle(
                "1",
                List.of(),
                List.of(),
                List.of(),
                null,
                Pageable.builder().size(2).page(1).build()
        );

        Assertions.assertEquals(0, page.getPage());
        Assertions.assertEquals(0, page.getSize());

    }

}
