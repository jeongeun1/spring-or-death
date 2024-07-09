package com.sh.choichanguk.member.controller;

import com.sh.choichanguk.member.model.dto.FileDto;
import com.sh.choichanguk.member.model.dto.MemberRegistDto;
import com.sh.choichanguk.member.model.service.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class controller {

    private final Service service;
    private final RequestContextFilter requestContextFilter;

    @GetMapping("/regist")
    public String  regist(){
        return "member/regist";
    }


    @PostMapping(path = "/checkEmail", produces = "text/plain; charset=UTF-8")
    @ResponseBody // 이 핸들러의 반환객체를 직접 http 응답 메세지 본문(body)에 작성하라.
    public String first(@RequestParam String email) {
        log.debug("확인하려 하는 email = {}", email);

        String  memberEmail=service.findByEmail(email);

        if(memberEmail!=null)
            return "1"; // 중복된 이메일이 있다
        else return "0"; // 중복된 이메일이 없다 = 사용가능한 이메일이다
    }

    @PostMapping("/regist")
<<<<<<< HEAD
    public String regist(@ModelAttribute MemberRegistDto memberRegistInfo, RedirectAttributes redirectAttributes,
                         @RequestParam("upFile") List<MultipartFile> upFiles) throws IOException {
=======
    // @ModelAttribute 어노테이션을 쓰면 memberRegistInfo객체와, 제출되는 form에 name태그와 같은 parameter를 찾아 setter를 해준다
    public String regist(@ModelAttribute MemberRegistDto memberRegistInfo, RedirectAttributes redirectAttributes){
>>>>>>> c3ea328a5740915f5430f8e3f8da1f810061f963
        System.out.println("멤버 등록까지 왔습니다");
        System.out.println("memberRegistInfo = " + memberRegistInfo);
        MemberRegistDto memberRegistDto = memberRegistInfo;
        int result=service.insertMember(memberRegistDto);

        List<FileDto> list = new ArrayList<>();
        for (MultipartFile file : upFiles) {
            if (!file.isEmpty()) {
                FileDto fileDto = service.upload(file);
                list.add(fileDto);
            }
        }

        System.out.println("결과 : "+result);
        return "redirect:/member/regist";
    }
}
