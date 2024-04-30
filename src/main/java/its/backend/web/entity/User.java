package its.backend.web.entity;

import its.backend.global.config.BaseEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@DynamicInsert
@Table(name = "USER")
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserSeq;

    @Column(name = "USER_ID", nullable = false, length = 100)
    private String userId;

    @Column(name = "PASSWORD", nullable = false, length = 100)
    private String password;

    @Column(name = "USER_NAME", nullable = false, length = 100)
    private String userName;

    @Column(name = "USER_NICKNAME", nullable = false, length = 100)
    private String userNick;

    @Column(name = "BIRTH", nullable = false, length = 45)
    private String birth;

    @Column(name = "PHONE", nullable = false, length = 45)
    private String phone;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private Role role;

    @Column(name = "FIRST_LOGIN", nullable = false)
    @ColumnDefault("1")
    private int firstLogin;

    @Column(name = "SERVICE_CHECK", nullable = false)
    @ColumnDefault("0")
    private int serviceCheck;

    @Column(name = "MENU_REGISTER", nullable = false)
    @ColumnDefault("0")
    private int menu_register;

    @Column(name = "PERSONAL_CHECK", nullable = false)
    @ColumnDefault("0")
    private int personalCheck;

    @Column(name = "SMS_CHECK", nullable = false)
    @ColumnDefault("0")
    private int smsCheck;

    @Column(name = "EMAIL_CHECK", nullable = false)
    @ColumnDefault("0")
    private int email_check;

    @Column(name = "CALL_CHECK", nullable = false)
    @ColumnDefault("0")
    private int callCheck;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getKey());
        return Collections.singletonList(authority);
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
