package com.hs.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import com.hs.dao.BoardDAO;
import com.hs.domain.BoardDTO;
import com.hs.domain.SessionInfo;
import com.hs.mvc.annotation.Controller;
import com.hs.mvc.annotation.RequestMapping;
import com.hs.mvc.annotation.RequestMethod;
import com.hs.mvc.view.ModelAndView;
import com.hs.util.MyUtil;
import com.hs.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {
	@RequestMapping("/bbs/list") // GET, POST 모두 처리
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시글 리스트 : 파라미터 - [page], [schType, kwd] 
		
		ModelAndView mav = new ModelAndView("bbs/list");
		
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtilBootstrap();
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			// 검색
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if(schType == null) {
				schType = "all";
				kwd = "";
			}
			
			// GET 방식이면 디코딩
			if(req.getMethod().equalsIgnoreCase("GET")) {
				kwd = URLDecoder.decode(kwd, "utf-8");
			}
			
			// 데이터 개수
			int dataCount;
			if(kwd.length() == 0) {
				dataCount = dao.dataCount(); // 검색이 아닌경우
			} else {
				dataCount = dao.dataCount(schType, kwd); // 검색인 경우
			}
			
			// 전체 페이지 수
			int size = 10;
			int total_page = util.pageCount(dataCount, size);
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			// 게시물 가져오기
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<BoardDTO> list = null;
			if(kwd.length() == 0) {
				list = dao.listBoard(offset, size);
			} else {
				list = dao.listBoard(offset, size, schType, kwd);
			}
			
			// 쿼리
			String query = "";
			if(kwd.length() != 0) {
				query = "schType=" + schType + "&kwd=" +
						URLEncoder.encode(kwd, "utf-8");
			}
			
			// 페이징 처리
			String cp = req.getContextPath();
			String listUrl = cp + "/bbs/list";
					// 글리스트 주소
			String articleUrl = cp + "/bbs/article?page=" + current_page;
						// 글보기 주소
			if(query.length() != 0) {
				listUrl += "?" + query;
				articleUrl += "&" + query;
			}
			
			// 페이징
			String paging = util.paging(current_page, total_page, listUrl);
			
			// 포워딩할 JSP에 전달할 속성
			mav.addObject("list", list);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("paging", paging);
			mav.addObject("schType", schType);
			mav.addObject("kwd", kwd);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mav;
	}
	
	@RequestMapping(value = "/bbs/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼
		
		ModelAndView mav = new ModelAndView("bbs/write");
		mav.addObject("mode", "write");
		return mav;
	}
	
	@RequestMapping(value = "/bbs/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글등록하기 : 넘어온 파라미터 - subject, content
		
		BoardDAO dao = new BoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
	
		try {
			BoardDTO dto = new BoardDTO();
			
			// userId는 세션에 저장된 정보
			dto.setUserId(info.getUserId());
			
			// 파라미터
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			dao.insertBoard(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/bbs/list");
	}	
	
	@RequestMapping(value = "/bbs/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtilBootstrap();
		
		String page = req.getParameter("page");
		String query = "page=" + page;

		try {
			long num = Long.parseLong(req.getParameter("num"));
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = URLDecoder.decode(kwd, "utf-8");

			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "UTF-8");
			}

			// 조회수 증가
			dao.updateHitCount(num);

			// 게시물 가져오기
			BoardDTO dto = dao.findById(num);
			if (dto == null) { // 게시물이 없으면 다시 리스트로
				return new ModelAndView("redirect:/bbs/list?" + query);
			}
			dto.setContent(util.htmlSymbols(dto.getContent()));

			// 이전글 다음글
			BoardDTO prevDto = dao.findByPrev(dto.getNum(), schType, kwd);
			BoardDTO nextDto = dao.findByNext(dto.getNum(), schType, kwd);


			ModelAndView mav = new ModelAndView("bbs/article");
			
			// JSP로 전달할 속성
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("query", query);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);

			// 포워딩
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/bbs/list?" + query);
	}

	@RequestMapping(value = "/bbs/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 폼
		BoardDAO dao = new BoardDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String page = req.getParameter("page");

		try {
			long num = Long.parseLong(req.getParameter("num"));
			BoardDTO dto = dao.findById(num);

			if (dto == null) {
				return new ModelAndView("redirect:/bbs/list?page=" + page);
			}

			// 게시물을 올린 사용자가 아니면
			if (! dto.getUserId().equals(info.getUserId())) {
				return new ModelAndView("redirect:/bbs/list?page=" + page);
			}

			ModelAndView mav = new ModelAndView("bbs/write");
			
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("mode", "update");

			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/bbs/list?page=" + page);
	}

	@RequestMapping(value = "/bbs/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 완료
		BoardDAO dao = new BoardDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String page = req.getParameter("page");
		try {
			BoardDTO dto = new BoardDTO();
			
			dto.setNum(Long.parseLong(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));

			dto.setUserId(info.getUserId());

			dao.updateBoard(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/bbs/list?page=" + page);
	}

	@RequestMapping(value = "/bbs/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제
		BoardDAO dao = new BoardDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String page = req.getParameter("page");
		String query = "page=" + page;

		try {
			long num = Long.parseLong(req.getParameter("num"));
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = URLDecoder.decode(kwd, "utf-8");

			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "UTF-8");
			}

			dao.deleteBoard(num, info.getUserId(), info.getUserLevel());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/bbs/list?" + query);
	}
	
}
