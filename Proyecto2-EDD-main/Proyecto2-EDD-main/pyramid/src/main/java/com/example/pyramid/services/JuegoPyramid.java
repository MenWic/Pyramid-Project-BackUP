package com.example.pyramid.services;

import java.util.ArrayList;

import com.example.pyramid.models.ArbolAVL;
import com.example.pyramid.models.Nodo;

/*
*   Este clase contiene todos los metodos generales que son invocados 
*   desde la clase Controlador.java, que a su vez se conforman de otros sub-metodos
*   que hacen diversas llamadas, manejos de datos, etc
*/
public class JuegoPyramid {
    
    private ArbolAVL arbol;
    private ArrayList<Integer> valoresUnicos; //Arreglo para almacenar valores de cartas ordenados
    private ArrayList<String> simbolos; //Arreglo para almacenar simbolos de cartas en orden
    private ArrayList<Nodo> nodosPorNivel; //Arreglo para cartas de cierto nivel
    private ArrayList<String> nodosOrdenados; //Arreglo para almacenar cartas en orden que se mostraran

    public JuegoPyramid() {
        arbol = new ArbolAVL();
    }

    /*
    *   Metodos Generales del funcionamiento de la API
    */
    //Metodo General para iniciar el juego (insertando cartas iniciales) que retorna estados
    public int iniciarPyramid(String json) {
    
        ArrayList<Integer> numeros = parseJson(json);
        ArrayList<Integer> valoresUnicos = this.valoresUnicos;

        //Verificar que los numeros ingresados (sin corrimiento) tengan valores entre 1 y 13
        for (int i = 0; i < valoresUnicos.size(); i++){
            if (valoresUnicos.get(i)>13 || valoresUnicos.get(i) < 1){
                return 400;
            }
        }

        //Verificamos si ya existen los valores en el arbol
        for (int i = 0; i < numeros.size(); i++) {
            if (arbol.existeNodo(numeros.get(i), arbol.getNodoRaiz())) {
                return 406;
            }
        }

        //insertamos los valores al arbol
        for (int i = 0; i < numeros.size(); i++) {
            arbol.insertarNodo(numeros.get(i), valoresUnicos.get(i), simbolos.get(i));
        }

        //System.out.println("Cartas insertadas exitosamente!");
        return 200;
    }
    
    //Metodo General para parsear datos de cartas del JSON a valores
    public ArrayList<Integer> parseJson(String json) {
        
        valoresUnicos = new ArrayList<>();
        simbolos = new ArrayList<>();

        //Separar body del JSON por comas
        String dataSinComas[] = json.split(",");

        //Aqui almaceno numero + text
        ArrayList<String> dataCartas = new ArrayList<>();

        //Agregar inserciones al ArrayList valoresCartas que almacenan los numeros en formato String
        for (int i = 0; i < dataSinComas.length; i++) {
            String[] dataSinPuntos = dataSinComas[i].split(":");
            dataCartas.add(dataSinPuntos[1]); //la posicion 0 es la comilla, y la posicion 1 el numero
        }

        //Creamos 2 ArrayList's, 1 para almacenar valores de cartas y 1 para almacenar valores de corrimientos
        ArrayList<String> valoresSinCorrimiento = new ArrayList<>();
        ArrayList<Integer> corrimientos = new ArrayList<>();

        for (int i = 0; i < dataCartas.size(); i++) {

            //Creamos un CharArray que pasara a caracteres a dataCartas
            char[] caracteres = dataCartas.get(i).toCharArray();
            String valorSinCorrim = ""; //Valor de carta
            int corrimiento = 0; //Corrimiento de carta en base a su simbolo
            
            for (int j = 0; j < caracteres.length; j++) {
                if (Character.isDigit(caracteres[j])) { //Si el caracter es un digito numerico, lo almacenamos
                    valorSinCorrim += caracteres[j]; //Le sumamos el valor y lo reasignamos
                    corrimiento = agregarCorrimiento(caracteres[j + 1], simbolos); //Metodo que suma corrimiento

                }
                if (esLetraAdmitida(caracteres[j])) { //Si el carcater es una letra, obtenemos el valor de esa letra
                    int valorLetra = obtenerValorLetra(caracteres[j]);
                    valorSinCorrim += valorLetra; //le sumamos el valor y lo reasiganmos
                    corrimiento = agregarCorrimiento(caracteres[j + 1], simbolos); //Metodo que suma corrimiento
                }
            }
            valoresSinCorrimiento.add(valorSinCorrim); //Por ultimo agregamos el numero con corrimiento al array
            corrimientos.add(corrimiento); //Por ultimo agregamos el corrimiento respectivo del valor de la incersion (carta) al array 
        }

        //Creamos un ArrayList para almacenar los mismos valores pero parseados a Int (los traia en String)
        ArrayList<Integer> valorNumeros = new ArrayList<>();
        for (int i = 0; i < valoresSinCorrimiento.size(); i++) {
            int val = Integer.parseInt(valoresSinCorrimiento.get(i)) + corrimientos.get(i);
            int valUnico = Integer.parseInt(valoresSinCorrimiento.get(i)); //Ese numero es el valor de la carta que va previo al simbolo 
            valoresUnicos.add(valUnico);
            //System.out.println(valorUnico);
            valorNumeros.add(val);
        }

        return valorNumeros; //Retornamos numero entero que es el valor de una carta con corrimiento aplicado
    }

