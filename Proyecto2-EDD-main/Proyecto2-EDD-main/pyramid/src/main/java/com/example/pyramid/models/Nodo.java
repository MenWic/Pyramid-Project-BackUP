package com.example.pyramid.models;

import java.util.ArrayList;

public class Nodo {

    private int valorUnico; //Valor de carta
    private int valor; //ValorUnico + ValorCorrimiento
    private String simbolo; //Caracter que se usara para sumarle ValorCorrimiento a Valor
    private int altura; //Cuantos niveles de hijos tiene
    private int nivel; //Nivel del arbol al que pertenece
    private int factEquilibrio; //hijoIzquierdo.getAltura() - hijoDerecho.getAltura()
    
    private boolean esHoja; //Indicador para saber si se puede eliminar
    private Nodo hijoIzq; //Enlace a carta menor izquierda
    private Nodo hijoDer; //Enlace a carta superior derecha

    public Nodo() {
    }

    //Constructor de Cartas
    public Nodo(int valor) {
        this.valor = valor;
        this.altura = 0;
        this.nivel = 0;
        this.esHoja = false;
        this.hijoIzq = null;
        this.hijoDer = null;
        this.factEquilibrio = 0;
    }

    /*
    *   Metodos Getters y Setters
    */
    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public Nodo getHijoIzq() {
        return hijoIzq;
    }

    public void setHijoIzq(Nodo hijoIzq) {
        this.hijoIzq = hijoIzq;
    }

    public Nodo getHijoDer() {
        return hijoDer;
    }

    public void setHijoDer(Nodo hijoDer) {
        this.hijoDer = hijoDer;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public boolean isEsHoja() {
        return esHoja;
    }

    public void setEsHoja(boolean esHoja) {
        this.esHoja = esHoja;
    }

    public int getFactEquilibrio() {
        return factEquilibrio;
    }

    public void setFactEquilibrio(int factEquilibrio) {
        this.factEquilibrio = factEquilibrio;
    }

    public int getValorUnico() {
        return valorUnico;
    }

    public void setValorUnico(int valorUnico) {
        this.valorUnico = valorUnico;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    /*
    *   Metodos de ordenamiento de cartas
    */
    public void enOrden(ArrayList<String> numeros) {

        if (hijoIzq != null) {
            hijoIzq.enOrden(numeros);
        }

        numeros.add(this.valorUnico + this.simbolo);

        if (hijoDer != null) {
            hijoDer.enOrden(numeros);
        }
    }

    public void preOrden(ArrayList<String> numeros) {

        numeros.add(this.valorUnico + this.simbolo);

        if (hijoIzq != null) {
            hijoIzq.preOrden(numeros);
        }
        if (hijoDer != null) {
            hijoDer.preOrden(numeros);
        }
    }

    public void postOrden(ArrayList<String> numeros) {

        if (hijoIzq != null) {
            hijoIzq.postOrden(numeros);
        }

        if (hijoDer != null) {
            hijoDer.postOrden(numeros);
        }

        numeros.add(this.valorUnico + this.simbolo);
    }

    /*
    *   Metodo para retornar letra inicial del simbolo
    *   Nota: Graphviz no me reconocio los simbolos, por eso use Letras
    */
    public String letraSimbolo(String simbolo) {
        if (simbolo.equals("♣")) {
            return "T";
        }
        if (simbolo.equals("♦")) {
            return "D";
        }
        if (simbolo.equals("♥")) {
            return "C";
        }
        if (simbolo.equals("♠")) {
            return "P";
        }
        return "";
    }

    /*
    *   Metodo empleado para crear sentencia de grafica de
    *   herramienta Graphviz
    */
    public String textoGraphviz() {

        if (hijoIzq == null && hijoDer == null) {
            String simbolo = letraSimbolo(this.simbolo);

            return String.valueOf("<" + this.valorUnico + simbolo + ">");
        } else {
            String texto = "";
            String simbolo = letraSimbolo(this.simbolo);

            if (hijoIzq != null) {
                texto = "<" + this.valorUnico + simbolo + ">" + "->" + hijoIzq.textoGraphviz() + "\n";
            }

            if (hijoDer != null) {
                texto += "<" + this.valorUnico + simbolo + ">" + "->" + hijoDer.textoGraphviz() + "\n";
            }

            return texto;
        }
    }
}
