package com.project.osc.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Campaign {

    private Integer id;
    private String name;
    private Integer campaignType;
    private String description;
}
