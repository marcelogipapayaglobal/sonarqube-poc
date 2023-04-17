package service.apis.v1.requests;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Pageable {

    private @Min(0) int page = 0;

    private @Min(1) @Max(1000) int size;


}

