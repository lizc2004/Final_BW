package noemicoppotelli.finalbuildweek.exceptions;

import noemicoppotelli.finalbuildweek.payloads.ErrorDTO;
import noemicoppotelli.finalbuildweek.payloads.ErrorListDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleAccessDenied(AccessDeniedException ex) {
        return new ErrorDTO("Accesso negato: permessi insufficienti", LocalDateTime.now());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorListDTO handleValidation(ValidationException ex) {
        return new ErrorListDTO(ex.getMessage(), LocalDateTime.now(), ex.getErrorsList());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadRequest(BadRequestException ex) {
        return new ErrorDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFound(NotFoundException ex) {
        return new ErrorDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleGeneralException(Exception ex) {
        ex.printStackTrace();
        return new ErrorDTO("ERRORE INTRENO AL SERVER", LocalDateTime.now());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleUnauthorized(UnauthorizedException ex) {
        return new ErrorDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorListDTO handleValidationException(
            MethodArgumentNotValidException ex
    ) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField() + ": " + error.getDefaultMessage()
                )
                .toList();


        return new ErrorListDTO(
                "Errore di validazione", LocalDateTime.now(), errors);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleMessageNotReadable(HttpMessageNotReadableException ex) {
        return new ErrorDTO("Il corpo della richiesta è mancante o malformato", LocalDateTime.now());
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleMissingPart(MissingServletRequestPartException ex) {
        return new ErrorDTO("Parte mancante nella richiesta: " + ex.getRequestPartName(), LocalDateTime.now());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorListDTO> handleBadCredentials(
            BadCredentialsException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        new ErrorListDTO(
                                "Credenziali non valide",
                                LocalDateTime.now(),
                                List.of(ex.getMessage())
                        )
                );
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorListDTO handleDataIntegrityViolation(
            DataIntegrityViolationException ex
    ) {
        return new ErrorListDTO(
                "Violazione dei vincoli del database", LocalDateTime.now(), List.of(ex.getMessage())
        );
    }
}

