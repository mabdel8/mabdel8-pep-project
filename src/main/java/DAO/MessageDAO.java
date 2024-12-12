package DAO;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MessageDAO {
    public Message insertMessage(Message msg){
        Connection connection = ConnectionUtil.getConnection();
        //Check to see if the message is valid first
        if (msg.getMessage_text().length() > 255 || msg.getMessage_text().isEmpty()){
            return null;
        }
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, msg.getPosted_by());
            ps.setString(2, msg.getMessage_text());
            ps.setLong(3, msg.getTime_posted_epoch());

            ps.executeUpdate();
            // using this to get the updated row
            ResultSet pKeyResultSet = ps.getGeneratedKeys();

            if(pKeyResultSet.next()){
                //here we return the newly added row
                int generated_message_id = (int) pKeyResultSet.getLong(1);
                return new Message(generated_message_id, msg.getPosted_by(), msg.getMessage_text(), msg.getTime_posted_epoch());
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages(){
        List<Message> msgList = new ArrayList<>();

        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int id = rs.getInt("message_id");
                int posted_by = rs.getInt("posted_by");
                String msg_text = rs.getString("message_text");
                long time_posted = rs.getLong("time_posted_epoch");

                Message newMsg = new Message(id, posted_by, msg_text, time_posted);
                msgList.add(newMsg);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        return msgList;
    }

    public Message getMessageById(int id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int msg_id = rs.getInt("message_id");
                int posted_by = rs.getInt("posted_by");
                String msg_text = rs.getString("message_text");
                long time_posted = rs.getLong("time_posted_epoch");

                Message newMsg = new Message(msg_id, posted_by, msg_text, time_posted);
                return newMsg;
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        Message messageToDelete = null;

        // need to query first to return the message that needs to be deleted
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int msg_id = rs.getInt("message_id");
                int posted_by = rs.getInt("posted_by");
                String msg_text = rs.getString("message_text");
                long time_posted = rs.getLong("time_posted_epoch");

                messageToDelete = new Message(msg_id, posted_by, msg_text, time_posted);
            }

            if (messageToDelete != null){
                String dltSql = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement ps2 = connection.prepareStatement(dltSql);
                ps2.setInt(1, id);
                ps2.executeUpdate();
            }


        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        return messageToDelete;
    }

    public Message updateMessageById(int id, Message msg){
        Connection connection = ConnectionUtil.getConnection();
        if (msg.getMessage_text().length() > 255 || msg.getMessage_text().isEmpty()){
            return null;
        }

        try {
            String updateSql = "Update message Set message_text = ? WHERE message_id = ?";
            PreparedStatement ps2 = connection.prepareStatement(updateSql);
            ps2.setString(1, msg.getMessage_text());
            ps2.setInt(2, id);
            ps2.executeUpdate();
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int msg_id = rs.getInt("message_id");
                int posted_by = rs.getInt("posted_by");
                String msg_text = rs.getString("message_text");
                long time_posted = rs.getLong("time_posted_epoch");
                return new Message(msg_id, posted_by, msg_text, time_posted);
            }

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        return null;

    }

    public List<Message> getMessagesByAccount(int accountId){
        List<Message> messages = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("message_id");
                int posted_by = rs.getInt("posted_by");
                String msg_text = rs.getString("message_text");
                long time_posted = rs.getLong("time_posted_epoch");

                Message newMsg = new Message(id, posted_by, msg_text, time_posted);
                messages.add(newMsg);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
