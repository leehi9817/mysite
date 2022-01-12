package com.javaex.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;

@WebServlet("/user")
public class UserController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("user");

		String action = request.getParameter("action");

		if ("joinForm".equals(action)) {
			System.out.println("user?action=joinForm");

			// 포워드
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinForm.jsp");
		} else if ("join".equals(action)) {
			System.out.println("user?action=join");

			// 파라미터 값 가져오기
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");

			// 파라미터를 vo로 만들기
			UserVo userVo = new UserVo(id, password, name, gender);

			// UserDao의 insert()로 저장하기
			UserDao userDao = new UserDao();
			userDao.insert(userVo);

			// 포워드
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinOk.jsp");

		} else if ("loginForm".equals(action)) {
			System.out.println("user?action=loginForm");

			// 포워드
			WebUtil.forward(request, response, "/WEB-INF/views/user/loginForm.jsp");
		} else if ("login".equals(action)) {
			System.out.println("user?action=login");

			String id = request.getParameter("id");
			String password = request.getParameter("password");

			UserDao userDao = new UserDao();
			UserVo authVo = userDao.getUser(id, password);

			if (authVo == null) { // 로그인 실패
				System.out.println("로그인실패");
				WebUtil.redirect(request, response, "/mysite/user?action=loginForm&result=fail");
			} else { // 로그인 성공
				System.out.println("로그인성공");
				
				HttpSession session = request.getSession();
				session.setAttribute("authUser", authVo);
				
				WebUtil.redirect(request, response, "/mysite/main");
			}
		} else if("logout".equals(action)) {
			
			System.out.println("user?action=logout");
			
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");
			session.invalidate();
			
			WebUtil.redirect(request, response, "/mysite/main");
		} else if("modifyForm".equals(action)) {
			
			System.out.println("user?action=modifyForm");
			
			HttpSession session = request.getSession();
			UserVo authVo = (UserVo)session.getAttribute("authUser");
			
			int no = authVo.getNo();
			
			UserDao userDao = new UserDao();
			authVo = userDao.getUser(no);
			
			session.setAttribute("authUser", authVo);
			
			WebUtil.forward(request, response, "/WEB-INF/views/user/modifyForm.jsp");
		} else if("modify".equals(action)) {
			System.out.println("user?action=modify");

			HttpSession session = request.getSession();
			UserVo authVo = (UserVo)session.getAttribute("authUser");
			
			System.out.println(authVo);
			
			int no = authVo.getNo();
			
			// 파라미터 값 가져오기
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");

			// 파라미터를 vo로 만들기
			authVo = new UserVo(no, password, name, gender);
			
			// UserDao의 update(vo)로 수정하기
			UserDao userDao = new UserDao();
			userDao.update(authVo);
			
			session.setAttribute("authUser", authVo);
			
			WebUtil.redirect(request, response, "/mysite/main");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
