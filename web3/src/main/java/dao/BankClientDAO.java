package dao;

//import com.sun.deploy.util.SessionState;

import exception.DBException;
import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BankClientDAO {

    private final Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() {
        List<BankClient> list = new ArrayList<>();
        try (Statement st = connection.createStatement()) {
            st.execute("select * from bank_client");
            ResultSet resultSet = st.getResultSet();
            while (resultSet.next()) {
                list.add(new BankClient(resultSet.getLong("id"), resultSet.getString("name"),
                        resultSet.getString("password"), resultSet.getLong("money")));
            }
            resultSet.close();
            st.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean validateClient(String name, String password) {
        try (Statement st = connection.createStatement()) {
            st.execute("select * from bank_client where name='" + name + "'");
            ResultSet res = st.getResultSet();
            res.next();
            String pass = res.getString(3);
            return pass.compareTo(password) == 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateClientsMoney(String name, String password, Long transactValue) {
        try (Statement prst = connection.createStatement()) {
            connection.setAutoCommit(false);
            prst.executeUpdate("update bank_client set money = money + '"+transactValue+"' " +
                    "where name='"+name+"'");
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
            }
            e.printStackTrace();
        }
    }

    public BankClient getClientById(long id) {
        try (Statement st = connection.createStatement()) {
            st.execute("select  * from bank_client where id='" + id + "'");
            ResultSet res = st.getResultSet();
            res.next();
            return new BankClient(res.getLong("id"), res.getString("name"),
                    res.getString("password"), res.getLong("money"));
        } catch (SQLException e) {
            e.printStackTrace();
            return new BankClient();
        }
    }

    public boolean isClientHasSum(String name, Long expectedSum) {
        try (Statement st = connection.createStatement()) {
            st.execute("select * from bank_client where name='" + name + "'");
            ResultSet res = st.getResultSet();
            res.next();
            return res.getLong("money") >= expectedSum;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public long getClientIdByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        long id = result.getLong(1);
        result.close();
        stmt.close();
        return id;

    }

    public boolean deleteClient(String name) {
        if (getClientByName(name) != null) {
            try (Statement st = connection.createStatement()) {
                st.execute("delete from bank_client where name=' " + name + " '");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public BankClient getClientByName(String name) {
        try (Statement st = connection.createStatement()) {
            st.execute("select  * from bank_client where name='" + name + "'");
            ResultSet res = st.getResultSet();
            res.next();
            return new BankClient(res.getLong("id"), res.getString("name"),
                    res.getString("password"), res.getLong("money"));
        } catch (SQLException e) {
            return null;
        }
    }

    public void addClient(BankClient client) throws DBException {
        String name = client.getName();
        String password = client.getPassword();
        long money = client.getMoney();
        try(Statement sq = connection.createStatement()){
            sq.execute("select name from bank_client where name='"+name+"'");
            ResultSet res = sq.getResultSet();
            res.next();
            if(name.compareToIgnoreCase(res.getString(1)) == 0){
                throw new DBException(new Throwable());
            }
        }catch (SQLException eq){
            eq.printStackTrace();
        }

        try (Statement st = connection.createStatement()) {
            st.executeUpdate("insert into bank_client(name, password, money)" +
                    " values ('" + name + "','" + password + "','" + money + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256)," +
                    " password varchar(256), money bigint, primary key (id))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropTable() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
