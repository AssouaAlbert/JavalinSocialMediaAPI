package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Util.ConnectionUtil;
import Model.Account;

public class AccountDAO {

    public Account registerUser(Account user) {
        if (this.getUser(user.getUsername()) != null) return null;
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "insert into account(username, password) values(?, ?)";
            PreparedStatement prepStmt = connection.prepareStatement(sql);
            prepStmt.setString(1, user.getUsername());
            prepStmt.setString(2, user.getPassword());
            prepStmt.execute();
            return this.getUser(user.getUsername());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account loginUser(Account user) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from account username= '?' and password = '?')";
            PreparedStatement prepStmt = connection.prepareStatement(sql);
            prepStmt.setString(1, user.getUsername());
            prepStmt.setString(2, user.getPassword());
            ResultSet rs = prepStmt.executeQuery();
            if (rs.next()) {
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account getUser(String username) {
        Connection connection = ConnectionUtil.getConnection();
        ResultSet res;
        try {
            String sql = "select * from account where username = '?'";
            PreparedStatement prepStmt = connection.prepareStatement(sql);
            prepStmt.setString(1, username);
            res = prepStmt.executeQuery();
            if (res.next()) {
                return new Account(res.getInt("account_id"), res.getString("username"), res.getString("password"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account getUserByID(int userId) {
        Connection connection = ConnectionUtil.getConnection();
        ResultSet res;
        try {
            String sql = "select * from account where account_id = '?'";
            PreparedStatement prepStmt = connection.prepareStatement(sql);
            prepStmt.setInt(1, userId);
            res = prepStmt.executeQuery();
            if (res.next()) {
                return new Account(res.getInt("account_id"), res.getString("username"), res.getString("password"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
