package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }
    public  MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public List<Message> getMessages(){
        return messageDAO.getMessages();
    }
    public Message postMessage(Message message){
        return messageDAO.postMessage(message);
    }
    public Message getById(int messageId){
        return  messageDAO.getById(messageId);
    }

    public Message deleteMessage(int messageID){
        return messageDAO.deleteMessage(messageID);
    }

    public Message updateMessage(Message message){
        return  messageDAO.updateMessage(message);
    }

    public List<Message> getByPosedBy(int account_id){
        return messageDAO.getByPosedBy(account_id);
    }
}
