package me.jordy.rest.sample.accounts;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.useDefaultDateFormatsOnly;
import static org.junit.Assert.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

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
        accountRepository.save(account);

        // When
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Then
        assertThat(userDetails.getPassword()).isEqualTo(password);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findByUsernameFailBasedTestAnnotation() {
        String email ="!@#$%^&&*()QWERTYUIO@QWE.com";
        accountService.loadUserByUsername(email);
    }

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
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(email));

        // When
        accountService.loadUserByUsername(email);
    }


}
