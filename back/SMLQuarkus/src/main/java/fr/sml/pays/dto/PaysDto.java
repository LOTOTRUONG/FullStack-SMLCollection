package fr.sml.pays.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.sml.pays.entity.PaysEntity;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.UriBuilder;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import vn.loto.HateOas;


import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PaysDto extends HateOas {
    @JsonProperty(index = 1)
    private Integer idPays;
    @JsonProperty(index = 2)
    @Schema(name = "nomPays", description = "Nom du pays", required = true, example = "FRANCE")
    private String nomPays;


    public PaysDto(){
    }

    public PaysDto(PaysEntity pays){
        idPays = pays.getIdPays();
        nomPays = pays.getLibellePays();
    }
    public static List<PaysDto> toDtoList(List<PaysEntity> paysEntities){
        List<PaysDto> paysDTOList = new ArrayList<>();
        for (PaysEntity paysEntity : paysEntities){
            paysDTOList.add(new PaysDto(paysEntity));
        }
        return paysDTOList;
    }

    public PaysDto(PaysEntity pays, UriBuilder uriBuilder) throws Exception {
        this.idPays = pays.getIdPays();
        this.nomPays = pays.getLibellePays();
        // Add self link
        addLink("pays by id",HttpMethod.GET, new URI(uriBuilder.clone().path(pays.getIdPays().toString()).build().toString()));
        addLink("all Pay",HttpMethod.GET, new URI(uriBuilder.clone().build().toString()));
    }
}
