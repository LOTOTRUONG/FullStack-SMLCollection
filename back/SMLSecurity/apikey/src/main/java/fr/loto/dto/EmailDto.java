package fr.loto.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.loto.entities.ClientEntity;
import fr.loto.entities.EmailEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class EmailDto {
    @JsonProperty(index = 1)
    private Integer idMessage;
    @JsonProperty(index = 2)
    private String subjectMessage;
    @JsonProperty(index = 3)
    private String sendTo;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime sendAt;
    @JsonProperty(index = 5)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ClientDto client;

    public EmailDto(EmailEntity mailEntity){
        idMessage = mailEntity.getIdMessage();
        subjectMessage = mailEntity.getSubject();
        sendTo = mailEntity.getSendTo();
        sendAt = mailEntity.getSendAt();
        if (mailEntity.getClient() != null){
            client = new ClientDto();
            client.setNomClient(mailEntity.getClient().getNomClient());
            client.setEmailClient(mailEntity.getClient().getEmailClient());
        }
    }

    public static List<EmailDto> toDtoList(List<EmailEntity> emailEntities){
        List<EmailDto> emailDtoList = new ArrayList<>();
        for (EmailEntity emailEntity : emailEntities){
            emailDtoList.add(new EmailDto(emailEntity));
        }
        return emailDtoList;
    }
}
