package com.tms.finalproject_autoshop.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@type")
// Перечисляем все возможные подклассы
@JsonSubTypes({
        @JsonSubTypes.Type(value = ServiceParts.class, name = "servicePart"),
        @JsonSubTypes.Type(value = OilParts.class, name = "oilPart"),
        @JsonSubTypes.Type(value = SpareParts.class, name = "sparePart")
})
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@MappedSuperclass
@NoArgsConstructor
@Scope("prototype")
public abstract class ProductAbstract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String brand;
    private String article;
    private Double price;
    private String image;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;
}