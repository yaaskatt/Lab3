package ru.mirea.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mirea.Connect_db;
import ru.mirea.Convertion;
import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

@Component
public class PetService {

    private Connect_db connect_db;

    private static Connection con = null;
    private static Statement stmt;
    private static ResultSet rs;

    @PostConstruct
    public void init() {
        con = Connect_db.getConnection();
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE pets(" +
                    "id INT(10) PRIMARY KEY," +
                    "name VARCHAR(30)," +
                    "price INT(6)" +
                    ")");
            stmt.executeUpdate("INSERT INTO pets VALUES " +
                    "(1, 'cat', 2000)," +
                    "(2, 'dog', 3000)," +
                    "(3, 'rabbit', 5000)");
        } catch (Exception e) {e.printStackTrace();};
    }

    public List pets() {
        String q = "SELECT * FROM pets";
        List result = null;
        try {
            rs = stmt.executeQuery(q);
        } catch (Exception e) {e.printStackTrace();};
        try {
            result = Convertion.resultSetToArrayList(rs);
        } catch (Exception e) {e.printStackTrace();};
        return result;
    }

    @Autowired
    public void setConnect_db (Connect_db connect_db) {
        this.connect_db = connect_db;
    }

}
