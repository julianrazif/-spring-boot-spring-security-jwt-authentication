package id.julianraziffigaro.demo.sbssja.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

public class User implements UserDetails {

  @Serial
  private static final long serialVersionUID = -8358806941698382067L;

  private final String password;
  private final String username;
  private final Set<GrantedAuthority> authorities;
  private final boolean accountNonExpired;
  private final boolean accountNonLocked;
  private final boolean credentialsNonExpired;
  private final boolean enabled;

  public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
    this(username, password, true, true, true, true, authorities);
  }

  public User(String username, String password, boolean enabled, boolean accountNonExpired,
              boolean credentialsNonExpired, boolean accountNonLocked,
              Collection<? extends GrantedAuthority> authorities) {
    Assert.isTrue(username != null && !"".equals(username) && password != null,
      "Cannot pass null or empty values to constructor");
    this.username = username;
    this.password = password;
    this.enabled = enabled;
    this.accountNonExpired = accountNonExpired;
    this.credentialsNonExpired = credentialsNonExpired;
    this.accountNonLocked = accountNonLocked;
    this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
  }

  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

  @Override
  public boolean isAccountNonExpired() {
    return this.accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return this.accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return this.credentialsNonExpired;
  }

  private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
    Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
    // Ensure array iteration order is predictable (as per
    // UserDetails.getAuthorities() contract and SEC-717)
    SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new AuthorityComparator());
    for (GrantedAuthority grantedAuthority : authorities) {
      Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
      sortedAuthorities.add(grantedAuthority);
    }
    return sortedAuthorities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    var user = (User) o;
    return username.equals(user.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username);
  }

  @Override
  public String toString() {
    return "User{" +
      "password='" + password + '\'' +
      ", username='" + username + '\'' +
      ", authorities=" + authorities +
      ", accountNonExpired=" + accountNonExpired +
      ", accountNonLocked=" + accountNonLocked +
      ", credentialsNonExpired=" + credentialsNonExpired +
      ", enabled=" + enabled +
      '}';
  }

  public static UserBuilder withUsername(String username) {
    return builder().username(username);
  }

  public static UserBuilder builder() {
    return new UserBuilder();
  }

  public static UserBuilder withUserDetails(UserDetails userDetails) {
    return withUsername(userDetails.getUsername())
      .password(userDetails.getPassword())
      .accountExpired(!userDetails.isAccountNonExpired())
      .accountLocked(!userDetails.isAccountNonLocked())
      .authorities(userDetails.getAuthorities())
      .credentialsExpired(!userDetails.isCredentialsNonExpired())
      .disabled(!userDetails.isEnabled());
  }

  private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    @Override
    public int compare(GrantedAuthority g1, GrantedAuthority g2) {
      // Neither should ever be null as each entry is checked before adding it to
      // the set. If the authority is null, it is a custom authority and should
      // precede others.
      if (g2.getAuthority() == null) {
        return -1;
      }
      if (g1.getAuthority() == null) {
        return 1;
      }
      return g1.getAuthority().compareTo(g2.getAuthority());
    }
  }

  public static final class UserBuilder {

    private String username;
    private String password;
    private List<GrantedAuthority> authorities;
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean credentialsExpired;
    private boolean disabled;
    private Function<String, String> passwordEncoder = (password) -> password;

    private UserBuilder() {
    }

    public UserBuilder username(String username) {
      Assert.notNull(username, "username cannot be null");
      this.username = username;
      return this;
    }

    public UserBuilder password(String password) {
      Assert.notNull(password, "password cannot be null");
      this.password = password;
      return this;
    }

    public UserBuilder passwordEncoder(Function<String, String> encoder) {
      Assert.notNull(encoder, "encoder cannot be null");
      this.passwordEncoder = encoder;
      return this;
    }

    public UserBuilder roles(String... roles) {
      List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
      for (String role : roles) {
        Assert.isTrue(!role.startsWith("ROLE_"),
          () -> role + " cannot start with ROLE_ (it is automatically added)");
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
      }
      return authorities(authorities);
    }

    public UserBuilder authorities(GrantedAuthority... authorities) {
      return authorities(Arrays.asList(authorities));
    }

    public UserBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
      this.authorities = new ArrayList<>(authorities);
      return this;
    }

    public UserBuilder authorities(String... authorities) {
      return authorities(AuthorityUtils.createAuthorityList(authorities));
    }

    public UserBuilder accountExpired(boolean accountExpired) {
      this.accountExpired = accountExpired;
      return this;
    }

    public UserBuilder accountLocked(boolean accountLocked) {
      this.accountLocked = accountLocked;
      return this;
    }

    public UserBuilder credentialsExpired(boolean credentialsExpired) {
      this.credentialsExpired = credentialsExpired;
      return this;
    }

    public UserBuilder disabled(boolean disabled) {
      this.disabled = disabled;
      return this;
    }

    public UserDetails build() {
      String encodedPassword = this.passwordEncoder.apply(this.password);
      return new User(this.username, encodedPassword, !this.disabled, !this.accountExpired,
        !this.credentialsExpired, !this.accountLocked, this.authorities);
    }
  }
}
