package fr.sml.objet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.sml.pays.dto.PaysDto;
import fr.sml.type_objet.dto.TypeObjetDto;
import fr.sml.objet.entity.ObjetEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ObjetDto {
    @JsonProperty(index = 1)
    private String login;
    @JsonProperty(index = 2)
    private Integer idObjet;
    @JsonProperty(index = 3)
    private String nomObjet;
    @JsonProperty(index = 4)
    private String typeObjet;
    @JsonProperty(index = 5)
    private String pays;

    public ObjetDto(ObjetEntity objet){
        login = objet.getUser().getLogin();
        idObjet = objet.getIdObjet();
        nomObjet = objet.getLibelleObjet();
        if (objet.getTypeObjet() != null){
            typeObjet = objet.getTypeObjet().getNomTypeObjet();

        }
        if (objet.getPays() != null){
            pays = objet.getPays().getLibellePays();
        }
    }


    public static List<ObjetDto> toDtoList(List<ObjetEntity> objetEntities){
        List<ObjetDto> objetDtoList = new ArrayList<>();
        for (ObjetEntity objetEntity : objetEntities){
            objetDtoList.add(new ObjetDto(objetEntity));
        }
        return objetDtoList;
    }

}
