package cn.classroom.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.classroom.dao.ClassroomDao;
import cn.classroom.dao.impl.ClassroomDaoImpl;
import cn.classroom.domain.Classroom;
import cn.classroom.domain.QueryResult;
import cn.classroom.service.BusinessService;
import cn.classroom.service.impl.BusinessServiceImpl;
import cn.classroom.utils.WebUtils;
import cn.classroom.web.formbean.UpdateClassroomForm;

public class UpdateClassroomServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		// 1.校验提交表单的字段进行合法性校验(把表单数据封装到formbean)
		UpdateClassroomForm form = WebUtils.request2Bean(request,
				UpdateClassroomForm.class);
		Boolean b = form.validate();

		// 2.如果校验失败,跳回到表单页面,回显校验失败信息
		if (!b) {
			request.setAttribute("form", form);
			request.setAttribute("room_no", form.getRoom_no());
			request.getRequestDispatcher("/WEB-INF/jsp/addclassroom.jsp").forward(
					request, response);
			return;
		}

		// 3.如果校验成功,则调用service处理注册请求
		Classroom c = new Classroom();
		WebUtils.copyBean(form, c);
		ClassroomDao dao = new ClassroomDaoImpl();
		try {
			Classroom classroom = new Classroom();
			WebUtils.copyBean(form, classroom);
			dao.updateClassroom(classroom);
			// 6.如果service处理成功,跳回用户管理界面
			BusinessService service = new BusinessServiceImpl();
			QueryResult qr = service.queryClassroom();
			request.setAttribute("queryresult", qr);
			request.setAttribute("alert", "修改成功！");
			request.getRequestDispatcher("/WEB-INF/jsp/classroom-mana-main.jsp")
					.forward(request, response);
			return;
		} catch (Exception e) {
			// 5.如果service处理不成功,并且不成功的原因是其他问题的话,则跳转到网站的全局消息显示页面,为用户显示友好错误消息
			request.setAttribute("message", "服务器出现错误！");
			request.getRequestDispatcher("/message.jsp").forward(request,
					response);
			e.printStackTrace();
			return;
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
