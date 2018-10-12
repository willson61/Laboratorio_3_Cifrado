package com.example.sthephan.laboratorio_3_cifrado;

import java.util.HashMap;

public class MetodosSDES {

    public HashMap<String, String> S0 = new HashMap<>();
    public HashMap<String, String> S1 = new HashMap<>();

    public void setearSBoxes(){
        S0.put("0000", "01");
        S0.put("0001", "00");
        S0.put("0010", "11");
        S0.put("0011", "10");
        S0.put("0100", "11");
        S0.put("0101", "10");
        S0.put("0110", "01");
        S0.put("0111", "00");
        S0.put("1000", "00");
        S0.put("1001", "10");
        S0.put("1010", "01");
        S0.put("1011", "11");
        S0.put("1100", "11");
        S0.put("1101", "01");
        S0.put("1110", "11");
        S0.put("1111", "10");
        S1.put("0000", "00");
        S1.put("0001", "01");
        S1.put("0010", "10");
        S1.put("0011", "11");
        S1.put("0100", "10");
        S1.put("0101", "00");
        S1.put("0110", "01");
        S1.put("0111", "11");
        S1.put("1000", "11");
        S1.put("1001", "00");
        S1.put("1010", "01");
        S1.put("1011", "00");
        S1.put("1100", "10");
        S1.put("1101", "01");
        S1.put("1110", "00");
        S1.put("1111", "11");
    }

    public String devolverValorS0(String binario){
        String binarioNuevo = "";
        char[] indice = binario.toCharArray();
        binarioNuevo += indice[0];
        binarioNuevo += indice[3];
        binarioNuevo += indice[1];
        binarioNuevo += indice[2];
        return binarioNuevo;
    }

    public String devolverValorS1(String binario){
        String binarioNuevo = "";
        char[] indice = binario.toCharArray();
        binarioNuevo += indice[0];
        binarioNuevo += indice[3];
        binarioNuevo += indice[1];
        binarioNuevo += indice[2];
        return binarioNuevo;
    }
}
