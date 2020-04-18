package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public final class BankClientService {

    private static BankClientService instance;

    private BankClientService() {
    }

    public static BankClientService instance() {
        if (instance == null) {
            instance = new BankClientService();
        }
        return instance;
    }

    public BankClient getClientById(long id) {
        return getBankClientDAO().getClientById(id);
    }

    public BankClient getClientByName(String name) {
        return getBankClientDAO().getClientByName(name);
    }

    public List<BankClient> getAllClient() {
        return getBankClientDAO().getAllBankClient();
    }

    public boolean deleteClient(String name) {
        return getBankClientDAO().deleteClient(name);
    }

    public boolean addClient(BankClient client) {
        try {
            if (client.getName() != null && client.getPassword() != null && client.getMoney() >= 0) {
                createTable();
                getBankClientDAO().addClient(client);
                return true;
            }
        } catch (DBException e) {
                return false;
        }
        return false;
    }

    public boolean sendMoneyToClient(BankClient sender, String name, Long value) {
        if (getBankClientDAO().validateClient(sender.getName(), sender.getPassword())) {
            if (value >= 0) {
                if (getBankClientDAO().isClientHasSum(sender.getName(), value)) {
                    getBankClientDAO().updateClientsMoney(name, sender.getPassword(), value);
                    getBankClientDAO().updateClientsMoney(sender.getName(), sender.getPassword(), -value);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public void cleanUp() {
        getBankClientDAO().dropTable();
    }

    public void createTable() {
        getBankClientDAO().createTable();
    }

    private static Connection getMysqlConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("web3?").                //db name
                    append("useUnicode=true&").     //unicode
                    append("serverTimezone=Europe/Moscow");  // setTimeZone

            System.out.println("URL: " + url + "\n");
            return DriverManager.getConnection(url.toString(), "root", "root");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    private static BankClientDAO getBankClientDAO() {
        return new BankClientDAO(getMysqlConnection());
    }
}
