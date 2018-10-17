package com.example.sthephan.laboratorio_3_cifrado;

public class SDES {
    LlavesSDES Llaves = new LlavesSDES();
    MetodosSDES Metodos = new MetodosSDES();
    Permutaciones Permutacion = new Permutaciones();

    public Character Cifrar(char caracter){
        char retornar = ' ';
        String carac = "";
        carac += caracter;
        String binario = Metodos.extraerBinarioDeAscii(carac);
        String pasoInicial = Permutacion.PermutacionInicial(binario);
        String pasoEP = pasoInicial.substring(pasoInicial.length()/2);
        String pasoXOR = Permutacion.ExpandiryPermutar(pasoEP);
        String resultadoXOR = Metodos.XOR(pasoXOR, Llaves.getK1());
        String pasoS0 = resultadoXOR.substring(0, resultadoXOR.length()/2);
        String pasoS1 = resultadoXOR.substring(resultadoXOR.length()/2);
        String pasoS0Resultado = Metodos.devolverValorS0(pasoS0);
        String pasoS1Resultado = Metodos.devolverValorS1(pasoS1);
        String PasoP4 = Permutacion.Permutacion4(pasoS0Resultado + pasoS1Resultado);
        String ResultadoXOR2 = Metodos.XOR(pasoInicial.substring(0, pasoInicial.length()/2), PasoP4);
        String finFK1 = ResultadoXOR2 + pasoInicial.substring(pasoInicial.length()/2, pasoInicial.length());
        String ResultadoSW = Metodos.Switch(finFK1);
            String pasoEP2 = ResultadoSW.substring(ResultadoSW.length()/2);
        String pasoXOR3 = Permutacion.ExpandiryPermutar(pasoEP2);
        String resultadoXOR3 = Metodos.XOR(pasoXOR3, Llaves.getK2());
        String pasoS02 = resultadoXOR3.substring(0, resultadoXOR3.length()/2);
        String pasoS12 = resultadoXOR3.substring(resultadoXOR3.length()/2);
        String pasoS0Resultado2 = Metodos.devolverValorS0(pasoS02);
        String pasoS1Resultado2 = Metodos.devolverValorS1(pasoS12);
        String PasoP42 = Permutacion.Permutacion4(pasoS0Resultado2 + pasoS1Resultado2);
        String ResultadoXOR4 = Metodos.XOR(ResultadoSW.substring(0, ResultadoSW.length()/2), PasoP42);
        String finFK2 = ResultadoXOR4 + ResultadoSW.substring(ResultadoSW.length()/2, ResultadoSW.length());
        String BinarioFinal = Permutacion.PermutacionInversa(finFK2);
        String Final = Metodos.textoToAscii(BinarioFinal);
        retornar = Final.charAt(0);
        return retornar;
    }

    public Character Descifrar(char caracter){
        char retornar = ' ';
        String carac = "";
        carac += caracter;
        String binario = Metodos.extraerBinarioDeAscii(carac);
        String pasoInicial = Permutacion.PermutacionInicial(binario);
        String pasoEP = pasoInicial.substring(pasoInicial.length()/2);
        String pasoXOR = Permutacion.ExpandiryPermutar(pasoEP);
        String resultadoXOR = Metodos.XOR(pasoXOR, Llaves.getK1());
        String pasoS0 = resultadoXOR.substring(0, resultadoXOR.length()/2);
        String pasoS1 = resultadoXOR.substring(resultadoXOR.length()/2);
        String pasoS0Resultado = Metodos.devolverValorS0(pasoS0);
        String pasoS1Resultado = Metodos.devolverValorS1(pasoS1);
        String PasoP4 = Permutacion.Permutacion4(pasoS0Resultado + pasoS1Resultado);
        String ResultadoXOR2 = Metodos.XOR(pasoInicial.substring(0, pasoInicial.length()/2), PasoP4);
        String finFK1 = ResultadoXOR2 + pasoInicial.substring(pasoInicial.length()/2, pasoInicial.length());
        String ResultadoSW = Metodos.Switch(finFK1);
        String pasoEP2 = ResultadoSW.substring(ResultadoSW.length()/2);
        String pasoXOR3 = Permutacion.ExpandiryPermutar(pasoEP2);
        String resultadoXOR3 = Metodos.XOR(pasoXOR3, Llaves.getK2());
        String pasoS02 = resultadoXOR3.substring(0, resultadoXOR3.length()/2);
        String pasoS12 = resultadoXOR3.substring(resultadoXOR3.length()/2);
        String pasoS0Resultado2 = Metodos.devolverValorS0(pasoS02);
        String pasoS1Resultado2 = Metodos.devolverValorS1(pasoS12);
        String PasoP42 = Permutacion.Permutacion4(pasoS0Resultado2 + pasoS1Resultado2);
        String ResultadoXOR4 = Metodos.XOR(ResultadoSW.substring(0, ResultadoSW.length()/2), PasoP42);
        String finFK2 = ResultadoXOR4 + ResultadoSW.substring(ResultadoSW.length()/2, ResultadoSW.length());
        String BinarioFinal = Permutacion.PermutacionInversa(finFK2);
        String Final = Metodos.textoToAscii(BinarioFinal);
        retornar = Final.charAt(0);
        return retornar;
    }
}
