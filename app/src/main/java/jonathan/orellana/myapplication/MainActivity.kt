package jonathan.orellana.myapplication

import RecyclerViewHelper.Adaptador
import android.os.Bundle
import android.widget.Adapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.claseConexion
import modelo.classMascotas

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Mandar a llamar a todos los elementos
        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtPeso = findViewById<EditText>(R.id.txtPeso)
        val txtEdad = findViewById<EditText>(R.id.txtEdad)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val rcvMascotas = findViewById<RecyclerView>(R.id.rcvMascotas)

        //Primer paso para mostrar datos, asignar layout al recyclerview
        rcvMascotas.layoutManager = LinearLayoutManager(this)

        //TODO: MOSTRAR DATOS//
        //función para mostrar datos

        fun obtenerDatos(): List<classMascotas>{

            //crear objeto conexion

            val objConexion = claseConexion().cadenaConexion()

            //crear statement

            val statement = objConexion?.createStatement()
            val resulSet = statement?.executeQuery("select * from tbMascotas")!!
            val mascotas = mutableListOf<classMascotas>()

            //recorro todos los registos de la base de datos

            while(resulSet.next()){
                val nombre = resulSet.getString("nombreMascota")
                val mascota = classMascotas(nombre)
                mascotas.add(mascota)
            }

            return mascotas
        }

        //Asignar adaptador a RecyclerView
        CoroutineScope(Dispatchers.IO).launch {
            val mascotasDB = obtenerDatos()
            withContext(Dispatchers.Main){
                val adapter = Adaptador(mascotasDB)
                rcvMascotas.adapter= adapter
            }
        }

        //cabal

        //Programar botón agregar
        btnAgregar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                //Crear objeto clase conecion
                val objConexion = claseConexion().cadenaConexion()

                //Crear variable que contenga PrepareStatement
                val addMascota = objConexion?.prepareStatement("insert into tbMascotas values (?, ?, ?)")!!
                addMascota.setString(1,txtNombre.text.toString())
                addMascota.setInt(2, txtPeso.text.toString().toInt())
                addMascota.setInt(3, txtEdad.text.toString().toInt())
                addMascota.executeUpdate()
                println("saeadfasdfasdfasdfasdf")

                //Refresca la tabla
                val nuevasMascotas = obtenerDatos()
                withContext(Dispatchers.Main){
                    (rcvMascotas.adapter as? Adaptador)?.ActualizarLista(nuevasMascotas)
                }
            }
        }
    }
}