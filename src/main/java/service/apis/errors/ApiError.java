package service.apis.errors;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Collections;
import java.util.List;

@Setter
@Getter
public class ApiError {
    private HttpStatus status;
    private List<String> errors;

    public ApiError(HttpStatusCode statusCode, String error) {
        this(HttpStatus.resolve(statusCode.value()), error);
    }

    public ApiError(HttpStatus status, String error) {
        this(status, Collections.singletonList(error));
    }

    public ApiError(HttpStatus status, List<String> errors) {
        this.status = status;
        this.errors = errors;
    }
}
