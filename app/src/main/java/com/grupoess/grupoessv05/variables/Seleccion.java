package com.grupoess.grupoessv05.variables;

public class Seleccion {
    private static int id_categoria;
    private static int id_producto;
    private static boolean id_validar_pestana = true;

    public void set_id_categoria(int v_id){
        id_categoria = v_id;
    }

    public int get_id_categoria(){
        return id_categoria;
    }


    public void set_id_producto(int v_id){
        id_producto = v_id;
    }

    public int get_id_producto(){
        return id_producto;
    }


    public void set_id_validar_pestana(boolean v_id_validar_pestana){id_validar_pestana = v_id_validar_pestana;}

    public boolean get_id_validar_pestana(){
        return id_validar_pestana;
    }
}
