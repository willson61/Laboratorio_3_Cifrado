package com.example.sthephan.laboratorio_3_cifrado;

import java.util.ArrayList;

public class ZigZag {

    public ArrayList<ArrayList<Character>> Panel = new ArrayList<>();

    public String cifrado(String nivel, String texto){
        int N = Integer.parseInt(nivel);
        int longitud = texto.length();
        int tamanoOlas = (N*2) - 2;
        int numeroOlas = 0;
        String TextoCifrado = "";
        if (longitud%tamanoOlas != 0){
            while (longitud%tamanoOlas != 0){
                longitud++;
                texto += "|";
            }
            numeroOlas = longitud/tamanoOlas;
        }else{
            numeroOlas = longitud/tamanoOlas;
        }
        char[] textoacifrar = texto.toCharArray();

        for (int i = 0; i < N; i++){
            ArrayList<Character> renglon = new ArrayList<>();
            Panel.add(renglon);
        }

        int numeroRenglon = 0;
        boolean descendente = true;

        for (int i = 0; i < longitud; i++){
            if (descendente){
                Panel.get(numeroRenglon).add(textoacifrar[i]);
                if (numeroRenglon == N - 1){
                    descendente = false;
                    numeroRenglon--;
                }else{
                    numeroRenglon++;
                }
            }else{
                Panel.get(numeroRenglon).add(textoacifrar[i]);
                if (numeroRenglon == 0){
                    descendente = true;
                    numeroRenglon++;
                }else{
                    numeroRenglon--;
                }
            }
        }

        for (int i = 0; i < Panel.size(); i++){
            for (int j = 0; j < Panel.get(i).size(); j++){
                TextoCifrado += Panel.get(i).get(j);
            }
        }

        return TextoCifrado;
    }

    public String Descifrar (String Nivel, String Texto){
        ArrayList<char[]> bloques = new ArrayList<>();
        int N = Integer.parseInt(Nivel);
        int longitud = Texto.length();
        int tamanoOlas = (N*2) - 2;
        int numeroOlas = longitud/tamanoOlas;
        int tamanoBloque = 2 * tamanoOlas;
        char[] crestas = Texto.substring(0, numeroOlas).toCharArray();
        char[] bases = Texto.substring(Texto.length() - numeroOlas).toCharArray();
        int indice = numeroOlas;
        int finbloques = Texto.length() - numeroOlas;

        while(indice <  finbloques){
            bloques.add(Texto.substring(indice, tamanoBloque).toCharArray());
            indice = indice + tamanoBloque;
        }

        int cantidadBloqques = Texto.substring(numeroOlas, Texto.length()-numeroOlas).length();
        int indicecrestas = 0;
        int indicebases = 0;
        int indicebloques = 0;
        boolean terminado = false;
        boolean descendente = true;
        String textoDescifrado = "";

        while (!terminado){
            if (descendente){
                textoDescifrado += crestas[indicecrestas];
                indicecrestas++;
                for (int i = 0; i < cantidadBloqques; i++){
                    textoDescifrado += bloques.get(i)[indicebloques];
                }
                indicebloques++;
                descendente = false;
            }else{
                textoDescifrado += bases[indicebases];
                indicebases++;
                for (int i = cantidadBloqques - 1; i >= 0; i--){
                    textoDescifrado += bloques.get(i)[indicebloques];
                }
                indicebloques++;
                descendente = true;
                if (indicebases == numeroOlas){
                    terminado = true;
                }
            }
        }
        
        return textoDescifrado;
    }
}
