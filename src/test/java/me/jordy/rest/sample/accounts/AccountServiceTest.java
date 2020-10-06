package me.jordy.rest.sample.accounts;

import me.jordy.rest.sample.common.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AccountServiceTest extends BaseTest {

    /**
     * Junit 5 버전 업으로 주석처리,
     * @Rule 어노태이션 삭제됨
     */
//    @Rule
//    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void findByUsername () {
        // Given
        String email = "jordy-torvalds@kakao.com";
        String password = "1234";
        Account account = Account.builder()
                .email(email)
                .password(password)
                .roles(new HashSet<AccountRole>(Arrays.asList(AccountRole.ADMIN, AccountRole.USER)))
                .build();
        accountService.saveAccount(account);

        // When
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Then
        assertThat(passwordEncoder.matches(password,account.getPassword())).isTrue();
    }

    /* JUnit 5 버전업으로 주석처리*/
    /*
    @Test(expected = UsernameNotFoundException.class)
    public void findByUsernameFailBasedTestAnnotation() {
        String email ="!@#$%^&&*()QWERTYUIO@QWE.com";
        accountService.loadUserByUsername(email);
    }

    */

    @Test
    public void findByUsernameFailBasedTryCatch() {
        String email ="!@#$%^&&*()QWERTYUIO@QWE.com";
        try {
            accountService.loadUserByUsername(email);
            fail("supposedToBeFailed");
        } catch( UsernameNotFoundException e) {
            assertThat(e.getMessage()).contains(email);
        }
    }

    @Test
    public void findByUsernameFailBasedRuleAnnotation() {
        // Expected
        /* 예상되는 예외와 메시지는 항상 When 절 보다 앞에 있어야 함*/
        String email ="!@#$%^&&*()QWERTYUIO@QWE.com";

        /**
         * Junit 5 버전 업으로 주석처리,
         * @Rule 어노태이션 삭제됨
         */
//        expectedException.expect(UsernameNotFoundException.class);
//        expectedException.expectMessage(Matchers.containsString(email));

        // When
        assertThrows(UsernameNotFoundException.class, ()->{
            accountService.loadUserByUsername(email);
        });
    }
}
