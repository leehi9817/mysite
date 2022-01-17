package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.BoardDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;

@WebServlet("/board")
public class BoardController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		System.out.println("board");
		
		String action = request.getParameter("action");
		
		if ("writeForm".equals(action)) {
			
		} else if ("write".equals(action)) {
			
		} else if ("read".equals(action)) {
			
		} else if ("modifyForm".equals(action)) {
			
		} else if ("modify".equals(action)) {
			
		} else if ("delete".equals(action)) {
			
			System.out.println("action=delete");
			
			int no = Integer.parseInt(request.getParameter("no"));
			
			BoardVo boardVo = new BoardVo();
			boardVo.setNo(no);
			
			BoardDao boardDao = new BoardDao();
			
			boardDao.delete(boardVo);
		
			WebUtil.redirect(request, response, "/mysite/board");
			
		} else {
			BoardDao boardDao = new BoardDao();
			List<BoardVo> boardList = boardDao.getList();
			
			request.setAttribute("bList", boardList);
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
