package edu.exception;

import edu.dto.error.DtoErrorResponse;
import edu.dto.error.DtoErrorResponseElement;
import edu.error.*;
import edu.configuration.ApplicationConfig;
import edu.util.mapper.ErrorElementMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final ApplicationConfig appConfig;
    private final ErrorElementMapper errorElementMapper;

    @Autowired
    public GlobalExceptionHandler(ApplicationConfig appConfig, ErrorElementMapper errorElementMapper) {
        this.appConfig = appConfig;
        this.errorElementMapper = errorElementMapper;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)//400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public DtoErrorResponse validationDtoRequest(HttpServletRequest request, MethodArgumentNotValidException ex) {
        DtoErrorResponse dto = new DtoErrorResponse();
        BindingResult result = ex.getBindingResult();

        List<FieldError> resultFieldErrors = result.getFieldErrors();
        for (FieldError fe : resultFieldErrors) {
            if (fe != null) {
                dto.addError(createError(fe));
            }
        }

        List<ObjectError> objectErrors = ex.getGlobalErrors();
        for (ObjectError objectError : objectErrors) {
            if (objectError != null) {
                dto.addError(createError(objectError));
            }
        }

        return dto;
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<DtoErrorResponse> handleApplicationException(ApplicationException e) {
        DtoErrorResponse dto = new DtoErrorResponse();
        DtoErrorResponseElement dtoElement =  errorElementMapper.toDto(e.getErrorElement());
        dto.addError(dtoElement);

        return new ResponseEntity<>(dto, e.getHttpErrorStatus());
    }

    private DtoErrorResponseElement createError(FieldError fe) {
        DtoErrorResponseElement dtoErrorResponseElement = new DtoErrorResponseElement();
        Object[] objects = fe.getArguments();
        if (objects != null) {
            for (Object o : objects) {
                if (o instanceof ApplicationErrorDetails) {
                    dtoErrorResponseElement.setErrorCode(
                            o.toString()
                    );
                    dtoErrorResponseElement.setField(
                            ((ApplicationErrorDetails) o).getField()
                    );
                    dtoErrorResponseElement.setMessage(
                            ((ApplicationErrorDetails) o).getMessage()
                    );
                }
            }
        }

        return dtoErrorResponseElement;
    }

    private DtoErrorResponseElement createError(ObjectError objectError) {
        DtoErrorResponseElement dtoErrorResponseElement = new DtoErrorResponseElement();
        Object[] objects = objectError.getArguments();
        if (objects != null) {
            for (Object o : objects) {
                if (o instanceof ApplicationErrorDetails) {
                    dtoErrorResponseElement.setErrorCode(
                            o.toString()
                    );
                    dtoErrorResponseElement.setField(
                            ((ApplicationErrorDetails) o).getField()
                    );
                    dtoErrorResponseElement.setMessage(
                            ((ApplicationErrorDetails) o).getMessage()
                    );
                }
            }
        }

        return dtoErrorResponseElement;
    }
}
