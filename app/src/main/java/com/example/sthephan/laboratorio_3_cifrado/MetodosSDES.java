package com.example.sthephan.laboratorio_3_cifrado;

import java.util.HashMap;

public class MetodosSDES {

    public HashMap<String, String> S0 = new HashMap<>();
    public HashMap<String, String> S1 = new HashMap<>();

    public void setearSBoxes(){
        if(S0.size() == 0){
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
        }
        if(S1.size() == 0){
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
    }

    public String devolverValorS0(String binario){
        String binarioNuevo = "";
        char[] indice = binario.toCharArray();
        binarioNuevo += indice[0];
        binarioNuevo += indice[3];
        binarioNuevo += indice[1];
        binarioNuevo += indice[2];
        return S0.get(binarioNuevo);
    }

    public String devolverValorS1(String binario){
        String binarioNuevo = "";
        char[] indice = binario.toCharArray();
        binarioNuevo += indice[0];
        binarioNuevo += indice[3];
        binarioNuevo += indice[1];
        binarioNuevo += indice[2];
        return S1.get(binarioNuevo);
    }

    public String Switch(String binario){
        String binarioReordenado = "";
        binarioReordenado += binario.substring(binario.length()/2, binario.length());
        binarioReordenado += binario.substring(0, binario.length() / 2);
        return binarioReordenado;
    }

    public String XOR(String binario, String llave){
        String resultadoXOR = "";
        char[] arregloBinario = binario.toCharArray();
        char[] arregloLlave = llave.toCharArray();
        for (int i = 0; i < binario.length(); i++){
            if (arregloBinario[i] == arregloLlave[i]){
                resultadoXOR += "0";
            }else{
                resultadoXOR += "1";
            }
        }
        return resultadoXOR;
    }

    public String extraerBinarioDeAscii(String codigoAscii){
        String txtEnBinario = "";
        for (int i = 0; i < codigoAscii.length(); i++){
            String asciiABinario = Integer.toBinaryString(codigoAscii.charAt(i));
            if (asciiABinario.length() % 8 != 0){
                int cerosRestantes = 8 - asciiABinario.length() % 8;
                for (int j = 0; j < cerosRestantes; j++){
                    asciiABinario = "0" + asciiABinario;
                }
            }
            txtEnBinario += asciiABinario;
        }
        return txtEnBinario;
    }

    public String textoToAscii(String txtBinario){
        String txtAscii = "";
        int cont = 0;
        String ascii = "";
        int num = 0;

        for (int i = 0; i < txtBinario.length(); i++) {
            cont++;
            if (cont <= 8) {
                ascii = ascii + txtBinario.charAt(i);
                if ((cont == 8)||(i == txtBinario.length() - 1)){
                    num = Integer.parseInt(ascii,2);
                    txtAscii += (char)Integer.valueOf(num).intValue();
                    cont = 0;
                    ascii = "";
                }
            }
        }
        return txtAscii;
    }
}
