package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class UserRegistrationDto {

    @NotNull(message = "Email must not be null")
    @Email
    private String username;

    @NotNull(message = "Password must not be null")
    private String password;

    @Email
    @NotNull(message = "Email must not be null")
    private String email;

    public UserRegistrationDto() {
    }

    public UserRegistrationDto(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserRegistrationDto)) {
            return false;
        }
        UserRegistrationDto userLoginDto = (UserRegistrationDto) o;
        return Objects.equals(username, userLoginDto.username)
            && Objects.equals(password, userLoginDto.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return "UserLoginDto{"
            + "username='" + username + '\''
            + ", password='" + password + '\''
            + '}';
    }


    public static final class UserLoginDtoBuilder {
        private String username;
        private String password;

        private UserLoginDtoBuilder() {
        }

        public static UserLoginDtoBuilder anUserLoginDto() {
            return new UserLoginDtoBuilder();
        }

        public UserLoginDtoBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public UserLoginDtoBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserRegistrationDto build() {
            UserRegistrationDto userLoginDto = new UserRegistrationDto();
            userLoginDto.setUsername(username);
            userLoginDto.setPassword(password);
            return userLoginDto;
        }
    }
}
