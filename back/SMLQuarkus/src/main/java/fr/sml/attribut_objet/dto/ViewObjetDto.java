package fr.sml.attribut_objet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.sml.attribut_objet.entity.ViewObjetEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class ViewObjetDto {
    @JsonProperty(index = 0)
    private String login;
    @JsonProperty(index = 1)
    private Integer idAttributType;
    @JsonProperty(index = 2)
    private String nomType;
    @JsonProperty(index = 3)
    private String nomObject;
    @JsonProperty(index = 4)
    private Float valeurFloat;
    @JsonProperty(index = 5)
    private String valeurString;
    @JsonProperty(index = 6)
    private String nomAttribut;
    @JsonProperty(index = 7)
    private String nomValue;
    @JsonProperty(index = 8)
    private String nomUnite;

    public ViewObjetDto(ViewObjetEntity viewObjectEntity){
        this.idAttributType = viewObjectEntity.getIdAttributType();
        this.login = viewObjectEntity.getLogin();
        this.nomType = viewObjectEntity.getLibelleType();
        this.nomObject = viewObjectEntity.getLibelleObjet();

        this.valeurFloat = viewObjectEntity.getValeurFloat();
        this.valeurString = viewObjectEntity.getValeurString();
        this.nomAttribut = viewObjectEntity.getLibelleAttribut();
        this.nomValue = viewObjectEntity.getLibelleValeur();
        this.nomUnite = viewObjectEntity.getLibelleUnite();

    }


    public static List<ViewObjetDto> toDtoList(List<ViewObjetEntity> viewObjectEntities){
        List<ViewObjetDto> viewObjectDtos = new ArrayList<>();
        for (ViewObjetEntity viewObjectEntity : viewObjectEntities){
            viewObjectDtos.add(new ViewObjetDto(viewObjectEntity));
        }
        return viewObjectDtos;
    }
}
