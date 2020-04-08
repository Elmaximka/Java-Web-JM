package servlets;

import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/mult")
public class TestServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String value = request.getParameter("value");
        response.setContentType("text/html;charset=utf-8");
        try {
            response.getWriter().println(Integer.parseInt(value) * 2);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            response.getWriter().println(0);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


}