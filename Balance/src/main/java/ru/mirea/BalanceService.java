package ru.mirea;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class BalanceService {

    private Connect_db connect_db;

    private static Connection con = null;
    private static Statement stmt;
    private static ResultSet rs;

    public void createDB() {

        con = connect_db.getConnection();

        try {
            stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE balance(" +
                    "userId INT(10) PRIMARY KEY, balance INT(10)" +
                    ")");
            stmt.executeUpdate("INSERT INTO balance VALUES (1, 1000), (2, 4000), (3, 500), (4, 0), (5, 12000)");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
    }

    public List<HashMap<String, String>> balance(int id) {
        String q = "SELECT * FROM balance WHERE userId = " + id;
        List<HashMap<String, String>> result = new ArrayList<>();
        rs = null;
        try {
            rs = stmt.executeQuery(q);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException("User not found");
        }
        ;

        try {
            result = Convertion.resultSetToArrayList(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
        return result;
    }

    public void putNewBal(int userId, double newBal) {
        String q = "UPDATE balance SET balance = " + newBal + " WHERE userId = " + userId;
        try {
            stmt.executeUpdate(q);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException("User not found");
        }
        ;
    }

    @Autowired
    public void setConnect_db(Connect_db connect_db) {
        this.connect_db = connect_db;
    }
}