    //Metodo General para insertar cartas indicadas en el JSON, a un arbol AVL que retorna estados
    public int insertarCarta(String json) {

        ArrayList<Integer> numeros = parseJson(json);
        ArrayList<Integer> valoresUnicos = this.valoresUnicos;

        //Verificamos que los valores a insertar sean de valores entre 1 y 13
        for (int i = 0; i < valoresUnicos.size(); i++){
            if (valoresUnicos.get(i)>13 || valoresUnicos.get(i) < 1){
                return 400;
            }
        }
        //Verificamos si existen los valores en el arbol
        for (int i = 0; i < numeros.size(); i++) {
            if (arbol.existeNodo(numeros.get(i), arbol.getNodoRaiz())) {
                return 406;
            }
        }
        //insertamos los valores al arbol
        for (int i = 0; i < numeros.size(); i++) {
            arbol.insertarNodo(numeros.get(i), valoresUnicos.get(i), simbolos.get(i));
        }

        //System.out.println("Cartas insertadas exitosamente!");
        return 200;
    }

    //Metodo General para eliminar cartas indicadas en el JSON, del arbol AVL que retorna estados
    public int eliminar(String json) {
        ArrayList<Integer> numeros = parseJson(json);
        ArrayList<Integer> numerosSinCorrimiento = this.valoresUnicos;

        //Verificar que los numeros ingresados (sin corrimiento) tengan valores entre 1 y 13
        for (int i = 0; i < numerosSinCorrimiento.size(); i++){
            if (numerosSinCorrimiento.get(i) > 13 || numerosSinCorrimiento.get(i) < 0){
                return 400;
            }
        }
        //Verificar que los numeros ingresados (sin corrimiento) sumen 13 
        if (sumaValoresEsTrece(numerosSinCorrimiento)) {
            for (int i = 0; i < numeros.size(); i++) {
                if (!arbol.existeNodo(numeros.get(i), arbol.getNodoRaiz())) {
                    return 404;
                }
                Nodo nodo = arbol.buscarNodo(numeros.get(i), arbol.getNodoRaiz());
                if (!arbol.esHoja(nodo)) {
                    return 409;
                }
                int eliminado = arbol.eliminarNodo(numeros.get(i), arbol.getNodoRaiz(), arbol.getNodoRaiz());
            }
            return 200;
        } else {
            return 406;
        }
    }

    /*
    *   Sub-Metodos que manejan varias funcionalidades en base al nivel del arbol
    */
    //Actualizar nivel del arbol
    public void actualizarNivelCarta() {
        arbol.asignarNivelNodo(arbol.getNodoRaiz());
    }

