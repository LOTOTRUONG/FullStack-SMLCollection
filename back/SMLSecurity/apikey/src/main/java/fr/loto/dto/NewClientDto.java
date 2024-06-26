package fr.loto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.loto.entities.ClientEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewClientDto {
    @JsonProperty(index = 1)
    private String emailClient;
    @JsonProperty(index = 2)
    private String nomClient;
    @JsonProperty(index = 3)
    private Integer monthlyQuota;

    public NewClientDto(ClientEntity clientEntity){
        this.emailClient = clientEntity.getEmailClient();
        this.nomClient = clientEntity.getNomClient();
        this.monthlyQuota = clientEntity.getMonthlyQuota();
    }
}
