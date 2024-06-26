package fr.loto.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Getter
@Setter
@EqualsAndHashCode
public class Email {
    @Schema(description = "Email address of the recipient", required = true, example = "recipient@example.com")
    private String sendTo;
    private String subject;
    private String text;



}
