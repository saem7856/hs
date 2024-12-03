package com.hs.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.hs.dao.MemberDAO;
import com.hs.domain.MemberDTO;
import com.hs.domain.SessionInfo;
import com.hs.mvc.annotation.Controller;
import com.hs.mvc.annotation.RequestMapping;
import com.hs.mvc.annotation.RequestMethod;
import com.hs.mvc.annotation.ResponseBody;
import com.hs.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
	@RequestMapping(value = "/member/login", method = RequestMethod.GET)
	public ModelAndView loginForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 로그인 폼
		ModelAndView mav = new ModelAndView("member/login");
		return mav;
	}
	
	@RequestMapping(value = "/member/login", method = RequestMethod.POST)
	public ModelAndView loginSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 로그인 처리
		
		// 세션 객체
		HttpSession session = req.getSession();
		
		MemberDAO dao = new MemberDAO();
		
		// 클라이언트가 보낸 아이디/패스워드
		String userId = req.getParameter("userId");
		String userPwd = req.getParameter("userPwd");
		
		MemberDTO dto = dao.loginMember(userId, userPwd);
		if(dto != null) {
			// 로그인 성공한 경우
			// 세션에 아이디, 이름, 권한등을 저장
			
			// 세션 유지시간을 20분으로 설정 : 톰켓 기본은 30분
			session.setMaxInactiveInterval(20 * 60);
			
			// 세션에 저장할 정보
			SessionInfo info = new SessionInfo();
			info.setMemberIdx(dto.getMemberIdx());
			info.setUserId(dto.getUserId());
			info.setUserName(dto.getUserName());
			info.setUserLevel(dto.getUserLevel());
			
			// 세션에 member 라는 이름으로 로그인 정보를 저장
			session.setAttribute("member", info);
			
			// 로그인 전 주소가 존재하는 경우
			String preLoginURI = (String)session.getAttribute("preLoginURI");
			session.removeAttribute("preLoginURI");
			if(preLoginURI != null) {
				return new ModelAndView(preLoginURI);
			}
			
			// 메인 화면으로 리다이렉트
			return new ModelAndView("redirect:/");
		}
		
		// 아이디 또는 패스워드가 일치하지 않은 경우
		// 다시 로그인 폼으로 포워딩
		
		ModelAndView mav = new ModelAndView("member/login");
		mav.addObject("message", "아이디 또는 패스워드가 일치하지 않습니다.");
		return mav;
	}
	
	@RequestMapping(value = "/member/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 로그 아웃
		HttpSession session = req.getSession();
		
		// 세션에 저장된 정보 지우기
		session.removeAttribute("member");
		
		// 세션에 저장된 모든 내용을 지우고 세션을 초기화
		session.invalidate();

		// 메인화면으로 리다이렉트
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = "/member/noAuthorized", method = RequestMethod.GET)
	public ModelAndView noAuthorized(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 권한이 없는 경우
		ModelAndView mav = new ModelAndView("member/noAuthorized");
		return mav;
	}	
	
	@RequestMapping(value = "/member/member", method = RequestMethod.GET)
	public ModelAndView memberForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 회원가입 폼
		ModelAndView mav = new ModelAndView("member/member");
		
		mav.addObject("title", "회원 가입");
		mav.addObject("mode", "member");

		return mav;
	}

	@RequestMapping(value = "/member/member", method = RequestMethod.POST)
	public ModelAndView memberSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 회원가입 처리
		MemberDAO dao = new MemberDAO();
		HttpSession session = req.getSession();
		
		String message = "";
		try {
			MemberDTO dto = new MemberDTO();
			dto.setUserId(req.getParameter("userId"));
			dto.setUserPwd(req.getParameter("userPwd"));
			dto.setUserName(req.getParameter("userName"));
			
			dto.setBirth(req.getParameter("birth"));
			
			String email1 = req.getParameter("email1");
			String email2 = req.getParameter("email2");
			dto.setEmail(email1 + "@" + email2);

			String tel1 = req.getParameter("tel1");
			String tel2 = req.getParameter("tel2");
			String tel3 = req.getParameter("tel3");
			dto.setTel(tel1 + "-" + tel2 + "-" + tel3);

			dto.setZip(req.getParameter("zip"));
			dto.setAddr1(req.getParameter("addr1"));
			dto.setAddr2(req.getParameter("addr2"));

			dao.insertMember(dto);
			
			session.setAttribute("mode", "insert");
			session.setAttribute("userName", dto.getUserName());
			
			return new ModelAndView("redirect:/member/complete");
		} catch (SQLException e) {
			if (e.getErrorCode() == 1) {
				message = "아이디 중복으로 회원 가입이 실패 했습니다.";
			} else if (e.getErrorCode() == 1400) {
				message = "필수 사항을 입력하지 않았습니다.";
			} else if (e.getErrorCode() == 1840 || e.getErrorCode() == 1861) {
				message = "날짜 형식이 일치하지 않습니다.";
			} else {
				message = "회원 가입이 실패 했습니다.";
			// 기타 - 2291:참조키 위반, 12899:폭보다 문자열 입력 값이 큰경우
			}
		} catch (Exception e) {
			message = "회원 가입이 실패 했습니다.";
			e.printStackTrace();
		}

		ModelAndView mav = new ModelAndView("member/member");
		
		mav.addObject("title", "회원 가입");
		mav.addObject("mode", "member");
		mav.addObject("message", message);
		
		return mav;
	}

	@RequestMapping(value = "/member/pwd", method = RequestMethod.GET)
	public ModelAndView pwdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 패스워드 확인 폼
		ModelAndView mav = new ModelAndView("member/pwd");
		
		String mode = req.getParameter("mode");
		mav.addObject("mode", mode);

		return mav;
	}

	@RequestMapping(value = "/member/pwd", method = RequestMethod.POST)
	public ModelAndView pwdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 패스워드 확인
		MemberDAO dao = new MemberDAO();
		HttpSession session = req.getSession();

		try {
			SessionInfo info = (SessionInfo) session.getAttribute("member");

			// DB에서 해당 회원 정보 가져오기
			MemberDTO dto = dao.findById(info.getUserId());
			if (dto == null) {
				session.invalidate();
				return new ModelAndView("redirect:/");
			}

			String userPwd = req.getParameter("userPwd");
			String mode = req.getParameter("mode");
			if (! dto.getUserPwd().equals(userPwd)) {
				ModelAndView mav = new ModelAndView("member/pwd");
				
				mav.addObject("mode", mode);
				mav.addObject("message", "패스워드가 일치하지 않습니다.");
				
				return mav;
			}

			if (mode.equals("delete")) {
				// 회원탈퇴
				dao.deleteMember(info.getUserId());

				session.removeAttribute("member");
				session.invalidate();

				return new ModelAndView("redirect:/");
			}

			// 회원정보수정 - 회원수정폼으로 이동
			ModelAndView mav = new ModelAndView("member/member");
			
			mav.addObject("title", "회원 정보 수정");
			mav.addObject("dto", dto);
			mav.addObject("mode", "update");
			
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/member/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 회원정보 수정 완료
		MemberDAO dao = new MemberDAO();
		HttpSession session = req.getSession();
		
		try {
			MemberDTO dto = new MemberDTO();

			dto.setUserId(req.getParameter("userId"));
			dto.setUserPwd(req.getParameter("userPwd"));
			dto.setUserName(req.getParameter("userName"));
			
			dto.setBirth(req.getParameter("birth"));
			
			String email1 = req.getParameter("email1");
			String email2 = req.getParameter("email2");
			dto.setEmail(email1 + "@" + email2);

			String tel1 = req.getParameter("tel1");
			String tel2 = req.getParameter("tel2");
			String tel3 = req.getParameter("tel3");
			dto.setTel(tel1 + "-" + tel2 + "-" + tel3);

			dto.setZip(req.getParameter("zip"));
			dto.setAddr1(req.getParameter("addr1"));
			dto.setAddr2(req.getParameter("addr2"));

			dao.updateMember(dto);
			
			session.setAttribute("mode", "update");
			session.setAttribute("userName", dto.getUserName());
			
			return new ModelAndView("redirect:/member/complete");			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = "/member/complete", method = RequestMethod.GET)
	public ModelAndView complete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String mode = (String)session.getAttribute("mode");
		String userName = (String)session.getAttribute("userName");
		
		session.removeAttribute("mode");
		session.removeAttribute("userName");
		
		if(mode == null) {
			return new ModelAndView("redirect:/");
		}
		
		String title;
		String message = "<b>" + userName + "</b>님 ";
		if(mode.equals("insert")) {
			title = "회원 가입";
			message += "회원가입이 완료 되었습니다.<br>로그인 하시면 정보를 이용하실수 있습니다.";
		} else {
			title = "회원 정보 수정";
			message += "회원정보가 수정 되었습니다.<br>메인 화면으로 이동하시기 바랍니다.";
		}

		ModelAndView mav = new ModelAndView("member/complete");

		mav.addObject("title", title);
		mav.addObject("message", message);
		
		return mav;
	}
	
	@ResponseBody // AJAX : JSON 으로 반환
	@RequestMapping(value = "/member/userIdCheck", method = RequestMethod.POST)
	public Map<String, Object> userIdCheck(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 아이디 중복 검사
		MemberDAO dao = new MemberDAO();
		
		String userId = req.getParameter("userId");
		MemberDTO dto = dao.findById(userId);
		
		String passed = "false";
		if(dto == null) {
			passed = "true";
		}
		
		Map<String, Object> model = new HashMap<>();
		
		model.put("passed", passed);
		
		return model;
	}
	
}
