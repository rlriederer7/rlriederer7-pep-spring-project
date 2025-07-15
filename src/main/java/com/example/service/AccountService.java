package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired private AccountRepository accountRepository;

    public Account save(Account account) {
        System.out.println("2");
        return accountRepository.save(account);
    }

    public boolean existsByUsername(String username){
        return accountRepository.existsByUsername(username);
    }

    public boolean existsById(int id){
        return accountRepository.existsByAccountId(id);
    }

    public Account loginAccount(String username, String password){
        return accountRepository.getByUsernameAndPassword(username, password);
    }
}
