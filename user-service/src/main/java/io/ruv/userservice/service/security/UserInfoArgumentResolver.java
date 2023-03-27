//package io.ruv.userservice.service.security;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.ruv.userservice.api.UserRole;
//import io.ruv.userservice.api.auth.UserInfo;
//import lombok.RequiredArgsConstructor;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.support.WebDataBinderFactory;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.method.support.ModelAndViewContainer;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.ArrayList;
//
//todo evict this class
//@RequiredArgsConstructor
//public class UserInfoArgumentResolver implements HandlerMethodArgumentResolver {
//
//    private final ObjectMapper objectMapper;
//
//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//
//        return UserInfo.class.equals(parameter.getParameterType());
//    }
//
//    @Override
//    public UserInfo resolveArgument(MethodParameter parameter,
//                                  ModelAndViewContainer mavContainer,
//                                  NativeWebRequest webRequest,
//                                  WebDataBinderFactory binderFactory) {
//
//        var username = webRequest.getHeader("Username");
//
//        if (username == null || username.isEmpty()) {
//
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
//
//        var rolesStr = webRequest.getHeader("User-Roles");
//
//        if (rolesStr == null || rolesStr.isEmpty()) {
//
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
//
//        try {
//            var roles = objectMapper.readValue(rolesStr, new TypeReference<ArrayList<UserRole>>() {
//            });
//
//            return new UserInfo(username, roles);
//        } catch (JsonProcessingException e) {
//
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
//    }
//}
