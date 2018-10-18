package com.example.sthephan.laboratorio_3_cifrado;

public class Permutaciones {
    public String Permutacion10(String binario){
        String binarioReordenado = "";
        char[]  Entrada = binario.toCharArray();
        char[]  Permutacion = {2, 4, 1, 6, 3, 9, 0, 8, 7, 5}; //determina el orden de la permutacion
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
        char[] Permutacion = {5, 2, 6, 3, 7, 4, 9, 8, 0, 1}; //determina el orden de la permutacion
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
        char[] Permutacion = {1, 5, 2, 0, 3, 7, 4, 6};//determina el orden de la permutacion
        char[] Salida = {0, 0, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < 8; i++){
            Salida[i] = Entrada[Permutacion[i]];
        }
        for (int i = 0; i < 8; i++){
            binarioReordenado += Salida[i];
        }
        return binarioReordenado;
    }

    public String PermutacionInversa(String binario){
        String binarioReordenado = "";
        char[] Entrada = binario.toCharArray();
        char[] Permutacion = {3, 0, 2, 4, 6, 1, 7, 5};
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
        char[] Permutacion = {3, 0, 1, 2, 1, 2, 3, 0};//determina el orden de la permutacion
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
        char[] Permutacion = {1, 3, 2, 0};//determina el orden de la permutacion
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
