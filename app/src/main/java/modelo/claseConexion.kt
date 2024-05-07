package modelo

import java.sql.Connection
import java.sql.DriverManager

class claseConexion {
    fun cadenaConexion(): Connection?{

        try{
            val ip = "jdbc:oracle:thin:@10.10.0.144:1521:xe"
            val usuario = "system"
            val contrasena = "desarrollo"

            val connection = DriverManager.getConnection(ip, usuario, contrasena)
            return connection
        }catch (error:Exception){
            println("Este es el error: $error")
            return null
        }
    }
}