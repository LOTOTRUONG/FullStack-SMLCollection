package fr.loto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailRecord {
    @Schema(description = "Email address of the recipient", required = true, example = "recipient@gmail.com")
    private String sendTo;
    private String subject;

}
