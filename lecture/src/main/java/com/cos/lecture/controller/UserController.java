package com.cos.lecture.controller;

import java.io.PrintWriter;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.lecture.model.User;
import com.cos.lecture.repository.EvaluationRepository;
import com.cos.lecture.repository.LikeyRepository;
import com.cos.lecture.repository.UserRepository;
import com.cos.lecture.util.Gmail;
import com.cos.lecture.util.SHA256;
import com.cos.lecture.util.Script;

@Controller
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserRepository mUserRepo;

	@Autowired
	LikeyRepository mLikeyRepo;
	
	@Autowired
	EvaluationRepository mEvalRepo;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	//공백 지우기
	public static String delSpace(String str)
    {
        String result = "";
        
        for(int i = 0 ; i < str.length(); i ++)
        {
            if(str.charAt(i) != ' ')
                result += str.charAt(i);
        }
        
        return result;
    }
	
	//공백 체크하기
	public static Boolean checkSpace(String str)
    {
        
        for(int i = 0 ; i < str.length(); i ++)
        {
            if(str.charAt(i) == ' ')
                return false;
        }
        
        return true;
    }
	
	//유저 로그인 페이지 이동
	@GetMapping("/userLogin")
	public String UserLogin() {
		return "userLogin";
	}
	
	//유저 회원가입 페이지 이동
	@GetMapping("/userJoin")
	public String UserJoin() {
		return "userJoin";
	}
	
	//유저 이메일 인증 요청 페이지 이동
	@GetMapping("/emailSend")
	public String EmailSend() {
		return "emailSend";
	}
	
	//유저 회원가입 액션
	
	@PostMapping("/userJoinProc")
	public @ResponseBody String UserJoinProc(User user, HttpServletResponse response) throws Exception {
		
		if(checkSpace(user.getUserName()) == false || checkSpace(user.getUserEmail()) == false
				|| checkSpace(user.getUserPassword()) == false) {
			System.out.println("회원가입 중 공백 발생");
			
			return Script.Back("입력 항목 중에 공백이 있으면 안 됩니다.");
		}
		
		if(mUserRepo.findByUserName(user.getUserName()) != null) {
			System.out.println("아이디 중복");
            
            return Script.Back("아이디 중복");
		}
		
		user.setUserEmailHash(SHA256.getSHA256(user.getUserEmail()));
			
		String rawPassword = user.getUserPassword();
		String encPassword = encoder.encode(rawPassword);
		user.setUserPassword(encPassword);
		log.info("rawPassword : "+rawPassword);
		log.info("encPassword : "+encPassword);
		
		mUserRepo.save(user);
		
		return Script.href("가입이 완료되었습니다.", "/userLogin"); 
	}
	
	//유저 로그인 액션
	
	@GetMapping("/userLoginProc")
	public String UserLoginProc(User user, HttpServletResponse response, 
			HttpSession session, CsrfToken _csrf) throws Exception {

//		session.getAttribute(_csrf.getParameterName());
		System.out.println("이게 받아지나? : "+_csrf.getToken());
		
		if(mUserRepo.findByUserName(user.getUserName()) == null) {
			System.out.println("아이디 틀림");
			response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println(Script.Back("아이디가 틀립니다."));
            out.flush();
            
            return "userLogin";
		}else {
			User userChecking = mUserRepo.findByUserName(user.getUserName());
			
			if(encoder.matches(user.getUserPassword(), userChecking.getUserPassword()) == false) {
				System.out.println("비번 틀림");
				response.setContentType("text/html; charset=UTF-8");
	            PrintWriter out = response.getWriter();
	            out.println(Script.Back("비밀번호가 틀립니다."));
	            out.flush();
	            
	            return "userLogin";
			}
		}
		
		User user1 = mUserRepo.findByUserName(user.getUserName());
		
		if(user1.getUserEmailChecked() == false) {
			
			String host = "http://localhost:8080/";
			String from = "myseongin@gmail.com";
			String to = user1.getUserEmail();
			String subject = "강의평가를 위한 이메일 인증 메일입니다.";
			String content = "다음 링크에 접속하여 이메일 인증을 진행하세요." +
					"</br><form action='"+ host +"emailCheck/"+ user1.getUserName() +"/" + SHA256.getSHA256(to) +"' method='post'>"
							+ "<input type='hidden' name='"+_csrf.getParameterName()+"' value='"+_csrf.getToken()+"'/>"
							+ "<button type='submit' class='btn btn-primary'>이메일 인증하기</button></form>";

			Properties p = new Properties();
			p.put("mail.smtp.user", from);
			p.put("mail.smtp.host", "smtp.googlemail.com");
			p.put("mail.smtp.port", "465");
			p.put("mail.smtp.starttls.enable", "true");
			p.put("mail.smtp.auth", "true");
			p.put("mail.smtp.debug", "true");
			p.put("mail.smtp.socketFactory.port", "465");
			p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			p.put("mail.smtp.socketFactory.fallback", "false");
			
			try {
				Authenticator auth = new Gmail();
				Session ses = Session.getInstance(p, auth);
				ses.setDebug(true);
				MimeMessage msg = new MimeMessage(ses);
				msg.setSubject(subject);
				Address fromAddr = new InternetAddress(from);
				msg.setFrom(fromAddr);
				Address toAddr = new InternetAddress(to);
				msg.addRecipient(Message.RecipientType.TO, toAddr);
				msg.setContent(content, "text/html;charset=UTF8");
				Transport.send(msg);
				
			} catch (Exception e) {
				e.printStackTrace();
				response.setContentType("text/html; charset=UTF-8");
	            PrintWriter out = response.getWriter();
	            out.println(Script.Back("오류가 발생하였습니다."));
	            out.flush();
			}
			
			System.out.println("이메일 인증 요청");
			
			return "emailSend";
		}else {
			
			session.setAttribute("user", user1);
			
			return "redirect:/home?page=1";
		}
	}
	
	@PostMapping("/emailCheck/{userName}/{emailHash}")
	public @ResponseBody String EmailCheck(@PathVariable String userName, @PathVariable String emailHash
			, HttpServletResponse response, HttpSession session) throws Exception{
		
		User user = mUserRepo.findByUserName(userName);
		String userEmail = user.getUserEmail();
		
		boolean isRight = (SHA256.getSHA256(userEmail).equals(emailHash)) ? true : false;
		
		if(isRight == true) {
			
			user.setUserEmailChecked(true);
			
			mUserRepo.save(user);
			
			System.out.println("이메일 인증 성공");
            
            return Script.href("이메일 인증에 성공하였습니다.","http://localhost:8080/userLogin");
		}else {
			
			System.out.println("이메일 인증 오류");
			response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println(Script.Back("유효하지 않은 코드입니다."));
            out.flush();
			
			return "userLogin";
		}
		
		
	}
	
	
	//유저 로그아웃
	@GetMapping("/userLogout")
	public String UserLogout(HttpSession session) {
		
		session.invalidate();
		
		return "redirect:/home?page=1";
	}
}
