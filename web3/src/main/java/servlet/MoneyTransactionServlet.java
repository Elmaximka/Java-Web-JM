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

public class MoneyTransactionServlet extends HttpServlet {

    BankClientService bankClientService = BankClientService.instance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", new HashMap<>()));
        resp.setStatus(200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("senderName");
        String password = req.getParameter("senderPass");
        String sendTo = req.getParameter("nameTo");
        String count = req.getParameter("count");
        Long money = Long.parseLong(count);
        Map<String, Object> map = new HashMap<>();
        if(bankClientService.sendMoneyToClient(new BankClient(name, password, money), sendTo, money)){
            map.put("message", "The transaction was successful");

        }else {
            map.put("message", "transaction rejected");
        }
        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", map));
        resp.setStatus(200);
    }
}
