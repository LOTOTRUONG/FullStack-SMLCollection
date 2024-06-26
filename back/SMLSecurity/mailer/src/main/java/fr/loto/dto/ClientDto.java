package fr.loto.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.loto.entities.ClientEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class ClientDto {
    @JsonProperty(index = 1)
    private String emailClient;
    @JsonIgnore // Exclude apiKey field from being serialized
    private String apiKey;
    @JsonProperty(index = 2)
    private String nomClient;
    @JsonProperty(index = 3)
    private Integer monthlyQuota;

    public ClientDto(ClientEntity clientEntity){
        emailClient = clientEntity.getEmailClient();
        nomClient = clientEntity.getNomClient();
        apiKey = clientEntity.getApiKey();
        monthlyQuota = clientEntity.getMonthlyQuota();
    }

    @JsonProperty(value = "apiKey", access = JsonProperty.Access.READ_ONLY)
    public String getApiKey() {
        return apiKey;
    }
    public static List<ClientDto> toDtoList(List<ClientEntity> clientEntities){
        List<ClientDto> clientDtoList = new ArrayList<>();
        for (ClientEntity clientEntity : clientEntities){
            clientDtoList.add(new ClientDto(clientEntity));
        }
        return clientDtoList;
    }
 }
