package DAO;


import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class MessageDAO {

    public List<Message> getByPosedBy(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new LinkedList<>();
        try{
            String sql = "select * from message where posted_by = ?";
            PreparedStatement prepStmt = connection.prepareStatement(sql);
            prepStmt.setInt(1, account_id);
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                Message tempMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(tempMessage);
            }
            return messages;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message updateMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        Message dbMessage = getById(message.getMessage_id());
        if (dbMessage != null) {
            try {
                String sql = "update message set message_text = ? where message_id = ?";
                PreparedStatement prepStmt = connection.prepareStatement(sql);
                prepStmt.setString(1, dbMessage.getMessage_text());
                prepStmt.setInt(2, dbMessage.getMessage_id());
                prepStmt.execute();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return dbMessage;
    }
    public Message deleteMessage(int messageID) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = getById(messageID);
        if (message != null) {
            try {
                String sql = "delete from message where message_id = ?";
                PreparedStatement prepStmt = connection.prepareStatement(sql);
                prepStmt.setInt(1, messageID);
                prepStmt.execute();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return message;
    }

    public Message getById(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from message where message_id = ?";
            PreparedStatement prepStmt = connection.prepareStatement(sql);
            prepStmt.setInt(1, messageId);
            ResultSet rs = prepStmt.executeQuery();
            if (rs.next()) {
                return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public Message postMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        AccountDAO accountDAO = new AccountDAO();
        if (accountDAO.getUserByID(message.getPosted_by()) == null) return null;
        try {
            String sql = "insert into message(posted_by, message_text, time_posted_epoch) values(?,?,?)";
            PreparedStatement prepStmt = connection.prepareStatement(sql);
            prepStmt.setInt(1, message.getPosted_by());
            prepStmt.setString(2, message.getMessage_text());
            prepStmt.setLong(3, message.getTime_posted_epoch());
            prepStmt.execute();
            return this.getMessageByPostedByAndTimePosted(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message getMessageByPostedByAndTimePosted(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        ResultSet rs;
        try {
            String sql = "select * from message where posted_by = ? and  message_text  = ? and time_posted_epoch = ?";
            PreparedStatement prepStmt = connection.prepareStatement(sql);
            prepStmt.setInt(1, message.getPosted_by());
            prepStmt.setString(2, message.getMessage_text());
            prepStmt.setLong(3, message.getTime_posted_epoch());
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new LinkedList<>();
        try {
            String sql = "select * from message";
            PreparedStatement prepStmt = connection.prepareStatement(sql);
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                Message tempMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(tempMessage);
            }
            return messages;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
