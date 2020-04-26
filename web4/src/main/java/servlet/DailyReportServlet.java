package servlet;

import com.google.gson.Gson;
import service.CarService;
import service.DailyReportService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DailyReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        String json = null;
        if (req.getPathInfo().contains("all")) {
            json = gson.toJson(DailyReportService.getInstance().getAllDailyReports());
        } else if (req.getPathInfo().contains("last")) {
            json = gson.toJson(DailyReportService.getInstance().getLastReport());
        }
        resp.setStatus(200);
        resp.getWriter().println(json);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DailyReportService.getInstance().deleteReports();
        CarService.getInstance().deleteAllCars();
        resp.setStatus(200);
    }
}
