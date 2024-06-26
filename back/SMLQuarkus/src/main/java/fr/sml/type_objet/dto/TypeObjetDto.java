package fr.sml.type_objet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.sml.type_objet.entity.TypeObjetEntity;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.UriBuilder;
import lombok.*;
import vn.loto.HateOas;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TypeObjetDto extends HateOas {
    @JsonProperty(index = 1)
    private Integer idTypeObjet;
    @JsonProperty(index = 2)
    private String nomTypeObjet;

    public TypeObjetDto(TypeObjetEntity typeObjetEntity){
        this.idTypeObjet = typeObjetEntity.getIdType();
        this.nomTypeObjet = typeObjetEntity.getNomTypeObjet();
    }

    public static List<TypeObjetDto> toDtoList(List<TypeObjetEntity> typeObjetEntityList){
        List<TypeObjetDto> typeObjetDtoList = new ArrayList<>();
        for (TypeObjetEntity typeObjetEntity : typeObjetEntityList){
            typeObjetDtoList.add(new TypeObjetDto(typeObjetEntity));
        }
        return typeObjetDtoList;
    }

    public TypeObjetDto(TypeObjetEntity typeObjetEntity, UriBuilder uriBuilder) throws Exception {
        this.idTypeObjet = typeObjetEntity.getIdType();
        this.nomTypeObjet = typeObjetEntity.getNomTypeObjet();
        // Add links
        addLink("type object by id", HttpMethod.GET, new URI(uriBuilder.clone().path(typeObjetEntity.getIdType().toString()).build().toString()));
        addLink("all types object", HttpMethod.GET, new URI(uriBuilder.clone().build().toString()));
    }
}
