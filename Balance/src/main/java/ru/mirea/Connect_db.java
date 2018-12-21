package ru.mirea;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;


@Component
public class Connect_db {

    private Connection con = null;

    public Connection getConnection() {
        if (con == null) con = getNewConnection();
        return con;
    }

    private Connection getNewConnection() {
        String user = "root";
        String password = "root";
        String url = "jdbc:h2:mem:~/Balance_DB";
        try {
            Class.forName("org.h2.Driver").newInstance();
            con=DriverManager.getConnection(url, user, password);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}
