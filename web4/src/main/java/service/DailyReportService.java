package service;

import DAO.DailyReportDao;
import model.DailyReport;
import org.hibernate.SessionFactory;
import util.DBHelper;

import java.util.List;

public class DailyReportService {

    private static DailyReportService dailyReportService;

    private SessionFactory sessionFactory;

    private DailyReportService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public static DailyReportService getInstance() {
        if (dailyReportService == null) {
            dailyReportService = new DailyReportService(DBHelper.getSessionFactory());
        }
        return dailyReportService;
    }

    public List<DailyReport> getAllDailyReports() {
        return new DailyReportDao(sessionFactory.openSession()).getAllDailyReport();
    }


    public DailyReport getLastReport() {
        return new DailyReportDao(sessionFactory.openSession()).getLastReport();
    }

    public void deleteReports() {
        DailyReportDao dailyReport = new DailyReportDao(sessionFactory.openSession());
        dailyReport.deleteReports();
    }

    public void addReprot(long price) {
        DailyReportDao dailyReport = new DailyReportDao(sessionFactory.openSession());
        DailyReportDao dailyReport1 = new DailyReportDao(sessionFactory.openSession());
        try {
            long earnings = new DailyReportDao(sessionFactory.openSession()).getLastReport().getEarnings() + price;
            long soldCars = new DailyReportDao(sessionFactory.openSession()).getLastReport().getSoldCars() + 1;
            long id = new DailyReportDao(sessionFactory.openSession()).getLastReport().getId();
            dailyReport.addReport(earnings, soldCars, id);
        }   catch (IndexOutOfBoundsException e){
            dailyReport.newDay();
            dailyReport1.addReport(price,1, 1);
        }
    }

    public void newDay() {
        DailyReportDao dailyReport = new DailyReportDao(sessionFactory.openSession());
        dailyReport.newDay();
    }
}
