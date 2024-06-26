package fr.sml.valeur.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.sml.valeur.entity.ValeurEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValeurDto {
    @JsonProperty(index = 1)
    private Integer idValeur;
    @JsonProperty(index = 2)
    private String nomValeur;
    @JsonProperty(index = 3)
    private Integer idType;
    @JsonProperty(index = 4)
    private Integer idAttributType;


    public ValeurDto(ValeurEntity valeur){
        idValeur = valeur.getIdValeur();
        nomValeur = valeur.getLibelleValeur();
        idAttributType = valeur.getAttributType().getIdAttributType();
        idType = valeur.getAttributType().getTypeObjet().getIdType();

        }

    public static List<ValeurDto> toDtoList(List< ValeurEntity > valeurEntities){
        List<ValeurDto> valeurDtoList = new ArrayList<>();
        for (ValeurEntity valeurEntity : valeurEntities){
            if (valeurEntity.getLibelleValeur() == null || valeurEntity.getLibelleValeur().isEmpty())
                valeurEntity.setLibelleValeur("null");
                 valeurDtoList.add(new ValeurDto(valeurEntity));
        }
            return valeurDtoList;
    }
}
