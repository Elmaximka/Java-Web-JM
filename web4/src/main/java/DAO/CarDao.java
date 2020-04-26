package DAO;

import model.Car;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CarDao {

    private Session session;

    public CarDao(Session session) {
        this.session = session;
    }

    public List<Car> getAllCars() {
        Transaction transaction = session.beginTransaction();
        List<Car> list = session.createQuery("FROM Car").list();
        transaction.commit();
        session.close();
        return list;
    }

    public boolean addCar(Car car) {
        Transaction transaction = session.beginTransaction();
        List<Car> carList = session.createQuery("from Car as c where c.brand = :name")
                .setString("name", car.getBrand())
                .list();
        if (carList.toArray().length < 10) {
            session.save(car);
            transaction.commit();
            session.close();
            return true;
        } else {
            transaction.commit();
            session.close();
            return false;
        }
    }

    public Car buyCar(String model, String brand, String plate) {
        Transaction transaction = session.beginTransaction();
        List<Car> cars = session.createQuery("from Car where brand = :brand and model = :model and licensePlate = :plate")
                .setString("brand", brand)
                .setString("model", model)
                .setString("plate", plate)
                .list();
        try {
            long id = cars.get(0).getId();
            session.createQuery("delete Car c where id = :id").setLong("id", id).executeUpdate();
        } catch (IndexOutOfBoundsException ignored) {
        }
        transaction.commit();
        session.close();
        return cars.get(0);
    }
    public void deleteAllCars(){
        Transaction transaction = session.beginTransaction();
        session.createSQLQuery("drop table cars").executeUpdate();
        session.createSQLQuery("create table cars (id bigint not null auto_increment, brand varchar(255), licensePlate varchar(255), model varchar(255), price bigint, primary key (id))").executeUpdate();
        transaction.commit();
        session.close();
    }
}
