package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;


    @PostMapping("/register")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        if ((account.getUsername() == null) || (account.getPassword().length() < 4)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(account);
        }else if (accountService.existsByUsername(account.getUsername())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(account);
        }else{
            Account addedAccount = accountService.save(account);
            return ResponseEntity.status(HttpStatus.OK).body(addedAccount);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account account) {
        Account verifiedAccount = accountService.loginAccount(account.getUsername(), account.getPassword());
        if (verifiedAccount != null) {
            return ResponseEntity.status(HttpStatus.OK).body(verifiedAccount);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(verifiedAccount);
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        if (message.getMessageText() != "" && message.getMessageText().length() < 255 && accountService.existsById(message.getPostedBy())) {
            Message newMessage = messageService.save(message);
            return ResponseEntity.status(HttpStatus.OK).body(newMessage);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessages() {
        List<Message> allMessages = messageService.getAllMessages();
        return ResponseEntity.status(HttpStatus.OK).body(allMessages);
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<Optional<Message>> getMessageById(@PathVariable long id) {
        Optional<Message> messageById = messageService.getMessageById(id);
        if (messageById.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(messageById);
        }else{
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Optional<Integer>> deleteMessageById(@PathVariable long id) {
        int messageById = messageService.deleteMessageById(id);
        if (messageById == 1){
            Optional<Integer> result = Optional.of(1);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }else{
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessagesByPostedBy(@PathVariable long account_id) {
        List<Message> messagesById = messageService.getMessagesByPostedBy(account_id);
        return ResponseEntity.status(HttpStatus.OK).body(messagesById);
    }

    @PatchMapping("messages/{message_id}")
    public ResponseEntity<Optional<Integer>> patchMessage(@RequestBody Message messageText, @PathVariable long message_id) {
        if (messageText.getMessageText().length() < 255 && messageText.getMessageText() != "" && accountService.existsById((int) message_id)){
            messageService.patchMessage(messageText.getMessageText(), message_id);
            Optional<Integer> result = Optional.of(1);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }else{
            return ResponseEntity.badRequest().build();
        }
    }
}
