package com.example.sthephan.laboratorio_3_cifrado;

public class Permutaciones {

    public String Permutacion10(String binario){
        String binarioReordenado = "";
        char[]  Entrada = binario.toCharArray();
        char[]  Permutacion = {0, 2, 1, 5, 4, 6, 9, 8, 3, 7}; //determina el orden de la permutacion
        char[] Salida = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < 10; i++){
            Salida[i] = Entrada[Permutacion[i]];
        }
        for (int i = 0; i < 10; i++){
            binarioReordenado += Salida[i];
        }
        return binarioReordenado;
    }

    public String Permutacion8(String binario){
        String binarioReordenado = "";
        char[] Entrada = binario.toCharArray();
        char[] Permutacion = {9, 3, 5, 1, 0, 4, 8, 7, 2, 6}; //determina el orden de la permutacion
        char[] Salida = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < 10; i++){
            Salida[i] = Entrada[Permutacion[i]];
        }
        for (int i = 0; i < 10; i++) {
            binarioReordenado += Salida[i];
        }
        return binarioReordenado.substring(0, binarioReordenado.length() - 2); //convierte el tamaño de la cadena a 8 (selecionar)
    }

    public String PermutacionInicial(String binario){
        String binarioReordenado = "";
        char[] Entrada = binario.toCharArray();
        char[] Permutacion = {7, 6, 5, 4, 3, 1, 2, 0};//determina el orden de la permutacion
        char[] Salida = {0, 0, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < 8; i++){
            Salida[i] = Entrada[Permutacion[i]];
        }
        for (int i = 0; i < 8; i++){
            binarioReordenado += Salida[i];
        }
        return binarioReordenado;
    }

    public String ExpandiryPermutar(String binario){
        String binarioReordenado = "";
        char[] Entrada = binario.toCharArray();
        char[] Permutacion = {3, 2, 1, 0, 3, 1, 0, 2};//determina el orden de la permutacion
        char[] Salida = {0, 0, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < 8; i++){
            Salida[i] = Entrada[Permutacion[i]];
        }
        for (int i = 0; i < 8; i++){
            binarioReordenado += Salida[i];
        }
        return binarioReordenado;
    }

    public String Permutacion4(String binario){
        String binarioReordenado = "";
        char[] Entrada = binario.toCharArray();
        char[] Permutacion = {0, 3, 1, 2};//determina el orden de la permutacion
        char[] Salida = {0, 0, 0, 0};
        for (int i = 0; i < 4; i++){
            Salida[i] = Entrada[Permutacion[i]];
        }
        for (int i = 0; i < 4; i++){
            binarioReordenado += Salida[i];
        }
        return binarioReordenado;
    }
}
