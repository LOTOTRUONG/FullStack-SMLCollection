package fr.sml.unite.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.sml.unite.entity.UniteEntity;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.UriBuilder;
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
public class UniteDto extends HateOas {
    @JsonProperty(index = 1)
    private Integer idUnite;
    @JsonProperty(index = 2)
    private String nomUnite;

    public UniteDto(UniteEntity unite){
        idUnite = unite.getIdUnite();
        nomUnite = unite.getLibelleUnite();
    }

    public static List<UniteDto> toDtoList(List<UniteEntity> uniteEntities){
        List<UniteDto> uniteDTOList = new ArrayList<>();
        for (UniteEntity uniteEntity : uniteEntities){
            uniteDTOList.add(new UniteDto(uniteEntity));
        }
        return uniteDTOList;
    }

    public UniteDto(UniteEntity unite, UriBuilder uriBuilder) throws Exception {
        this.idUnite = unite.getIdUnite();
        this.nomUnite = unite.getLibelleUnite();
        // Add self link
        addLink("unit by id", HttpMethod.GET, new URI(uriBuilder.clone().path(unite.getIdUnite().toString()).build().toString()));
        addLink("all Units", HttpMethod.GET, new URI(uriBuilder.clone().build().toString()));

    }
}

