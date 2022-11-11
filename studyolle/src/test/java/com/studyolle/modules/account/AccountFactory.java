package com.studyolle.modules.account;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountFactory {
    @Autowired AccountRepository accountRepository;

    public Account createAccount(String nickname) {
        Account brucehan = new Account();
        brucehan.setNickname(nickname);
        brucehan.setEmail(nickname + "@induk.ac.kr");
        accountRepository.save(brucehan);
        return brucehan;
    }
}
