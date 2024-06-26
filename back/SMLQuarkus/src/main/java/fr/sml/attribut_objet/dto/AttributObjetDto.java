package fr.sml.attribut_objet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.sml.attribut_objet.entity.ViewObjetEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AttributObjetDto {
    @JsonProperty(index = 0)
    private String login;
    @JsonProperty(index = 1)
    private Integer idType;
    @JsonProperty(index = 2)
    private String nomTypeObject;
    @JsonProperty(index = 3)
    private String nomObject;
    @JsonProperty(index = 4)
    private Float valeurFloat;
    @JsonProperty(index = 5)
    private String valeurString;
    @JsonProperty(index = 6)
    private String nomValue;
    @JsonProperty(index = 7)
    private String nomUnite;

    public AttributObjetDto(ViewObjetEntity viewObjectEntity){
            this.login = viewObjectEntity.getLogin();
            this.idType = viewObjectEntity.getIdType();
            this.nomTypeObject = viewObjectEntity.getLibelleType();
            this.nomObject = viewObjectEntity.getLibelleObjet();

        this.valeurFloat = viewObjectEntity.getValeurFloat();
        this.valeurString = viewObjectEntity.getValeurString();
        this.nomValue = viewObjectEntity.getLibelleValeur();
        this.nomUnite = viewObjectEntity.getLibelleUnite();

    }


    public static List<AttributObjetDto> toDtoList(List<ViewObjetEntity> viewObjectEntities){
        List<AttributObjetDto> attributObjetDtoList = new ArrayList<>();
        for (ViewObjetEntity viewObjectEntity : viewObjectEntities){
            attributObjetDtoList.add(new AttributObjetDto(viewObjectEntity));
        }
        return attributObjetDtoList;
    }

}
