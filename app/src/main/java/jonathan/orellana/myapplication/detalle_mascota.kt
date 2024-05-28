package jonathan.orellana.myapplication

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class detalle_mascota : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_mascota)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Recibir valores
        val UUIDRecibido = intent.getStringExtra("MascotaUUID")
        val nombreRecibido = intent.getStringExtra("NombreMascota")
        val edadRecibido = intent.getIntExtra("Edad", 0)
        val pesoRecibido = intent.getIntExtra("Peso", 0)

        //mandar elementos de la pantalla
        val txtUUIDdetalle = findViewById<TextView>(R.id.txtUUIDdetalle)
        val txtNombreDetalle = findViewById<TextView>(R.id.txtNombreDetalle)
        val txtEdadDetalle = findViewById<TextView>(R.id.txtEdadDetalle)
        val txtPesoDetalle = findViewById<TextView>(R.id.txtPesoDetalle)

        //asignar datos recibidos  a mis textviews
        txtUUIDdetalle.text = UUIDRecibido
        txtNombreDetalle.text = nombreRecibido
        txtEdadDetalle.text = edadRecibido.toString()
        txtPesoDetalle.text = pesoRecibido.toString()

    }
}