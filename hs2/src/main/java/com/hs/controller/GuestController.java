package com.hs.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.hs.dao.GuestDAO;
import com.hs.domain.GuestDTO;
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
public class GuestController {
	@RequestMapping(value = "/guest/main", method = RequestMethod.GET)
	public ModelAndView main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		return new ModelAndView("guest/guest");
	}
	
	// AJAX - JSON
	public Map<String, Object> list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Map<String, Object> model = new HashMap<String, Object>();
		return model;
	}
	
	// AJAX - JSON
	@ResponseBody
	@RequestMapping(value = "/guest/insert", method = RequestMethod.POST)
	public Map<String, Object> insertSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 방명록 저장
		Map<String, Object> model = new HashMap<String, Object>();
		
		GuestDAO dao = new GuestDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String state = "false";
		try {
			GuestDTO dto = new GuestDTO();
			
			dto.setUserId(info.getUserId());
			dto.setContent(req.getParameter("content"));
			
			dao.insertGuest(dto);
			
			state = "true";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.put("state", state);
		
		return model;
	}
	
	// AJAX - JSON
	public Map<String, Object> deleteSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Map<String, Object> model = new HashMap<String, Object>();
		return model;
	}

}
