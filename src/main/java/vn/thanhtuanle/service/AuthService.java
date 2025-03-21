package vn.thanhtuanle.service;

import vn.thanhtuanle.model.request.LoginRequest;
import vn.thanhtuanle.model.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest req);

    boolean introspect(String token);

    AuthResponse refreshToken(String refreshToken);
}
