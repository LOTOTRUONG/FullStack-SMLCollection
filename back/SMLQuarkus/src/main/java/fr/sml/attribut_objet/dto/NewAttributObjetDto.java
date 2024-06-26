package fr.sml.attribut_objet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewAttributObjetDto {
    @JsonProperty(index = 0)
    private String nomObject;
    @JsonProperty(index = 1)
    private Integer idAttributType;
    @JsonProperty(index = 2)
    private Float valeurFloat;
    @JsonProperty(index = 3)
    private String valeurString;
    @JsonProperty(index = 4)
    private String nomValeur;
}
