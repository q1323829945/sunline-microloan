package cn.sunline.saas.channel.rbac.modules

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
        name = "user",
        indexes = [
            Index(name = "idx_user_username", columnList = "username", unique = true),
            Index(name = "idx_user_enabled", columnList = "enabled"),
            Index(name = "idx_user_account_expired", columnList = "accountExpired"),
            Index(name = "idx_user_credentials_expired", columnList = "credentialsExpired"),
            Index(name = "idx_user_account_locked", columnList = "accountLocked"),
            Index(name = "idx_user_created", columnList = "created"),
            Index(name = "idx_user_updated", columnList = "updated")
        ]
)
class User (
    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

    @Column(nullable = false, updatable = false, length = 64, columnDefinition = "varchar(64) not null")
        @NotNull
        private var username: String? = null,

    @Column(nullable = false, length = 128, columnDefinition = "varchar(128) not null")
        @NotNull
        private var password: String? = null,

    @Column(nullable = false, length = 128, columnDefinition = "varchar(128) not null")
        @NotNull
        var email: String,

    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
        @NotNull
        var jwtKey: String = UUID.randomUUID().toString(),

    @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "user_role_mapping",
                joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
        )
        @OrderBy(value = "name ASC")
        var roles: MutableList<Role> = mutableListOf(),

    @Column(nullable = false, columnDefinition = "tinyint(1) default true")
        @NotNull
        var enabled: Boolean = true,

    @Column(nullable = false, columnDefinition = "tinyint(1) default false")
        @NotNull
        var accountExpired: Boolean = false,

    @Column(nullable = false, columnDefinition = "tinyint(1) default false")
        @NotNull
        var credentialsExpired: Boolean = false,

    @Column(nullable = false, columnDefinition = "tinyint(1) default false")
        @NotNull
        var accountLocked: Boolean = false,

    @Column(name="person_id", columnDefinition = "bigint null")
        var personId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "position_id")
        @JsonIgnore
        var position: Position? = null,

    @CreationTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        var created: Date? = null,

    @UpdateTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        var updated: Date? = null
): UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val roles = roles.mapNotNull { it.permissions }
        val permissions = roles.flatMap { it.mapNotNull { p -> p.name } }
        return permissions.map { SimpleGrantedAuthority(it) }.toMutableList()
    }

    fun setUsername(newUsername: String) {
        this.username = newUsername
    }

    fun setPassword(newPassword: String) {
        this.password = newPassword
    }

    override fun getPassword(): String {
        return password!!
    }

    override fun getUsername(): String {
        return username!!
    }

    override fun isAccountNonExpired(): Boolean {
        return !accountExpired
    }

    override fun isAccountNonLocked(): Boolean {
        return !accountLocked
    }

    override fun isCredentialsNonExpired(): Boolean {
        return !credentialsExpired
    }

    override fun isEnabled(): Boolean {
        return enabled
    }
}