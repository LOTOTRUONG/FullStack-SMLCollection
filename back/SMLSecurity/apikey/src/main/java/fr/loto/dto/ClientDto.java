package fr.loto.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.loto.entities.ClientEntity;
import fr.loto.entities.EmailEntity;
import jakarta.json.Json;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ClientDto {
    @JsonProperty(index = 1)
    private Integer idClient;
    @JsonProperty(index = 2)
    private String nomClient;
    @JsonProperty(index = 3)
    private String emailClient;
    @JsonProperty(index = 5)
    private Integer monthlyQuota;
    @JsonProperty(index = 4)
    private String apiKey;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ClientDtoEmail> listEmails;

    @JsonIgnore
    private boolean includeListEmails = false;

    public ClientDto(ClientEntity clientEntity, Boolean includeListEmails){
        idClient = clientEntity.getIdClient();
        emailClient = clientEntity.getEmailClient();
        nomClient = clientEntity.getNomClient();
        apiKey = clientEntity.getApiKey();
        monthlyQuota = clientEntity.getMonthlyQuota();
        if (includeListEmails){
            listEmails = new ArrayList<>();
            if (clientEntity != null && clientEntity.getEmailEntities() !=null){
                for (EmailEntity emailEntity : clientEntity.getEmailEntities()){
                    listEmails.add(new ClientDtoEmail(emailEntity.getIdMessage(), emailEntity.getSendTo(), emailEntity.getSubject()));
                }
            }
        }
    }

    public static List<ClientDto> toDtoList(List<ClientEntity> clientEntities){
        List<ClientDto> clientDtoList = new ArrayList<>();
        for (ClientEntity clientEntity : clientEntities){
            clientDtoList.add(new ClientDto(clientEntity, false));
        }
        return clientDtoList;
    }

    @Getter
    class ClientDtoEmail{
        Integer idMessage;
        String sendTo;
        String subject;
        public ClientDtoEmail(Integer id, String sendTo, String subject) {
            this.idMessage = id;
            this.sendTo = sendTo;
            this.subject = subject;
        }
    }

}
