package fr.sml.attribut_type.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.sml.attribut_type.entity.AttributTypeEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AttributTypeDto {
    @JsonProperty(index = 1)
    private Integer idAttributType;
    @JsonProperty(index = 2)
    private Integer idType;
    @JsonProperty(index = 3)
    private boolean commentaire;
    @JsonProperty(index = 4)
    private boolean table_data;
    @JsonProperty(index = 5)
    private Integer idUnite;
    @JsonProperty(index = 6)
    private Integer idAttribut;

    public AttributTypeDto(AttributTypeEntity attributType){

            this.idType = attributType.getTypeObjet().getIdType();
            if (attributType.getUnite()!= null) {
                this.idUnite = attributType.getUnite().getIdUnite();
            }
            if (attributType.getAttribut()!= null) {
                this.idAttribut = attributType.getAttribut().getIdAttribut();
            }

        this.idAttributType = attributType.getIdAttributType();
        this.commentaire = attributType.isCommentaire();
        this.table_data = attributType.isTableDeDonnee();
    }

    public static List<AttributTypeDto> toDtoList(List<AttributTypeEntity> attributTypeEntities){
        List<AttributTypeDto> attributTypeDtoList = new ArrayList<>();
        for (AttributTypeEntity attributTypeEntity : attributTypeEntities){
            attributTypeDtoList.add(new AttributTypeDto(attributTypeEntity));
        }
        return attributTypeDtoList;
    }
}
