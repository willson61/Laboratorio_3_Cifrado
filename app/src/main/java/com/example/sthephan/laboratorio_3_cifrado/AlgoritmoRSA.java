package com.example.sthephan.laboratorio_3_cifrado;

import java.util.ArrayList;
import java.util.HashMap;

public class AlgoritmoRSA {

    public Double[] Primos = {41.0, 43.0, 47.0, 53.0, 61.0, 59.0, 67.0, 71.0, 73.0, 79.0, 83.0, 89.0, 97.0, 101.0,
            103.0, 107.0, 109.0, 113.0, 127.0, 131.0, 137.0, 149.0, 139.0, 149.0, 151.0, 157.0, 163.0, 167.0,  173.0,
            179.0, 181.0, 191.0, 193.0, 197.0, 199.0, 211.0, 223.0, 227.0, 229.0, 233.0, 239.0, 241.0, 251.0, 257.0,
            263.0, 269.0, 271.0, 277.0, 281.0, 283.0, 293.0, 307.0, 311.0, 313.0, 317.0, 331.0, 337.0, 347.0, 349.0,
            353.0, 359.0, 367.0, 373.0, 379.0, 383.0, 389.0, 397.0, 401.0, 409.0, 419.0, 421.0, 431.0, 433.0, 439.0,
            443.0, 449.0, 457.0, 461.0, 463.0, 467.0, 479.0, 487.0, 491.0, 499.0, 503.0, 509.0, 521.0, 523.0, 541.0,
            547.0, 557.0, 563.0, 569.0, 571.0, 577.0, 587.0, 593.0, 599.0, 601.0, 607.0, 613.0, 617.0, 619.0, 631.0,
            641.0, 643.0, 647.0, 653.0, 659.0, 661.0, 673.0, 677.0, 683.0, 691.0, 701.0, 709.0, 719.0, 727.0, 733.0,
            739.0, 743.0, 751.0, 757.0, 761.0, 769.0, 773.0, 787.0, 797.0, 809.0, 811.0, 821.0, 823.0, 827.0, 829.0,
            839.0, 853.0, 857.0, 859.0, 863.0, 877.0, 881.0, 883.0, 887.0, 907.0, 911.0, 919.0, 929.0, 937.0, 941.0,
            947.0, 953.0, 967.0, 971.0, 977.0, 983.0, 991.0, 997.0, 1009.0, 1013.0, 1019.0, 1021.0, 1031.0, 1033.0,
            1039.0, 1049.0, 1051.0, 1061.0, 1063.0, 1069.0,  1087.0, 1091.0, 1093.0, 1097.0, 1103.0, 1109.0, 1117.0,
            1123.0, 1129.0, 1151.0, 1153.0, 1163.0, 1171.0, 1181.0, 1187.0, 1193.0, 1201.0, 1213.0, 1217.0, 1223.0,
            1229.0, 1231.0, 1237.0, 1249.0, 1259.0, 1277.0, 1279.0, 1283.0, 1289.0, 1291.0, 1297.0, 1301.0, 1303.0,
            1307.0, 1319.0, 1321.0, 1327.0, 1361.0, 1367.0, 1373.0, 1381.0, 1399.0, 1409.0, 1423.0, 1427.0, 1429.0,
            1433.0, 1439.0, 1447.0, 1451.0, 1453.0, 1459.0, 1471.0, 1481.0, 1483.0, 1487.0, 1489.0, 1493.0, 1499.0};

    public void GenerarClaves(double primmop, double primoq){
        double n = 0;
        double fi = 0;
        double e = 0;
        double d = 1;
        n = primmop * primoq;
        fi = (primmop - 1) * (primoq - 1);
        e = encontrarPrimo(fi);
        boolean encontrarD = false;
        while (!encontrarD){
            if (d*e%fi == 1){
                encontrarD = true;
            }else{
                d++;
            }
        }
    }

    private String getClavePrivada(double n, double d){
        return String.valueOf(n) + "," + String.valueOf(d);
    }

    private String getClavePublica(double n, double e){
        return String.valueOf(n) + "," + String.valueOf(e);
    }

    private double encontrarPrimo(double numero){
        double numeroPrimo = 0.0;
        boolean esPrimo = false;
        int contador = (int) numero/2;
        int contador2 = 2;

        while(!esPrimo || contador < numero){
            if (contador%contador2 == 0){
                esPrimo = true;
            }else{
                contador2++;
            }
        }
        return contador2;
    }

    public String cifrar(String cadena, double e, double n){
        String textoCifrado = "";
        for (int i = 0; i < cadena.length(); i++){
            char caracter = cadena.charAt(i);
            int N = (int) caracter;
            double C = Math.pow(N, e)%n;
            textoCifrado += (char) C;
        }
        return textoCifrado;
    }

    public String descifrar(String cadena, double d, double n){
        String textoDescifrado = "";
        for (int i = 0; i < cadena.length(); i++){
            char caracter = cadena.charAt(i);
            int C = (int) caracter;
            double N = Math.pow(C, d)%n;
            textoDescifrado += (char) N;
        }
        return textoDescifrado;
    }
}
