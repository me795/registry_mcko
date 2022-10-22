package ru.mcko.registry.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Role {

    private static final long serialVersionUID = -8969122108362886672L;
    private Integer id;
    private String name;
}
