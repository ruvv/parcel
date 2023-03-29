package io.ruv.parcelservice.config;

import io.ruv.parcelservice.api.UserInfo;
import io.ruv.userservice.api.UserRole;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

public class UserInfoArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        return UserInfo.class.equals(parameter.getParameterType());
    }

    @Override
    public UserInfo resolveArgument(@NonNull MethodParameter parameter,
                                    ModelAndViewContainer mavContainer,
                                    NativeWebRequest webRequest,
                                    WebDataBinderFactory binderFactory) {

        var username = webRequest.getHeader("x-parcel-username"); //todo extract ParcelCustomHeaders to shared lib

        if (username == null || username.isEmpty()) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        var roleStrings = webRequest.getHeaderValues("x-parcel-user-roles"); //todo extract ParcelCustomHeaders to shared lib

        if (roleStrings == null || roleStrings.length == 0) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        var roles = UserRole.tryParse(roleStrings);

        if (roles.isEmpty()) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return new UserInfo(username, roles);
    }


}
