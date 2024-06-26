package fr.loto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EmailDto {
    @JsonProperty(index = 1)
    private Integer idMessage;
    @JsonProperty(index = 2)
    private String subjectMessage;
    @JsonProperty(index = 3)
    private String sendTo;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime sendAt;


    public EmailDto(){
    }
}
