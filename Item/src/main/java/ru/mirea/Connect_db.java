package ru.mirea;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


@Component
public class Connect_db {
    private static Connection con = null;

    public static Connection getConnection() {
        if (con == null) con = getNewConnection();
        return con;
    }

    private static Connection getNewConnection() {
        String user = "root";
        String password = "root";
        String url = "jdbc:h2:mem:~/DB";
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
