package fr.loto.dto;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Getter
@Setter
public class Email {
    @Schema(description = "Email address of the recipient", required = true, example = "recipient@example.com")
    private String sendTo;
    private String subject;
    private String text;

    public Email(String sendTo, String subject, String text) {
        this.sendTo = sendTo;
        this.subject = subject;
        this.text = text;
    }
}
