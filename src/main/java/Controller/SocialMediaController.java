package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     *
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerUserController);
        app.post("/login", this::userLogin);
        app.post("/messages", this::postNewMessage);
        app.get("/messages", this::getMessages);
        app.get("/messages/{message_id}", this::getMessage);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUser);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     *
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessagesByUser(Context ctx){
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> message = messageService.getByPosedBy(account_id);
        ctx.status(200).json(message);
    }
    private void updateMessage(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        if (message.getMessage_text().isEmpty() || message.getMessage_text().length() > 254) {
            ctx.status(400).result("Message should not be blank ot longer than 254 characters.");
        }
        message = messageService.updateMessage(message);
        if (message != null) ctx.status(200).json(message);
        else ctx.status(400).result("Bad Request! Your message could not be posted.");
    }

    private void deleteMessage(Context ctx) {
        int messageID = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessage(messageID);
        ctx.status(200).json(message);
    }

    private void getMessage(Context ctx) {
        int messageID = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getById(messageID);
        ctx.status(200).json(message);
    }

    private void getMessages(Context ctx) {
        List<Message> messages;
        messages = messageService.getMessages();
        ctx.status(200).json(messages);
    }

    private void postNewMessage(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        if (message.getMessage_text().isEmpty() || message.getMessage_text().length() > 254) {
            ctx.status(400).result("Message should not be blank ot longer than 254 characters.");
        }
        message = messageService.postMessage(message);
        if (message != null) ctx.status(200).json(message);
        else ctx.status(400).result("Bad Request! Your message could not be posted.");
    }

    private void registerUserController(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account user = mapper.readValue(ctx.body(), Account.class);
        if (user.getUsername().isEmpty()) {
            ctx.status(400).result("Bad Request! Add username.");
        } else if (user.getPassword().length() < 4) {
            ctx.status(400).result("Bad Request! Password has to be at least 4 characters.");
        }
        Account newUser = accountService.registerUser(user);
        if (newUser == null) ctx.status(400).result("Problems Creating User; Username may already exist.");
        else ctx.status(200).json(newUser);
    }

    private void userLogin(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account user = mapper.readValue(ctx.body(), Account.class);
        if (user.getUsername().isEmpty()) {
            ctx.status(400).result("Bad Request! Enter username.");
        } else if (user.getPassword().isEmpty()) {
            ctx.status(400).result("Bad Request! Enter password.");
        }
        Account activeUser = accountService.loginUser(user);
        if (activeUser == null) ctx.status(400).result("Sorry, wrong credentials (username or password)");
        else ctx.status(200).json(activeUser);

    }

}