package com.example.sthephan.laboratorio_3_cifrado;

public class LlavesSDES {

    public String k1 = "";
    public String k2 = "";

    Permutaciones p = new Permutaciones();

    public String leftShift1(String binario){
        String binarioDesplazado = "";
        binarioDesplazado += binario.substring(1, binario.length());
        binarioDesplazado += binario.substring(0, 1);
        return binarioDesplazado;
    }

    public String leftShift2(String binario){
        String binarioDesplazado = "";
        binarioDesplazado += binario.substring(2, binario.length());
        binarioDesplazado += binario.substring(0, 2);
        return binarioDesplazado;
    }

    public void generarLlaves(String binario){
        String paso1 = p.Permutacion10(binario);
        String paso1a = paso1.substring(0, paso1.length()/2);
        String paso1b = paso1.substring(paso1.length()/2, paso1.length());
        String paso2a = leftShift1(paso1a);
        String paso2b = leftShift1(paso1b);
        k1 = p.Permutacion8(paso2a + paso2b);
        String paso3a = leftShift2(paso2a);
        String paso3b = leftShift2(paso2b);
        k2 = p.Permutacion8(paso3a + paso3b);
    }
}
