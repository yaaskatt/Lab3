package ru.mirea;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

@Component
public class CartService {

    private Connect_db connect_db;

    private static Connection con = null;
    private static Statement stmt;
    private static ResultSet rs;

    @PostConstruct
    public void init() {
        con = connect_db.getConnection();
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE cart (" +
                    "id INT(10) PRIMARY KEY AUTO_INCREMENT," +
                    "userId INT(10)," +
                    "itemId INT(10)" +
                    ")");
            stmt.executeUpdate("INSERT INTO cart(userId, itemId) VALUES " +
                    "(1, 2), (1,4)," +
                    "(2, 3), (2, 4)," +
                    "(3, 5)," +
                    "(4, 6), (4, 1)," +
                    "(5, 2), (5, 3), (5, 5)");
        } catch (Exception e) {e.printStackTrace();}

        }

    public List cart(int userId) {
        String q = "SELECT * FROM cart WHERE userId = " + userId;
        return get(q);
    }

    public List getStuff(int userId) {
        String q = "SELECT * FROM cart INNER JOIN stuff ON cart.itemId = stuff.id WHERE cart.userId = " + userId;
        return get(q);
    }

    public List getPets(int userId) {
        String q = "SELECT * FROM cart INNER JOIN pets ON " +
                "cart.itemId = pets.id WHERE cart.userId = " + userId;
        return get(q);
    }

    public void put(int userId, int itemId) {
        boolean n = true;
        String check = "SELECT * FROM pets WHERE id = " + itemId + " UNION SELECT * FROM stuff WHERE id = " + itemId;
        try {
            rs = stmt.executeQuery(check);
        } catch (Exception e) {
        };

        try {
        n = rs.next();
        } catch (Exception e) {};
        if (n == false) throw new NullPointerException("No item found");
        String q = "INSERT INTO cart(userId, itemId) VALUES(" + userId + "," + itemId + ")";
        try {
            stmt.executeUpdate(q);
        } catch (Exception e) {};

    }

    public void delete(int userId, int itemId) {
        String q = "DELETE FROM cart WHERE userId = " + userId + " AND itemId = " + itemId;
        try {
            stmt.executeUpdate(q);
        } catch (Exception e) {
        };
    }

    private List get(String q) {
        List result = null;
        rs = null;
        try {
            rs = stmt.executeQuery(q);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException("User not found");
        };
        try {
            result = Convertion.resultSetToArrayList(rs);
        } catch (Exception e) {e.printStackTrace();};
        return result;
    }

    public void post(int userId) {
        int petSum = 0;
        int stuffSum = 0;
        int totalPrice = 0;
        int bal = 0;
        String q = "SELECT SUM(pets.price) FROM pets INNER JOIN cart ON cart.itemId = pets.id WHERE cart.userId = " + userId;
        try {
            rs = stmt.executeQuery(q);
            rs.next();
            petSum = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        };
        q = "SELECT SUM(stuff.price) FROM stuff INNER JOIN cart ON cart.itemId = stuff.id WHERE cart.userId = " + userId;
        try {
            rs = stmt.executeQuery(q);
            rs.next();
            stuffSum = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        };
        totalPrice = petSum + stuffSum;
        q = "SELECT balance FROM balance WHERE userId = " + userId;
        try {
            rs = stmt.executeQuery(q);
            rs.next();
            bal = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        };
        if (bal >= totalPrice) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.put("http://localhost:8081/balance/{id}={balance}", null , userId, bal-totalPrice);
        }
        else throw new UnsupportedOperationException("Not enough balance");
        q = "DELETE FROM cart WHERE userId = " + userId;
        try {
            stmt.executeUpdate(q);
        } catch (Exception e) {};
    }


    @Autowired
    public void setConnect_db (Connect_db connect_db) {
        this.connect_db = connect_db;
    }
}
