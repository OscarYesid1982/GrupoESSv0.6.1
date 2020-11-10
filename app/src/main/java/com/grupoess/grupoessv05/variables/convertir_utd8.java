package com.grupoess.grupoessv05.variables;

public class convertir_utd8 {
    public String get_text(String texto){
        texto = texto.replace("Ã¡","á");
        texto = texto.replace("Ã³","ó");
        texto = texto.replace("Ã©","é");
        texto = texto.replace("Ã“","ó");

        return texto;
    }
}
