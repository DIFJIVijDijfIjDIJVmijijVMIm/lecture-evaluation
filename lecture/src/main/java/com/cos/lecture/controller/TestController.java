//package com.cos.lecture.controller;
//
//import java.io.PrintWriter;
//import java.util.List;
//import java.util.Properties;
//
//import javax.mail.Address;
//import javax.mail.Authenticator;
//import javax.mail.Message;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.cos.lecture.model.Evaluation;
//import com.cos.lecture.model.User;
//import com.cos.lecture.repository.EvaluationRepository;
//import com.cos.lecture.repository.LikeyRepository;
//import com.cos.lecture.repository.UserRepository;
//import com.cos.lecture.util.Gmail;
//import com.cos.lecture.util.SHA256;
//import com.cos.lecture.util.Script;
//
//@Controller
//public class TestController {
//	
//	private static final Logger log = LoggerFactory.getLogger(TestController.class);
//	
//	@Autowired
//	UserRepository mUserRepo;
//	
//	@Autowired
//	LikeyRepository mLikeyRepo;
//	
//	@Autowired
//	EvaluationRepository mEvalRepo;
//	
//	@Autowired
//	private BCryptPasswordEncoder encoder;
//	
//	@GetMapping("/home")
//	public String TestHome(Model model, HttpSession session, @RequestParam int page) {
//
//		System.out.println("page : " + page);
//		session.setAttribute("searching", "NoSearching");
//		
//		if(page == 1 || page < 1) {
//			
//			page = 1;
//			session.setAttribute("page", page);
//			
//			page = page-1;
//			
//			List<Evaluation> allEvalSize = mEvalRepo.findAll();
//			int evalSize = allEvalSize.size()/4;
//			
//			if(allEvalSize.size()%4 != 0) {
//				evalSize = evalSize+1;
//			}
//			
//			System.out.println("allEvalSize : " + allEvalSize.size());
//			System.out.println("evalSize : " + evalSize);
//
//			List<Evaluation> eval = mEvalRepo.findAllByOrderByIdDesc(page);
//
//			model.addAttribute("eval", eval);
//			
//			session.setAttribute("evalSize", evalSize);
//			
//			return "home";
//			
//		}else {
//			session.setAttribute("page", page);
//			page = (page-1)*4;
//			
//			List<Evaluation> allEvalSize = mEvalRepo.findAll();
//			int evalSize = allEvalSize.size()/4;
//			
//			if(allEvalSize.size()%4 != 0) {
//				evalSize = evalSize+1;
//			}
//			
//			List<Evaluation> eval = mEvalRepo.findAllByOrderByIdDesc(page);
//
//			model.addAttribute("eval", eval);
//			
//			session.setAttribute("evalSize", evalSize);
//			
//			return "home";
//		}
//	}
//	
//	@GetMapping("/userLogin")
//	public String UserLogin() {
//		return "userLogin";
//	}
//	
//	@GetMapping("/userJoin")
//	public String UserJoin() {
//		return "userJoin";
//	}
//	
//	@GetMapping("/emailSend")
//	public String EmailSend() {
//		return "emailSend";
//	}
//	
//	@PostMapping("/userJoinProc")
//	public String UserJoinProc(User user, HttpServletResponse response) throws Exception {
//		System.out.println("JoinProc : UserName : "+user.getUserName());
//		System.out.println("JoinProc : UserPassword : "+user.getUserPassword());
//		System.out.println("JoinProc : UserEmail : "+user.getUserEmail());
//		
//		if(mUserRepo.findByUserName(user.getUserName()) != null) {
//			System.out.println("아이디 중복");
//			response.setContentType("text/html; charset=UTF-8");
//            PrintWriter out = response.getWriter();
//            out.println(Script.Back("아이디 중복"));
//            out.flush();
//            
//            
//            return "userJoin";
//		}
//		
//		user.setUserEmailHash(SHA256.getSHA256(user.getUserEmail()));
//			
//		String rawPassword = user.getUserPassword();
//		String encPassword = encoder.encode(rawPassword);
//		user.setUserPassword(encPassword);
//		log.info("rawPassword : "+rawPassword);
//		log.info("encPassword : "+encPassword);
//		
//		mUserRepo.save(user);					
//		
//		return "redirect:/userLogin"; 
//	}
//	
//	@GetMapping("/userLoginProc")
//	public String UserLoginProc(User user, HttpServletResponse response, HttpSession session) throws Exception {
//		
//		System.out.println("userLoginProc : userName :"+user.getUserName());
//		System.out.println("userLoginProc : userPassword : "+user.getUserPassword());
//		
//		if(mUserRepo.findByUserName(user.getUserName()) == null) {
//			System.out.println("아이디 틀림");
//			response.setContentType("text/html; charset=UTF-8");
//            PrintWriter out = response.getWriter();
//            out.println(Script.Back("아이디가 틀립니다."));
//            out.flush();
//            
//            return "userLogin";
//		}else {
//			User userChecking = mUserRepo.findByUserName(user.getUserName());
//			
//			if(encoder.matches(user.getUserPassword(), userChecking.getUserPassword()) == false) {
//				System.out.println("비번 틀림");
//				response.setContentType("text/html; charset=UTF-8");
//	            PrintWriter out = response.getWriter();
//	            out.println(Script.Back("비밀번호가 틀립니다."));
//	            out.flush();
//	            
//	            return "userLogin";
//			}
//		}
//		
//		User user1 = mUserRepo.findByUserName(user.getUserName());
//		
//		if(user1.getUserEmailChecked() == false) {
//			
//			String host = "http://localhost:8080/";
//			String from = "myseongin@gmail.com";
//			String to = user1.getUserEmail();
//			String subject = "강의평가를 위한 이메일 인증 메일입니다.";
//			String content = "다음 링크에 접속하여 이메일 인증을 진행하세요." +
//					"</br><form action='"+ host +"emailCheck/"+ user1.getUserName() +"/" + SHA256.getSHA256(to) +"' method='post'>"
//							+ "<button type='submit' class='btn btn-primary'>이메일 인증하기</button></form>";
//
//			Properties p = new Properties();
//			p.put("mail.smtp.user", from);
//			p.put("mail.smtp.host", "smtp.googlemail.com");
//			p.put("mail.smtp.port", "465");
//			p.put("mail.smtp.starttls.enable", "true");
//			p.put("mail.smtp.auth", "true");
//			p.put("mail.smtp.debug", "true");
//			p.put("mail.smtp.socketFactory.port", "465");
//			p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//			p.put("mail.smtp.socketFactory.fallback", "false");
//			
//			try {
//				Authenticator auth = new Gmail();
//				Session ses = Session.getInstance(p, auth);
//				ses.setDebug(true);
//				MimeMessage msg = new MimeMessage(ses);
//				msg.setSubject(subject);
//				Address fromAddr = new InternetAddress(from);
//				msg.setFrom(fromAddr);
//				Address toAddr = new InternetAddress(to);
//				msg.addRecipient(Message.RecipientType.TO, toAddr);
//				msg.setContent(content, "text/html;charset=UTF8");
//				Transport.send(msg);
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//				response.setContentType("text/html; charset=UTF-8");
//	            PrintWriter out = response.getWriter();
//	            out.println(Script.Back("오류가 발생하였습니다."));
//	            out.flush();
//			}
//			
//			System.out.println("이메일 인증 요청");
//			
//			return "emailSend";
//		}else {
//			
//			session.setAttribute("user", user1);
//			
//			return "redirect:/home?page=1";
//		}
//		
//	}
//	
//	@PostMapping("/emailCheck/{userName}/{emailHash}")
//	public @ResponseBody String EmailCheck(@PathVariable String userName, @PathVariable String emailHash
//			, HttpServletResponse response, HttpSession session) throws Exception{
//		
//		User user = mUserRepo.findByUserName(userName);
//		String userEmail = user.getUserEmail();
//		
//		boolean isRight = (SHA256.getSHA256(userEmail).equals(emailHash)) ? true : false;
//		
//		if(isRight == true) {
//			
//			user.setUserEmailChecked(true);
//			mUserRepo.save(user);
//			
//			System.out.println("이메일 인증 성공");
//			
//			response.setContentType("text/html; charset=UTF-8");
//            PrintWriter out = response.getWriter();
//            out.println(Script.href("이메일 인증에 성공하였습니다.","http://localhost:8080/userLogin"));
//            out.flush();
//            
//            return "redirect:/userLogin";
//		}else {
//			
//			System.out.println("이메일 인증 오류");
//			response.setContentType("text/html; charset=UTF-8");
//            PrintWriter out = response.getWriter();
//            out.println(Script.Back("유효하지 않은 코드입니다."));
//            out.flush();
//			
//			return "userLogin";
//		}
//		
//		
//	}
//	
//	@GetMapping("/userLogout")
//	public String UserLogout(HttpSession session) {
//		
//		session.invalidate();
//		
//		return "redirect:/home?page=1";
//	}
//}
