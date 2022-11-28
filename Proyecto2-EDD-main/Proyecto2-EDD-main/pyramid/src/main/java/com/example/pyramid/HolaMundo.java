package com.example.pyramid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//Paso 1: Crear la clase xD

//Paso 2: Crear el RestController que se encarga de manejar la clase
//@RestController
public class HolaMundo {

    //Paso 3: Crear el RequestMapping de lo que se mostrara en determinada ruta "localhost:[puerto]/"
    @RequestMapping("/")
    public String hola(){
        return "Hola mundo!";
    }
    
}
