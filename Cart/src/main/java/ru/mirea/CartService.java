package ru.mirea;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Null;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CartService {

    private Connect_db connect_db;

    private static Connection con = null;
    private static Statement stmt;
    private static ResultSet rs;
    private static RestTemplate restTemplate;

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
        restTemplate = new RestTemplate();

        }

    public List<HashMap<String, String>> cart(int userId) {
        return get("SELECT * FROM cart WHERE userId = " + userId);
    }

    public List<HashMap<String, String>> getStuff(int userId) {
        List<HashMap<String, String>> stuff = getStuff();
        List<HashMap<String, String>> cart = get("SELECT * FROM cart WHERE cart.userId = " + userId);
        List<HashMap<String, String>> result = new ArrayList<>();

        for (int i=0; i<cart.size(); i++) {
            for (int j=0; j<stuff.size(); j++) {
                Map cartMap = cart.get(i);
                Map stuffMap = stuff.get(j);
                if (cartMap.get("ITEMID").toString().equals(stuffMap.get("ID").toString())) {
                    result.add(cart.get(i));
                    break;
                }
            }

        }
        return result;
    }

    public List<HashMap<String, String>> getPets(int userId) {

        List<HashMap<String, String>> pets = getPets();
        List<HashMap<String, String>> cart = get("SELECT * FROM cart WHERE cart.userId = " + userId);
        List<HashMap<String, String>> result = new ArrayList<>();

        for (int i=0; i<cart.size(); i++) {
            for (int j=0; j<pets.size(); j++) {
                Map cartMap = cart.get(i);
                Map petsMap = pets.get(j);
                if (cartMap.get("ITEMID").toString().equals(petsMap.get("ID").toString())) {
                    result.add(cart.get(i));
                    break;
                }
            }

        }
        return result;
    }

    public void put(int userId, int itemId) {
        boolean f = false;
        List<HashMap<String, String>> items = getPets();
        List<HashMap<String, String>> stuff = getStuff();
        items.addAll(stuff);

        for (int i=0; i<items.size(); i++) {
            Map map = new HashMap();
            if (map.get("ID").equals(itemId)) {
                f = true;
                break;
            }
        }
        if (!f) throw new NullPointerException(("No item found"));
        String q = "INSERT INTO cart(userId, itemId) VALUES(" + userId + "," + itemId + ")";
        try {
            stmt.executeUpdate(q);
        } catch (Exception e) {};

        /*
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
        */

    }

    public void delete(int userId, int itemId) {
        String q = "DELETE FROM cart WHERE userId = " + userId + " AND itemId = " + itemId;
        try {
            stmt.executeUpdate(q);
        } catch (Exception e) {
            System.out.println("No record found");
        };
    }

    private List<HashMap<String, String>> get(String q) {
        List<HashMap<String, String>> result = new ArrayList<>();
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

    private List<HashMap<String, String>> getPets() {
        ResponseEntity<List<HashMap<String, String>>> responseEntity = restTemplate.exchange
                ("http://localhost:8083/pets", HttpMethod.GET,
                        null, new ParameterizedTypeReference<List<HashMap<String, String>>>(){});
        return responseEntity.getBody();
    }

    private List<HashMap<String, String>> getStuff() {
        ResponseEntity<List<HashMap<String, String>>> responseEntity = restTemplate.exchange
                ("http://localhost:8083/stuff", HttpMethod.GET,
                        null, new ParameterizedTypeReference<List<HashMap<String, String>>>(){});
        return responseEntity.getBody();
    }

    public List<HashMap<String, String>> getBalance(int userId) {

        ResponseEntity<List<HashMap<String, String>>> responseEntity = restTemplate.exchange
                ("http://localhost:8081/balance/{id}", HttpMethod.GET,
                        null, new ParameterizedTypeReference<List<HashMap<String, String>>>(){}, userId);
        return responseEntity.getBody();

    }

    private void setBalance(int userId, int bal) {
        ResponseEntity<List<HashMap<String, String>>> responseEntity = restTemplate.exchange
                ("http://localhost:8081/balance/{id}={balance}", HttpMethod.PUT, null,
                        new ParameterizedTypeReference<List<HashMap<String, String>>>(){}, userId, bal);

    }



    public void post(int userId) {
        int sum = 0;
        int bal = 0;
        List<HashMap<String, String>> items = getPets();
        items.addAll(getStuff());
        List<HashMap<String, String>> cart = cart(userId);
        HashMap<String, String> balance = getBalance(userId).get(0);
        bal = Integer.parseInt(balance.get("BALANCE"));

        for (int i=0; i<items.size(); i++) {
            Map cartMap = new HashMap<>();
            for (int j=0; j<items.size(); j++) {
                Map itemsMap = new HashMap<>();
                if (cartMap.get("ITEMID").equals(itemsMap.get("ID"))) {
                    sum += (int)cartMap.get("PRICE");
                }
            }
        }
        if (bal < sum) throw new UnsupportedOperationException("Not enough balance");
        try {
            stmt.executeUpdate("DELETE FROM cart WHERE userId = " + userId);
        } catch (Exception e) {};
        setBalance(userId, bal-sum);

    }


    @Autowired
    public void setConnect_db (Connect_db connect_db) {
        this.connect_db = connect_db;
    }
}
