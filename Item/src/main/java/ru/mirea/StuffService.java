package ru.mirea;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mirea.Connect_db;
import ru.mirea.Convertion;
import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class StuffService {

    private Connect_db connect_db;

    private static Connection con = null;
    private static Statement stmt;
    private static ResultSet rs;

    @PostConstruct
    public void init(){
        con = Connect_db.getConnection();
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE stuff(" +
                    "id INT(10) PRIMARY KEY," +
                    "name VARCHAR(30)," +
                    "price INT(6)" +
                    ")");
            stmt.executeUpdate("INSERT INTO stuff VALUES " +
                    "(4, 'brush', 300)," +
                    "(5, 'shampoo', 800)," +
                    "(6, 'toy', 400)");
        } catch (Exception e) {e.printStackTrace();};
    }

    public List<HashMap<String, String>> stuff() {
        String q = "SELECT * FROM stuff";
        List<HashMap<String, String>> result = new ArrayList<>();
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
