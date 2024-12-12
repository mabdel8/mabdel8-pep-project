package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::addMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        //checks to see if the registration was successful
        if(addedAccount != null){
            context.json(mapper.writeValueAsString(addedAccount));
        } else {
            context.status(400);
        }
    }

    private void loginHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account loginAccount = accountService.login(account);
        if(loginAccount != null){
            context.json(mapper.writeValueAsString(loginAccount));
        } else {
            context.status(401);
        }
    }

    private void getAllMessagesHandler(Context context) {
        context.json(messageService.getAllMessages());
    }

    private void addMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message msg = mapper.readValue(context.body(), Message.class);
        //this will make sure that the account exists before attempting to add the message
        if (accountService.getAccountByID(msg.getPosted_by()) == null){
            context.status(400);
        }else {
            Message addedmsg = messageService.addMessage(msg);
            if(addedmsg != null){
                context.json(mapper.writeValueAsString(addedmsg));
            } else {
                context.status(400);
            }
        }

    }

    private void getMessageByIdHandler(Context context) {
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message msg = messageService.getMessageById(id);

        if (msg != null){
            context.json(msg);
        } else {
            context.json("");
        }
    }

    private void deleteMessageByIdHandler(Context context){
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message msg = messageService.deleteMessageById(id);

        if (msg != null){
            context.json(msg);
        } else {
            context.json("");
        }
    }

    private void updateMessageByIdHandler(Context context) throws JsonProcessingException{
        int id = Integer.parseInt(context.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message msg = mapper.readValue(context.body(), Message.class);
        Message updatedMsg = null;

        if (messageService.getMessageById(id) == null){
            context.status(400);
        } else {
            updatedMsg = messageService.updateMessageById(id, msg);
            if (updatedMsg == null){
                context.status(400);
            } else {
                context.json(updatedMsg);
            }
        }
    }

    private void getMessagesByAccountHandler(Context context){
        int id = Integer.parseInt(context.pathParam("account_id"));
        context.json(messageService.getMessagesByAccount(id));
    }


}