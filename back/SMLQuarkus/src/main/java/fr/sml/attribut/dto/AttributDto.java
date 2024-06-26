package fr.sml.attribut.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.sml.attribut.entity.AttributEntity;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.UriBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.loto.HateOas;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttributDto extends HateOas {
    @JsonProperty(index = 1)
    private Integer idAttribut;
    @JsonProperty(index = 2)
    private String nomAttribut;

    public AttributDto(AttributEntity attribut){
        idAttribut = attribut.getIdAttribut();
        nomAttribut = attribut.getLibelleAttribut();
    }

    public static List<AttributDto> toDtoList(List<AttributEntity> attributEntities) {
        List<AttributDto> attributDTOList = new ArrayList<>();
        for (AttributEntity attributEntity : attributEntities) {
            attributDTOList.add(new AttributDto(attributEntity));
        }
        return attributDTOList;
    }

    public AttributDto(AttributEntity attribut, UriBuilder uriBuilder) throws Exception{
        this.idAttribut = attribut.getIdAttribut();
        this.nomAttribut = attribut.getLibelleAttribut();
        // Add self link
        addLink("attribut by id", HttpMethod.GET, new URI(uriBuilder.clone().path(attribut.getIdAttribut().toString()).build().toString()));
        addLink("allAttributs", HttpMethod.GET, new URI(uriBuilder.clone().build().toString()));
    }
}
