package com.papaya.cycleactivitylog.service.apis.v1;

import com.papaya.cycleactivitylog.service.apis.services.PrimaryLoggedItemsApiService;
import com.papaya.cycleactivitylog.service.apis.v1.controllers.LoggedItemsApiController;
import com.papaya.cycleactivitylog.service.apis.v1.requests.Pageable;
import com.papaya.cycleactivitylog.service.apis.v1.requests.PeriodRequest;
import com.papaya.cycleactivitylog.service.data.LoggedItemsReadRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {
        LoggedItemsApiController.class,
        PrimaryLoggedItemsApiService.class,
})
class LoggedItemsControllerTest {

    @MockBean
    private LoggedItemsReadRepository readRepository;

    @Autowired
    private LoggedItemsApiController controller = new LoggedItemsApiController(new PrimaryLoggedItemsApiService(readRepository));

    @Test
    void one_item() throws Exception {
        var page = controller.searchByCycle(
                "1",
                List.of(),
                List.of(),
                List.of(),
                PeriodRequest.builder().build(),
                Pageable.builder().size(2).page(1).build()
        );

        Assertions.assertEquals(1, page.getPage());
        Assertions.assertEquals(2, page.getSize());

    }

}
