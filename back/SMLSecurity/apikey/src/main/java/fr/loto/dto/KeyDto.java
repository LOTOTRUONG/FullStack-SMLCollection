package fr.loto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.loto.entities.ClientEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KeyDto {
    @JsonProperty(index = 1)
    private Integer idClient;
    @JsonProperty(index = 2)
    private String apiKey;
    @JsonProperty(index = 3)
    private Integer monthlyQuota;


    public KeyDto(ClientEntity clientEntity){
        idClient = clientEntity.getIdClient();
        apiKey = clientEntity.getApiKey();
        monthlyQuota = clientEntity.getMonthlyQuota();
    }
}
