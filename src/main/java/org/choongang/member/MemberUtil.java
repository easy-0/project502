package org.choongang.member;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.member.entities.Member;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberUtil {

    private final HttpSession session;
    
    /* 로그인 여부 확인 */
    public boolean isLogin() {
        return getMember() != null;
    }
    
    /* 세션에 member가 있으면 member 반환 */
    public Member getMember() {
        Member member = (Member) session.getAttribute("member");
        
        return  member;
    }
    
    /* 로그인시 세션 비우기 */
    public static void clearLoginData(HttpSession session) {
        session.removeAttribute("username");
        session.removeAttribute("NotBlank_username");
        session.removeAttribute("NotBlank_password");
        session.removeAttribute("Global_error");
    }

}
