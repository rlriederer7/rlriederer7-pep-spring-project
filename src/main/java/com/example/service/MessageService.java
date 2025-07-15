package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired private MessageRepository messageRepository;

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(long id) {
        int iid = (int) id;
        return messageRepository.findById(iid);
    }

    public int deleteMessageById(long id) {
        int iid = (int) id;
        if (messageRepository.findById(iid).isPresent()){
            messageRepository.deleteById(iid);
            return 1;
        }else{
            return 0;
        }
    }

    public List<Message> getMessagesByPostedBy(long id) {
        int iid = (int) id;
        return messageRepository.getMessagesByPostedBy(iid);
    }

    public Optional<Message> patchMessage(String messageText, long message_id) {
        Integer iid = (int) message_id;
        Optional<Message> messageOptional = messageRepository.findById(iid);
        Message message = messageOptional.get();
        message.setMessageText(messageText);
        return Optional.of(messageRepository.save(message));
    }
}
