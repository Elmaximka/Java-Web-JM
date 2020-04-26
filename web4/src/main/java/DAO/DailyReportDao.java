package DAO;

import model.DailyReport;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class DailyReportDao {

    private Session session;

    public DailyReportDao(Session session) {
        this.session = session;
    }

    public List<DailyReport> getAllDailyReport() {
        Transaction transaction = session.beginTransaction();
        List<DailyReport> dailyReports = session.createQuery("FROM DailyReport").list();
        transaction.commit();
        session.close();
        return dailyReports;
    }

    public DailyReport getLastReport() {
        Transaction transaction = session.beginTransaction();
        long lastId = session.createQuery("from DailyReport ").list().toArray().length;
        Query query = session.createQuery("from DailyReport where id = :id");
        List<DailyReport> lastReport;
        if (lastId == 1) {
            lastReport = query.setLong("id", lastId).list();
        } else {
            lastId--;
            lastReport = query.setLong("id", lastId).list();
        }
        transaction.commit();
        session.close();
        return lastReport.get(0);
    }

    public void deleteReports() {
        Transaction transaction = session.beginTransaction();
        session.createSQLQuery("drop table daily_reports").executeUpdate();
        session.createSQLQuery("create table daily_reports (id bigint not null auto_increment, earnings bigint, soldCars bigint, primary key (id))").executeUpdate();
        transaction.commit();
        session.close();
    }

    public void addReport(long earnings, long soldCars, long id) {
        Transaction transaction = session.beginTransaction();
        session.createQuery("update DailyReport d set d.earnings = :earnings, d.soldCars = :soldCars where id = :id")
                .setString("earnings", String.valueOf(earnings))
                .setString("soldCars", String.valueOf(soldCars))
                .setLong("id", id)
                .executeUpdate();
        transaction.commit();
        session.close();

    }

    public void newDay() {
        DailyReport dailyReport = new DailyReport(0l, 0l);
        Transaction transaction = session.beginTransaction();
        session.save(dailyReport);
        transaction.commit();
        session.close();
    }

}
