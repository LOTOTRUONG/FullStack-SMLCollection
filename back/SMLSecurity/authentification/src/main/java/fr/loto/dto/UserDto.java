package fr.loto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.loto.entities.UserEntity;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.UriBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import vn.loto.HateOas;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserDto extends HateOas {
    @JsonProperty(index = 1)
    private String login;
    @JsonProperty(index = 2)
    private String email;
    @JsonProperty(index = 3)
    private String role;
    @JsonProperty(index = 4)
    private Boolean status;
    @JsonProperty(index = 5)
    private String connectionDate;

    public UserDto(UserEntity userEntity){
        login = userEntity.getLogin();
        email = userEntity.getEmail();
        role = userEntity.getRoleUser();
        status = userEntity.getStatus();
        connectionDate = userEntity.getConnectionDate().toString();
    }


    public UserDto(UserEntity userEntity, UriBuilder uriBuilder, boolean isAdmin) throws Exception {
        this.login = userEntity.getLogin();
        this.email = userEntity.getEmail();
        this.role = userEntity.getRoleUser();
        this.status = userEntity.getStatus();
        if (userEntity.getConnectionDate() != null){
            this.connectionDate = userEntity.getConnectionDate().toString();
        }
        addLink("user by login", HttpMethod.GET, new URI(uriBuilder.clone().path("users").path(userEntity.getLogin()).build().toString()));
        addLink("forgot login",HttpMethod.POST, new URI(uriBuilder.clone().path("users").path(userEntity.getEmail()).path("request_login").build().toString()));
        addLink("reset password",HttpMethod.POST, new URI(uriBuilder.clone().path("users").path(userEntity.getLogin()).path("request_password").build().toString()));
        addLink("reset email",HttpMethod.POST, new URI(uriBuilder.clone().path("users").path(userEntity.getLogin()).path("request_email").build().toString()));
        if (isAdmin) {
            addLink("all users",HttpMethod.GET, new URI(uriBuilder.clone().path("admin").path("allUsers").build().toString()));
            addLink("deactivate account",HttpMethod.POST, new URI(uriBuilder.clone().path("admin").path(userEntity.getLogin()).path("deactivate").build().toString()));
            addLink("re-activate account",HttpMethod.POST, new URI(uriBuilder.clone().path("admin").path(userEntity.getLogin()).path("reactivate").build().toString()));
            addLink("update role",HttpMethod.PUT, new URI(uriBuilder.clone().path("admin").path(userEntity.getLogin()).path("updateRole").build().toString()));

        }
    }





    public static List<UserDto> toDtoList(List<UserEntity> userEntities, UriBuilder uriBuilder, boolean isAdmin) throws Exception {
        List<UserDto> userDtoList = new ArrayList<>();
        for (UserEntity userEntity : userEntities){
            userDtoList.add(new UserDto(userEntity, uriBuilder, isAdmin));
        }
        return userDtoList;
    }

}
