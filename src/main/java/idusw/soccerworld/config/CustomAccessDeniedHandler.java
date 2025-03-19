package idusw.soccerworld.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        // 리디렉션과 함께 flash attribute 추가
        RedirectAttributes redirectAttributes = (RedirectAttributes) request.getAttribute(RedirectAttributes.class.getName());
        redirectAttributes.addFlashAttribute("message", "로그인이 필요한 기능입니다");

        // 리디렉션 수행
        response.sendRedirect("/main/index");
    }
}