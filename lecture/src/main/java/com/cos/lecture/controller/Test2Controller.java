//package com.cos.lecture.controller;
//
//import java.util.List;
//import java.util.Optional;
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
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.cos.lecture.model.Evaluation;
//import com.cos.lecture.model.Likey;
//import com.cos.lecture.model.User;
//import com.cos.lecture.repository.EvaluationRepository;
//import com.cos.lecture.repository.LikeyRepository;
//import com.cos.lecture.repository.UserRepository;
//import com.cos.lecture.util.Gmail;
//import com.cos.lecture.util.Script;
//
//@Controller
//public class Test2Controller {
//	
//	@Autowired
//	EvaluationRepository mEvalRepo;
//	
//	@Autowired
//	LikeyRepository mLikeyRepo;
//	
//	@Autowired
//	UserRepository mUserRepo;
//	
//	@GetMapping("/eval")
//	public String TestEval(Model model) {
//		
//		List<Evaluation> eval = mEvalRepo.findAll();
//		
//		model.addAttribute("eval", eval);
//		
//		return "test/test";
//	}
//	
//	//공백 지우기
//	public static String delSpace(String str)
//    {
//        String result = "";
//        
//        for(int i = 0 ; i < str.length(); i ++)
//        {
//            if(str.charAt(i) != ' ')
//                result += str.charAt(i);
//        }
//        
//        return result;
//    }
//	
//	@PostMapping("/evalRegister/{userId}")
//	public @ResponseBody String EvalRegister(Evaluation eval, @PathVariable int userId) throws Exception {
//		
//		if(delSpace(eval.getEvaluationContent()).equals("") == true || delSpace(eval.getEvaluationTitle()).equals("") == true  || 
//				delSpace(eval.getLectureName()).equals("") == true || delSpace(eval.getProfessorName()).equals("") == true ) {
//			
//			return Script.Back("입력이 안 된 항목이 있습니다.");
//		}
//		
//		Optional<User> opUser = mUserRepo.findById(userId);
//		User user = opUser.get();
//		eval.setUser(user);
//		mEvalRepo.save(eval);
//				
//		return Script.href("등록이 완료되었습니다.", "http://localhost:8080/");
//	}
//	
//	//신고하기
//	@PostMapping("/reportSend/{userEmail}")
//	public @ResponseBody String ReportSend(String reportTitle, String reportContent,
//			@PathVariable String userEmail, HttpServletResponse response) throws Exception {
//		
//		if(delSpace(reportTitle).equals("") == true || delSpace(reportContent).equals("") == true) {
//			return Script.href("입력이 안 된 항목이 있습니다.", "http://localhost:8080/");
//		}
//		
////		String host = "http://localhost:8080/";
//		String from = "myseongin@gmail.com";
//		String to = "smtlsska@naver.com";
//		String subject = "강의평가 사이트에서 접수된 신고 메일입니다.";
//		String content = "신고자 메일 주소 : "+userEmail+
//							"<br>제목 : "+ reportTitle+
//							"<br>내용 : "+ reportContent;
//
//		Properties p = new Properties();
//		p.put("mail.smtp.user", from);
//		p.put("mail.smtp.host", "smtp.googlemail.com");
//		p.put("mail.smtp.port", "465");
//		p.put("mail.smtp.starttls.enable", "true");
//		p.put("mail.smtp.auth", "true");
//		p.put("mail.smtp.debug", "true");
//		p.put("mail.smtp.socketFactory.port", "465");
//		p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//		p.put("mail.smtp.socketFactory.fallback", "false");
//		
//		try {
//			Authenticator auth = new Gmail();
//			Session ses = Session.getInstance(p, auth);
//			ses.setDebug(true);
//			MimeMessage msg = new MimeMessage(ses);
//			msg.setSubject(subject);
//			Address fromAddr = new InternetAddress(from);
//			msg.setFrom(fromAddr);
//			Address toAddr = new InternetAddress(to);
//			msg.addRecipient(Message.RecipientType.TO, toAddr);
//			msg.setContent(content, "text/html;charset=UTF8");
//			Transport.send(msg);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("신고하기 요청 오류");
//			return Script.Back("신고하기 중 오류가 발생하였습니다.");
//		}
//		
//		System.out.println("신고하기 요청 완료");
//		
//		return Script.href("신고하기가 완료되었습니다.", "http://localhost:8080/");
//	}
//	
//	//삭제하기
//	@GetMapping("/evalDelete/{evalId}/{userId}")
//	public @ResponseBody String EvalDelete(@PathVariable int evalId, @PathVariable int userId, HttpSession session) {
//		
//		Optional<Evaluation> evaluation = mEvalRepo.findById(evalId);
//		Evaluation eval = evaluation.get();
//				
//		if(eval.getUser().getId() != userId) {
//			
//			return Script.Back("회원님이 작성하신 글이 아닙니다."); 
//		}
//		
//		mEvalRepo.delete(eval);
//		
//		return Script.href("게시글을 삭제하였습니다.", "http://localhost:8080/");
//	}
//	
//	//추천하기
//	@GetMapping("/evalLike/{evalId}/{userId}")
//	public @ResponseBody String EvalLike(Likey likey, 
//			@PathVariable int evalId, @PathVariable int userId, 
//			HttpSession session) {
//		
//		List<Likey> likeyCheck = mLikeyRepo.findByUserId(userId);
//		
//		for(int i=0; i<likeyCheck.size(); i++) {
//			if(likeyCheck.get(i).getEvaluationId() == evalId) {
//				
//				likey.setUserId(userId);
//				likey.setEvaluationId(evalId);
//
//				mLikeyRepo.delete(likey);
//				
//				Optional<Evaluation> opEval = mEvalRepo.findById(evalId);
//				Evaluation eval = opEval.get();
//				
//				int likeCount = mLikeyRepo.countByEvaluationId(evalId);
//				System.out.println("likeCount : " + likeCount);
//				
//				eval.setLikeCount(likeCount);
//				
//				mEvalRepo.save(eval);
//				
//				return Script.Back("이미 추천된 게시글입니다. 추천을 취소합니다.");
//			}
//			
//		}
//		
//	
//		likey.setUserId(userId);
//		likey.setEvaluationId(evalId);
//
//		mLikeyRepo.save(likey);
//		
//		Optional<Evaluation> opEval = mEvalRepo.findById(evalId);
//		Evaluation eval = opEval.get();
//		
//		int likeCount = mLikeyRepo.countByEvaluationId(evalId);
//		System.out.println("likeCount : " + likeCount);
//		
//		eval.setLikeCount(likeCount);
//		
//		mEvalRepo.save(eval);
//	
//		return Script.Back("게시글 추천이 완료되었습니다.");
//	}
//
//	//검색하기
//	//미래의 나야 getmaaping 한다음에 @requestParam 이걸로 데이터를 받아와서 넘겨라
//	
//	@GetMapping("/evalSearch")
//	public String EvalSearch(Model model, HttpSession session, @RequestParam int page,
//			@RequestParam String lectureDivide, @RequestParam String searchType, @RequestParam String searchText) {
//		
//		session.setAttribute("lectureDivide", lectureDivide);
//		session.setAttribute("searchType", searchType);
//		session.setAttribute("searchText", searchText);
//		session.setAttribute("searching", "searching");
//		
//		if(lectureDivide.equals("전체") == true) {
//			if(searchType.equals("최신순") == true) {
//				
//				System.out.println("전체 : 최신순 : ");
//				List<Evaluation> allEval= mEvalRepo.findAllBySearchOrderById(searchText);
//				
//				if(page == 1 || page < 1) {
//					
//					page = 1;
//					session.setAttribute("page", page);
//					
//					page = page-1;
//					
//					int evalSize = allEval.size()/4;
//					
//					if(allEval.size()%4 != 0) {
//						evalSize = evalSize+1;
//					}
//					
//					List<Evaluation> eval = mEvalRepo.findAllBySearchOrderByIdLimit(searchText, page);
//
//					model.addAttribute("eval", eval);
//					
//					session.setAttribute("evalSize", evalSize);
//					
//					return "home";
//					
//				}else {
//					session.setAttribute("page", page);
//					page = (page-1)*4;
//					
//					int evalSize = allEval.size()/4;
//					
//					if(allEval.size()%4 != 0) {
//						evalSize = evalSize+1;
//					}
//					
//					List<Evaluation> eval = mEvalRepo.findAllBySearchOrderByIdLimit(searchText, page);
//
//					model.addAttribute("eval", eval);
//					
//					session.setAttribute("evalSize", evalSize);
//					
//					return "home";
//				}
//			}else if(searchType.equals("추천순") == true) {
//				
//				System.out.println("전체 : 추천순 : ");
//				List<Evaluation> allEval= mEvalRepo.findAllBySearchOrderByLikeCount(searchText);
//				
//				if(page == 1 || page < 1) {
//					
//					page = 1;
//					session.setAttribute("page", page);
//					
//					page = page-1;
//					
//					int evalSize = allEval.size()/4;
//					
//					if(allEval.size()%4 != 0) {
//						evalSize = evalSize+1;
//					}
//					
//					List<Evaluation> eval = mEvalRepo.findAllBySearchOrderByLikeCountLimit(searchText, page);
//
//					model.addAttribute("eval", eval);
//					
//					session.setAttribute("evalSize", evalSize);
//					
//					return "home";
//					
//				}else {
//					session.setAttribute("page", page);
//					page = (page-1)*4;
//					
//					int evalSize = allEval.size()/4;
//					
//					if(allEval.size()%4 != 0) {
//						evalSize = evalSize+1;
//					}
//					
//					List<Evaluation> eval = mEvalRepo.findAllBySearchOrderByLikeCountLimit(searchText, page);
//
//					model.addAttribute("eval", eval);
//					
//					session.setAttribute("evalSize", evalSize);
//					
//					return "home";
//				}
//				
//			}
//		}else {
//			if(searchType.equals("최신순") == true) {
//				
//				List<Evaluation> allEval= mEvalRepo.findBySearchOrderById(lectureDivide, searchText);
//				if(page == 1 || page < 1) {
//					
//					page = 1;
//					session.setAttribute("page", page);
//					
//					page = page-1;
//					
//					int evalSize = allEval.size()/4;
//					
//					if(allEval.size()%4 != 0) {
//						evalSize = evalSize+1;
//					}
//					
//					List<Evaluation> eval = mEvalRepo.findBySearchOrderByIdLimit(lectureDivide, searchText, page);
//
//					model.addAttribute("eval", eval);
//					
//					session.setAttribute("evalSize", evalSize);
//					
//					return "home";
//					
//				}else {
//					session.setAttribute("page", page);
//					page = (page-1)*4;
//					
//					int evalSize = allEval.size()/4;
//					
//					if(allEval.size()%4 != 0) {
//						evalSize = evalSize+1;
//					}
//					
//					List<Evaluation> eval = mEvalRepo.findBySearchOrderByIdLimit(lectureDivide, searchText, page);
//
//					model.addAttribute("eval", eval);
//					
//					session.setAttribute("evalSize", evalSize);
//					
//					return "home";
//				}
//			}else if(searchType.equals("추천순") == true) {
//				
//				List<Evaluation> allEval = mEvalRepo.findBySearchOrderByLikeCount(lectureDivide, searchText);
//				if(page == 1 || page < 1) {
//					
//					page = 1;
//					session.setAttribute("page", page);
//					
//					page = page-1;
//					
//					int evalSize = allEval.size()/4;
//					
//					if(allEval.size()%4 != 0) {
//						evalSize = evalSize+1;
//					}
//					
//					List<Evaluation> eval = mEvalRepo.findBySearchOrderByLikeCountLimit(lectureDivide, searchText, page);
//
//					model.addAttribute("eval", eval);
//					
//					session.setAttribute("evalSize", evalSize);
//					
//					return "home";
//					
//				}else {
//					session.setAttribute("page", page);
//					page = (page-1)*4;
//					
//					int evalSize = allEval.size()/4;
//					
//					if(allEval.size()%4 != 0) {
//						evalSize = evalSize+1;
//					}
//					
//					List<Evaluation> eval = mEvalRepo.findBySearchOrderByLikeCountLimit(lectureDivide, searchText, page);
//
//					model.addAttribute("eval", eval);
//					
//					session.setAttribute("evalSize", evalSize);
//					
//					return "home";
//				}
//			}
//		}
//		
//		return "/home";
//	}
//	
//	@GetMapping("/evalSearchAll")
//	public String EvalSearchAll(Model model, @RequestParam String searchText, @RequestParam int page
//			,HttpSession session) {
//		
//		List<Evaluation> allEval= mEvalRepo.findAllBySearch(searchText);
//		if(page == 1 || page < 1) {
//			
//			page = 1;
//			session.setAttribute("page", page);
//			
//			page = page-1;
//			
//			int evalSize = allEval.size()/4;
//			
//			if(allEval.size()%4 != 0) {
//				evalSize = evalSize+1;
//			}
//			
//			List<Evaluation> eval = mEvalRepo.findAllBySearchOrderByIdLimit(searchText, page);
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
//			int evalSize = allEval.size()/4;
//			
//			if(allEval.size()%4 != 0) {
//				evalSize = evalSize+1;
//			}
//			
//			List<Evaluation> eval = mEvalRepo.findAllBySearchOrderByIdLimit(searchText, page);
//
//			model.addAttribute("eval", eval);
//			
//			session.setAttribute("evalSize", evalSize);
//			
//			return "home";
//		}
//	}
//}
