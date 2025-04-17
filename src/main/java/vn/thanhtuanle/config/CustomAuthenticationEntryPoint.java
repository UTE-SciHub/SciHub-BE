package vn.thanhtuanle.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import vn.thanhtuanle.common.enums.ErrorCode;
import vn.thanhtuanle.model.response.BaseResponse;

import java.io.IOException;

import static vn.thanhtuanle.common.enums.ErrorCode.FORBIDDEN_ERROR;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        BaseResponse<?> responseData = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .code(FORBIDDEN_ERROR.getCode())
                .message(FORBIDDEN_ERROR.getMessage())
                .build();

        String jsonResponse = objectMapper.writeValueAsString(responseData);

        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(jsonResponse);
    }
}