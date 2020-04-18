package servlet;

import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {
    BankClientService bankClientService = BankClientService.instance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", new HashMap<>()));
        resp.setStatus(200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> map = new HashMap<>();
        try {
            if (bankClientService.addClient(new BankClient(req.getParameter("name"), req.getParameter("password"),
                    Long.parseLong(req.getParameter("money"))))) {
                map.put("message", "Add client successful");
            } else {
                map.put("message", "Client not add");
            }
            resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", map));
        }catch (IOException | NumberFormatException e){
            map.put("message", "Client not add");
            resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", map));
        }
    }
}
