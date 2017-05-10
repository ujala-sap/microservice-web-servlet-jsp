package com.microservice.servletjsp.web.servlet.todo;

import static com.microservice.servletjsp.web.util.Views.HOME_PAGE;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.microservice.domain.TodoItemList;
import com.microservice.domain.User;
import com.microservice.service.api.TodoService;
import com.microservice.web.common.util.TodoListUtils;

@WebServlet(name = "ListTodoServlet", urlPatterns = "/todos")
public class ListTodoServlet extends HttpServlet {

	private static final long serialVersionUID = 1213014138044619496L;
	private TodoService todoService;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		ApplicationContext applicationContext = WebApplicationContextUtils
				.getWebApplicationContext(servletConfig.getServletContext());
		todoService = applicationContext.getBean(TodoService.class);
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(TodoListUtils.SESSION_USER);
		TodoItemList todoList = todoService.getTodoListByUser(user.getId());

		// todo list is request scoped to avoid storing and synchronizing it in
		// session for each CRUD operation
		request.setAttribute("todoList", todoList.getItems());
		request.setAttribute("homeTabStyle", "active");

		request.setAttribute("totalCount", todoList.getCount());
		request.setAttribute("doneCount", todoList.getDoneCount());
		request.setAttribute("todoCount", todoList.getTodoCount());

		request.getRequestDispatcher(HOME_PAGE).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
