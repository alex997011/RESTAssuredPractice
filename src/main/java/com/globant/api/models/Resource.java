package com.globant.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Resource {

    private String name;
    private String trademark;
    private Integer stock;
    private Number price;
    private String description;
    private String tags;
    private Boolean active;
    private String Id;
}





