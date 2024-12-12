package Service;
import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService {
    public MessageDAO messageDAO;
    public MessageService(){
        messageDAO = new MessageDAO();
    }
    public Message addMessage(Message msg){
        return messageDAO.insertMessage(msg);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id){
        return messageDAO.getMessageById(id);
    }

    public Message deleteMessageById(int id){
        return messageDAO.deleteMessageById(id);
    }

    public Message updateMessageById(int id, Message msg){
        return messageDAO.updateMessageById(id, msg);
    }

    public List<Message> getMessagesByAccount(int accountId){
        return messageDAO.getMessagesByAccount(accountId);
    }
}
