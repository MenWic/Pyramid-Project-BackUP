package com.example.pyramid.controllers;

import com.example.pyramid.services.JuegoPyramid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controlador {

    JuegoPyramid juego = new JuegoPyramid();

    /*
     * Peticiones: GET
     */
    @GetMapping("/")
    public String bienvenida() {

        return " Hola, Bienvenido a Pyramid (API)!";
    }

    @GetMapping("Game/get-level") // ("/Game/get-level?")
    public String obtenerCartasDeNivel(@RequestParam(value = "level", defaultValue = "0") int nivel) {
        int nivelConsultado = nivel;
        juego.buscarCartaPorNivel(nivelConsultado); //Metodo que accede al nivel del arbol pedido
        String response = juego.cartasObtenidasPorNivel(); //Devolver nodos izq-der del nivel pedido en formato JSON

        return response; //System.out.println(" Imprimir cartas del nivel pedido: " + nivel);
    }

    @GetMapping("Game/status-avltree")
    public String obtenerEstadoArbol(@RequestParam(value = "status", defaultValue = "") String orden) {
        String ruta = "{\n" + "\"path\":\"" + juego.generarGrafica() + "\"\n}";

        return ruta; //System.out.println(" Devolver path");
    }

    @GetMapping("Game/avltree") // ("Game/avltree?")
    public String obtenerOrdenArbol(@RequestParam(value = "transversal", defaultValue = "inOrder") String orden) {
        String response = "";

        if (orden.equals("inOrder")) {
            response = juego.enOrden(); //Devolver nodos de orden pedido en formato JSON
            //System.out.println(" Imprimir cartas del arbol InOrden");
        }
        if (orden.equals("preOrder")) {
            response = juego.preOrden(); //Devolver nodos de orden pedido en formato JSON
            //System.out.println(" Imprimir cartas del arbol preOrden");
        }
        if (orden.equals("postOrder")) {
            response = juego.postOrden(); //Devolver nodos de orden pedido en formato JSON
            //System.out.println(" Imprimir cartas del arbol PostOrden");
        }

        return response; //System.out.println(" Imprimir cartas  en el orden pedido: " + orden);
    }

    /*
     * Peticiones: POST
     */
    @PostMapping("Game/start")
    public ResponseEntity<String> iniciarJuego(@RequestBody String json) {
        int estado = juego.iniciarPyramid(json); //Inserta cartas (nodos) 1 por 1 dentro de un arbol AVL

        if(estado == 400){
            return new ResponseEntity<>(" Al menos 1 carta a insertar, tiene valor inaceptable", HttpStatus.BAD_REQUEST);
        }
        if(estado == 406){
            return new ResponseEntity<>(" Al menos 1 carta a insertar, esta duplicada", HttpStatus.NOT_ACCEPTABLE);
        }

        juego.actualizarNivelCarta();
        
        return new ResponseEntity<>(" Iniciando Pyramid ... Inserción de carta(s) exitosa!", HttpStatus.OK);
    }

    @PostMapping("Game/add")
    public ResponseEntity<String> insertarArbol(@RequestBody String json) {
        int estado = juego.insertarCarta(json);

        if(estado == 406){
            return new ResponseEntity<>(" La carta a insertar, esta duplicada", HttpStatus.NOT_ACCEPTABLE);
        }
        if(estado == 400){
            return new ResponseEntity<>(" La carta a insertar, tiene valor inaceptable", HttpStatus.BAD_REQUEST);
        }

        juego.actualizarNivelCarta();
       
        return new ResponseEntity<>(" Inserción de carta(s) exitosa!", HttpStatus.OK);
    }

    /*
     * Peticiones: DELETE
     */
    @DeleteMapping("Game/delete")
    public ResponseEntity<String> eliminarArbol(@RequestBody String json) {
        int estado = juego.eliminar(json);

        if(estado == 400){
            return new ResponseEntity<>(" Al menos 1 carta, tiene valor inaceptable", HttpStatus.BAD_REQUEST);
        }
        if (estado == 404) {
            return new ResponseEntity<>(" Al menos 1 carta, no se encuentra en el árbol avl (eliminar)", HttpStatus.NOT_FOUND);
        }

        if (estado == 406) {
            return new ResponseEntity<>(" Los valores de las cartas no suman 13 (ni es K)", HttpStatus.NOT_ACCEPTABLE);
        }

        if (estado == 409) {
            return new ResponseEntity<>(" Las cartas no se puede eliminar ya que al menos 1, cuenta con hijo(s)", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(" Eliminacion de carta(s) exitosa!", HttpStatus.OK);
    }
}
