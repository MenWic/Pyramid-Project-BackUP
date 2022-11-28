package com.example.pyramid.models;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ArbolAVL {

    private Nodo nodoRaiz; //Carta principal o raiz del arbol
    private int altura;
    private int nivel; //Nivel del arbol
    private int cantidadImagen; //Graphv

    //Constructor
    public ArbolAVL() {
        this.nodoRaiz = null;
        this.altura = 0;
        this.nivel = 0;
        this.cantidadImagen = 0;
    }

    //Metodo para jalar al NodoRaiz
    public Nodo getNodoRaiz() {
        return nodoRaiz;
    }

    /* 
    *   Metodos para funciones de ArbolAVL
    */
    //Metodo recursivo para buscar cierto nodo dentro de los nodos del arbol y retornarlo si lo encuentra
    public Nodo buscarNodo(int dato, Nodo raiz) {
        //Buscar nodo siempre que la raiz inicial no sea null
        try {
            if (nodoRaiz == null) {
                return null;
            } else if (raiz.getValor() == dato) {
                return raiz;
            } else if (raiz.getValor() < dato) {
                return buscarNodo(dato, raiz.getHijoDer()); //Recursividad para seguir buscando
            } else {
                return buscarNodo(dato, raiz.getHijoIzq());
            }
        } catch (NullPointerException np) {
            return null;
        }
    }

    //Metodo para verificar si el nodo existe (si existe no puede retornar null)
    public boolean existeNodo(int valor, Nodo nodoRaiz) {
        return buscarNodo(valor, nodoRaiz) != null;
    }

    private int obtenerFactorEquilibrio(Nodo nodo) {

        if (nodo == null) {
            return -1;
        } else {
            return nodo.getFactEquilibrio();
        }

    }

    /*
    *   Movimientos Arbol: Rotciones de cartas para autoequilibracion del ArbolAVL
    */
    //Metodo para rotcion hacia la izquierda
    private Nodo rotacionIzq(Nodo nodo) {

        Nodo auxiliar = nodo.getHijoIzq();
        nodo.setHijoIzq(auxiliar.getHijoDer());
        auxiliar.setHijoDer(nodo);

        int feIzquierdo = obtenerFactorEquilibrio(nodo.getHijoIzq());
        int feDerecho = obtenerFactorEquilibrio(nodo.getHijoDer());
        int feIzquierdo2 = obtenerFactorEquilibrio(auxiliar.getHijoIzq());
        int feDerecho2 = obtenerFactorEquilibrio(auxiliar.getHijoDer());

        nodo.setFactEquilibrio(Math.max(feIzquierdo, feDerecho + 1));
        auxiliar.setFactEquilibrio(Math.max(feIzquierdo2, feDerecho2 + 1));

        return auxiliar;
    }

    //Metodo para rotcion hacia la derecha
    private Nodo rotacionDer(Nodo nodo) {

        Nodo auxiliar = nodo.getHijoDer();
        nodo.setHijoDer(auxiliar.getHijoIzq());
        auxiliar.setHijoIzq(nodo);

        int feIzquierdo = obtenerFactorEquilibrio(nodo.getHijoIzq());
        int feDerecho = obtenerFactorEquilibrio(nodo.getHijoDer());
        int feIzquierdo2 = obtenerFactorEquilibrio(auxiliar.getHijoIzq());
        int feDerecho2 = obtenerFactorEquilibrio(auxiliar.getHijoDer());

        nodo.setFactEquilibrio(Math.max(feIzquierdo, feDerecho + 1));
        auxiliar.setFactEquilibrio(Math.max(feIzquierdo2, feDerecho2 + 1));

        return auxiliar;
    }

    //Metodo para rotacion izquierda-derecha o doble hacia la izquierda
    private Nodo rotacionDobleIzq(Nodo nodo) {
        Nodo temporal;

        nodo.setHijoIzq(rotacionDer(nodo.getHijoIzq()));
        temporal = rotacionIzq(nodo);

        return temporal;
    }

    //Metodo para rotacion derecha-izquierda o doble hacia la derecha
    private Nodo rotacionDobleDer(Nodo nodo) {
        Nodo temporal;
        
        nodo.setHijoDer(rotacionIzq(nodo.getHijoDer()));
        temporal = rotacionDer(nodo);

        return temporal;
    }

    //Metodo para insertar forma del arbol AVL incial
    private Nodo insertarAVL(Nodo nuevo, Nodo subArbol) {

        Nodo nuevoPadre = subArbol;

        if (nuevo.getValor() < subArbol.getValor()) {

            if (subArbol.getHijoIzq() == null) {
                subArbol.setHijoIzq(nuevo);
            } else {
                subArbol.setHijoIzq(insertarAVL(nuevo, subArbol.getHijoIzq()));
                
                if ((obtenerFactorEquilibrio(subArbol.getHijoIzq())
                        - obtenerFactorEquilibrio(subArbol.getHijoDer())) == 2) {
                    if (nuevo.getValor() < subArbol.getHijoIzq().getValor()) {
                        nuevoPadre = rotacionIzq(subArbol);
                    } else {
                        nuevoPadre = rotacionDobleIzq(subArbol);
                    }
                }
            }

        } else if (nuevo.getValor() > subArbol.getValor()) {
            
            if (subArbol.getHijoDer() == null) {
                subArbol.setHijoDer(nuevo);
            } else {
                subArbol.setHijoDer(insertarAVL(nuevo, subArbol.getHijoDer()));
                
                if ((obtenerFactorEquilibrio(subArbol.getHijoDer())
                        - obtenerFactorEquilibrio(subArbol.getHijoIzq())) == 2) {
                    if (nuevo.getValor() > subArbol.getHijoDer().getValor()) {
                        nuevoPadre = rotacionDer(subArbol);
                    } else {
                        nuevoPadre = rotacionDer(subArbol);
                    }
                }
            }

        } else {
            return null; //Pasa cuando esta duplicado
        }

        if ((subArbol.getHijoIzq() == null) && (subArbol.getHijoDer() != null)) {
            subArbol.setFactEquilibrio(subArbol.getHijoDer().getFactEquilibrio() + 1);
        } else if ((subArbol.getHijoDer() == null) && (subArbol.getHijoIzq() != null)) {
            subArbol.setFactEquilibrio(subArbol.getHijoIzq().getFactEquilibrio() + 1);
        } else {
            int feIzquierdo = obtenerFactorEquilibrio(subArbol.getHijoIzq());
            int feDerecho = obtenerFactorEquilibrio(subArbol.getHijoDer());
            subArbol.setFactEquilibrio(Math.max(feIzquierdo, feDerecho));
        }
        return nuevoPadre; //Devolver el nuevo padre del nuevo subarbol
    }

    //Metodo para insertar carta al arbol formalmente
    public int insertarNodo(int valor, int valorUnico, String simbolo) {
        Nodo nuevoNodo = new Nodo(valor);
        nuevoNodo.setValorUnico(valorUnico);
        nuevoNodo.setSimbolo(simbolo);

        if (this.nodoRaiz == null) {
            this.nodoRaiz = nuevoNodo;
        } else {
            this.nodoRaiz = insertarAVL(nuevoNodo, nodoRaiz);
            
            if (this.nodoRaiz == null) {
                return 406;
            }
        }
        return 200;
    }

    //Metodo para asignar nivel a nodo del arbol (carta)
    public void asignarNivelNodo(Nodo nodoRaiz) {
        Nodo nodoIzq = null;
        Nodo nodoDer = null;

        if (nodoRaiz.getHijoIzq() != null) {
            int nivelHijo = nodoRaiz.getNivel() + 1;
            nodoRaiz.getHijoIzq().setNivel(nivelHijo);
            nodoIzq = nodoRaiz.getHijoIzq();
        }

        if (nodoRaiz.getHijoDer() != null) {
            int nivelHijo = nodoRaiz.getNivel() + 1;
            nodoRaiz.getHijoDer().setNivel(nivelHijo);
            nodoDer = nodoRaiz.getHijoDer();
        }

        if (nodoIzq != null) {
            asignarNivelNodo(nodoIzq);
        }
        if (nodoDer != null) {
            asignarNivelNodo(nodoDer);
        }

    }

    public void buscarNodoPorNivel(ArrayList<Nodo> nodos, Nodo nodoRaiz, int nivel) {

        if (nodoRaiz.getNivel() == nivel) {
            nodos.add(nodoRaiz);
            return;
        }

        if (nodoRaiz.getHijoIzq() != null) {
            if (nodoRaiz.getHijoIzq().getNivel() == nivel) {
                nodos.add(nodoRaiz.getHijoIzq());
            } else {
                Nodo tempRaiz = nodoRaiz.getHijoIzq();
                buscarNodoPorNivel(nodos, tempRaiz, nivel);
            }
        }

        if (nodoRaiz.getHijoDer() != null) {
            if (nodoRaiz.getHijoDer().getNivel() == nivel) {
                nodos.add(nodoRaiz.getHijoDer());
            } else {
                Nodo tempRaiz = nodoRaiz.getHijoDer();
                buscarNodoPorNivel(nodos, tempRaiz, nivel);
            }
        }

    }

    //Metodo para eliminar nodos del arbol
    public int eliminarNodo(int valor, Nodo nodoRaiz, Nodo nodoPadre) {

        if (nodoRaiz.getValor() == valor) { //Si el valor del nodo raiz coincide con el valor a eliminar
            if (esHoja(nodoRaiz)) { //si el nodo raiz es un nodo hoja
                return eliminarNodoHoja(valor, nodoRaiz, nodoPadre); //Se aplica metodo de eliminacion
            }
        }

        if (valor < nodoRaiz.getValor()) { //Si el valor es mas pequenio que la raiz
            Nodo padre = nodoRaiz; //Setteamos al nuevo padre
            Nodo raiz = nodoRaiz.getHijoIzq(); //La nueva raiz sera el HijoIzq del anterior Raiz
            eliminarNodo(valor, raiz, padre); //Se aplica metodo de eliminacion
        }

        if (valor > nodoRaiz.getValor()) { //Si el valor es mayor que la raiz
            Nodo padre = nodoRaiz; //Setteamos al nuevo padre
            Nodo raiz = nodoRaiz.getHijoDer(); //La nueva raiz sera el HijoDer del anterior Raiz
            eliminarNodo(valor, raiz, padre); //Se aplica metodo de eliminacion
        }
        return 200;
    }

    //Metodo para agregar a los hijos del nodoHijoDerecho
    private void agregarHijos(Nodo nodoRaiz, ArrayList<Nodo> hijos) {

        if (esHoja(nodoRaiz)) {
            hijos.add(nodoRaiz);
        }

        if (nodoRaiz.getHijoIzq() != null) {
            hijos.add(nodoRaiz.getHijoIzq());
            Nodo raizNueva = nodoRaiz.getHijoIzq();
            agregarHijos(raizNueva, hijos);
        }

        if (nodoRaiz.getHijoDer() != null) {
            hijos.add(nodoRaiz.getHijoDer());
            Nodo raizNueva = nodoRaiz.getHijoDer();
            agregarHijos(raizNueva, hijos);
        }

    }

    //Metodo de eliminacion de nodoHoja
    private int eliminarNodoHoja(int valor, Nodo nodoRaiz, Nodo nodoPadre) {

        if (valor > nodoPadre.getValor()) {
            nodoPadre.setHijoDer(null); //Hijo Der = null
        } else if (valor < nodoPadre.getValor()) {
            nodoPadre.setHijoIzq(null); //Hijo Izq = null
        }

        return 200;
    }

    //Verificar si el nodo es Hoja
    public boolean esHoja(Nodo nodo) {
        return ((nodo.getHijoIzq() == null) && (nodo.getHijoDer() == null));
    }

    public void enOrden(ArrayList<String> numeros) {
        this.nodoRaiz.enOrden(numeros);

    }

    public void preOrden(ArrayList<String> numeros) {
        this.nodoRaiz.preOrden(numeros);
    }

    public void postOrden(ArrayList<String> numeros) {
        this.nodoRaiz.postOrden(numeros);
    }

    //Metodod usado para textear formato del .dot
    public String obtenerGraphviz() {

        String texto = "digraph G\n"
            + "{\n"
            + "    node[shape = cicle]\n"
            + "    node[style = filled]\n";

        if (nodoRaiz != null) {
            texto += nodoRaiz.textoGraphviz();
        }

        texto += "}\n";

        return texto;
    }

    //Metodo para escribir archivo de la grafica
    public void escribirArchivo(String ruta, String contenido) {

        FileWriter archivo = null;
        PrintWriter pw = null;

        try {
            archivo = new FileWriter(ruta);
            pw = new PrintWriter(archivo);
            pw.write(contenido);
            pw.close();
            archivo.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    //Metodo para dibujar la grafica del arbol
    public String dibujarArbol() {
        
        try {
            this.cantidadImagen++;
            escribirArchivo("archivo.dot", obtenerGraphviz());
            ProcessBuilder pb;
            pb = new ProcessBuilder("dot", "-Tjpg", "-o", "arbol" + this.cantidadImagen + ".jpg", "archivo.dot");
            pb.redirectErrorStream(true);
            pb.start();
            String ruta = "arbol" + this.cantidadImagen + ".jpg";
            return ruta;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "-";
    }
}
