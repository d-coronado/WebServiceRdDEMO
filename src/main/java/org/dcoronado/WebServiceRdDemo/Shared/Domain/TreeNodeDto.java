package org.dcoronado.WebServiceRdDemo.Shared.Domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TreeNodeDto {

    private final String nombre;
    private final List<TreeNodeDto> hijos = new ArrayList<>();

    public TreeNodeDto(String nombre) {
        this.nombre = nombre;
    }

    public void agregarHijo(TreeNodeDto hijo) {
        this.hijos.add(hijo);
    }

}
