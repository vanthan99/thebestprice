package dtu.thebestprice.exceptions;

import dtu.thebestprice.payload.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception ex) {
        // quá trình kiểm soat lỗi diễn ra ở đây
        return ResponseEntity.status(400).body(new ApiResponse(false, ex.getLocalizedMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> undeclaredThrowableException(RuntimeException e){
        return ResponseEntity.status(400).body(new ApiResponse(false, e.getMessage()));
    }

    // lỗi 403
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedEx(AccessDeniedException ex) {
        return ResponseEntity.status(403).body(new ApiResponse(false,"Truy cập bị từ chối. bạn không đủ quyền"));
    }



    /*
    * xử lý ngoại lệ hibernate validator
    *
    * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException e){
        List<String> errors = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach(item -> {
            errors.add(item.getDefaultMessage());
        });
        return ResponseEntity.status(400).body(new ApiResponse(false, errors.toString()));
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Object> numberFormatException(NumberFormatException e){
        return ResponseEntity.status(400).body(new ApiResponse(false,e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> httpMessageNotReadableException(HttpMessageNotReadableException e){
        return ResponseEntity.status(400).body(new ApiResponse(false,e.getMessage()));
    }

}
