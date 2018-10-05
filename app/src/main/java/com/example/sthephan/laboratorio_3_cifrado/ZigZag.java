package com.example.sthephan.laboratorio_3_cifrado;

import java.util.ArrayList;

public class ZigZag {

    public ArrayList<ArrayList<Character>> Panel = new ArrayList<>();

    public String cifrado(String nivel, String texto){
        char[] textoacifrar = texto.toCharArray();
        int N = Integer.parseInt(nivel);
        int longitud = texto.length();
        int tamañoOlas = (N*2) - 2;
        int numeroOlas = 0;
        String TextoCifrado = "";
        if (longitud%tamañoOlas != 0){
            while (longitud%tamañoOlas != 0){
                longitud++;
                texto += "|";
            }
            numeroOlas = longitud/tamañoOlas;
        }else{
            numeroOlas = longitud/tamañoOlas;
        }

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
}
