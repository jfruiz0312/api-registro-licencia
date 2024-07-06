package sv.gob.vmt.api.licencia.exceptions;


import jakarta.validation.ConstraintViolationException;
import sv.gob.vmt.api.licencia.enums.Error;
import sv.gob.vmt.api.licencia.enums.ErrorDetail;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ControllerAdviceErrorExceptionHandler {


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Error> handleConstraintViolationException(ConstraintViolationException ex){
        List<ErrorDetail> details =
                ex.getConstraintViolations().stream()
                        .map(e -> new ErrorDetail(e.getPropertyPath().toString().replace("LicenciaREST", ""), e.getMessage()))
                        .toList();
        return new ResponseEntity<>(new Error("Validation failed for argument", details), BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> methodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<ErrorDetail> details =ex.getBindingResult().getFieldErrors()
        		.stream()
        		.map(e-> new ErrorDetail(e.getField(), e.getDefaultMessage()))
        		.toList();
        return new ResponseEntity<>(new Error("Error de validación de datos...", details), BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Error> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        List<ErrorDetail> details=new ArrayList<ErrorDetail>();
        details.add(new ErrorDetail("", ex.toString()));

        return new ResponseEntity<>(new Error("Error de violación de integridad de datos", details), BAD_REQUEST);
    }
}