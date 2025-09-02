package com.planova.server.global.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.planova.server.global.annotation.CurrentUser;
import com.planova.server.global.jwt.JwtPayload;

@Component
public class CurrentUserResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    boolean hasCurrentUserAnnotation = parameter.hasParameterAnnotation(CurrentUser.class);
    boolean isSupportedType = JwtPayload.class
        .isAssignableFrom(parameter.getParameterType());
    return hasCurrentUserAnnotation && isSupportedType;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication.getPrincipal() == null) {
      return null;
    }

    Object principal = authentication.getPrincipal();

    if (principal instanceof JwtPayload) {
      return principal;
    }
    return null;
  }
}
