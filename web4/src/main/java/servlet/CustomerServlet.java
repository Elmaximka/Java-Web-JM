package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import model.Car;
import model.DailyReport;
import service.CarService;
import service.DailyReportService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        String json = gson.toJson(CarService.getInstance().getAllCars());
        resp.getWriter().print(json);
        resp.setStatus(200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        String json = gson.toJson(CarService.getInstance()
                .buyCar(req.getParameter("model"), req.getParameter("brand"), req.getParameter("licensePlate")));
        Car car = gson.fromJson(json, Car.class);
        DailyReportService.getInstance().addReprot(car.getPrice());
        resp.getWriter().print(json);
        resp.setStatus(200);

    }

}
