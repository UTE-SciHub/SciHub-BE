package vn.thanhtuanle.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.thanhtuanle.common.enums.Gender;
import vn.thanhtuanle.common.enums.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_users")
public class User extends BaseEntity implements UserDetails {

    @Id
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    private String name;

    private String phoneNumber;

    private String imageUrl;

    private String imagePublicId;

    private LocalDateTime lastLogin;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate dob; // yyyy-MM-dd

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns=@JoinColumn(name="user", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="role", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TopicMember> topicMemberships = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