    //Metodo General para buscar valores dentro del arbol AVL
    public void buscarCartaPorNivel(int nivel) {
        nodosPorNivel = new ArrayList<>();
        arbol.buscarNodoPorNivel(nodosPorNivel, arbol.getNodoRaiz(), nivel);
    }

    //Metodo General que retorna los nodos obtenidos por nivel
    public String cartasObtenidasPorNivel() {
        String json = "{\n";

        for (int i = 0; i < this.nodosPorNivel.size(); i++) {
            json += "\"" + i + "\":\"" + nodosPorNivel.get(i).getValorUnico() + nodosPorNivel.get(i).getSimbolo() + "\"\n";
        }
        json += "}";

        return json;
    }

    /*
    *   Sub-Metodos que manejan las Letras admitidas 
    */
    //Metodo para verificar si el carcater es una Letra admitida
    public boolean esLetraAdmitida(char caracter) {
        return (caracter == 'J' || caracter == 'Q' || caracter == 'K' || caracter == 'A');
    }

    //Metodo para obtener el valor que es representado por la Letra  y retornarlo
    public int obtenerValorLetra(char caracter) {

        if (caracter == 'A') {
            return 1;
        }
        if (caracter == 'J') {
            return 11;
        }
        if (caracter == 'Q') {
            return 12;
        }
        if (caracter == 'K') {
            return 13;
        }
        return 0;
    }

    //Metodo para agregar corrimiento correspondiente al carcater evaluando y retornar su corrimiento
    public int agregarCorrimiento(char caracter, ArrayList<String> simbolos) {

        if (caracter == '♣') {
            simbolos.add(String.valueOf(caracter));
            return 0;
        }
        if (caracter == '♥') {
            simbolos.add(String.valueOf(caracter));
            return 40;
        }
        if (caracter == '♦') {
            simbolos.add(String.valueOf(caracter));
            return 20;
        }
        if (caracter == '♠') {
            simbolos.add(String.valueOf(caracter));
            return 60;
        }

        return 0;
    }

    /*
    *   Metodos de ordenamiento de Cartas
    */
    public String enOrden() {
        String texto = "";
        nodosOrdenados = new ArrayList<>();
        arbol.enOrden(nodosOrdenados);
        texto += "{\n";

        for (int i = 0; i < nodosOrdenados.size(); i++) {
            texto += "\"" + i + "\":" + "\"" + nodosOrdenados.get(i) + "\"\n";
        }
        texto += "}";
        return texto;
    }

    public String preOrden() {
        String texto = "";
        nodosOrdenados = new ArrayList<>();
        arbol.preOrden(nodosOrdenados);
        texto += "{\n";
        for (int i = 0; i < nodosOrdenados.size(); i++) {
            texto += "\"" + i + "\":" + "\"" + nodosOrdenados.get(i) + "\"\n";
        }
        texto += "}";

        return texto;
    }

    public String postOrden() {
        String texto = "";
        nodosOrdenados = new ArrayList<>();
        arbol.postOrden(nodosOrdenados);
        texto += "{\n";
        for (int i = 0; i < nodosOrdenados.size(); i++) {
            texto += "\"" + i + "\":" + "\"" + nodosOrdenados.get(i) + "\"\n";
        }
        texto += "}";

        return texto;
    }

    /*
    *   Sub-Metodo sobre el manejo de eliminacion de cartas
    */
    //Metodo para verificar si los valores de las cartas suman 13
    public boolean sumaValoresEsTrece(ArrayList<Integer> valores) {
        int sumatoria = 0; //variable que almaceara la sumatoria de valores de 2 cartas o 1 en el caso de K

        for (int i = 0; i < valores.size(); i++) { //Sumara la misma cantidad de veces asi como el tamanio del Arreglo de valores que se pase
            sumatoria += valores.get(i);
        }
        return (sumatoria == 13);
    }

    /*
    *   Metodo General para obtener la graica del estado del arbol
    */
    //Metodo General que invoca al metodo dibujar (he)
    public String generarGrafica() {

        return arbol.dibujarArbol();
    }
}
