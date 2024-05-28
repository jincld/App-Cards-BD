package RecyclerViewHelper

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import jonathan.orellana.myapplication.R
import jonathan.orellana.myapplication.detalle_mascota
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.claseConexion
import modelo.dataClassMascotas



class Adaptador(private var Datos: List<dataClassMascotas>) : RecyclerView.Adapter<ViewHolder>() {

    fun ActualizarLista(nuevaLista: List<dataClassMascotas>) {
        Datos = nuevaLista
        notifyDataSetChanged()//notifica al recycle que hay datos nuevos
    }

    fun actualizarPantalla(uuid: String, nuevoNombre: String){
        val index = Datos.indexOfFirst { it.uuid == uuid }
        Datos[index].nombreMascota = nuevoNombre
        notifyDataSetChanged()
    }

    //////// TODO: Eliminar datos
    fun eliminarDatos(nombreMascota: String, posicion: Int) {

        //Actualizar lista de datos y notificar al adaptador
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO) {
            //crear objeto clase conexion
            val objConexion = claseConexion().cadenaConexion()

            //crear variable con prepare statement
            val deleteMascota =
                objConexion?.prepareStatement("delete from tbMascotas where nombreMascota = ?")!!
            deleteMascota.setString(1, nombreMascota)
            deleteMascota.executeUpdate()

            val commit = objConexion?.prepareStatement("commit")!!
            commit.executeUpdate()
        }

        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()

    }

    ////////TODO: EDITAR DATOS
    fun updateMascota(nuevoNombre: String, uuid: String) {
        GlobalScope.launch(Dispatchers.IO) {
            //crear objeto de clase conexion
            val objConexion = claseConexion().cadenaConexion()

            //crear variable con prepare statement
            val updateMascota =
                objConexion?.prepareStatement("update tbMascotas set nombreMascota = ? where uuid = ?")!!
            updateMascota.setString(1, nuevoNombre)
            updateMascota.setString(2, uuid)
            updateMascota.executeUpdate()

            withContext(Dispatchers.Main){
                actualizarPantalla(uuid, nuevoNombre)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)

        return ViewHolder(vista)
    }
    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mascota = Datos[position]
        holder.textView.text = mascota.nombreMascota

        //todo: click icono eliminar
        holder.btnBorrar.setOnClickListener() {

            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Desea eliminar la mascota?")

            //Botones

            builder.setPositiveButton("si") { dialog, which ->
                eliminarDatos(mascota.nombreMascota, position)
            }

            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }
        //todo: click icono editar
        holder.btnEditar.setOnClickListener() {

            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Editar")
            builder.setMessage("¿Desea editar la mascota?")

            //Cuadro de texto
            val cuadroTexto = EditText(context)
            cuadroTexto.setHint(mascota.nombreMascota)
            builder.setView(cuadroTexto)

            //Botones

            builder.setPositiveButton("si") { dialog, which ->
                updateMascota(cuadroTexto.text.toString(), mascota.uuid)
            }

            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        //TODO: CLICK A LA CARD PARA MOSTRAR DETALLE
        holder.itemView.setOnClickListener {

            val context = holder.itemView.context

            //cambiar a pantalla de detalle
            val pantallaDetalle = Intent(context, detalle_mascota :: class.java)
            //enviar valores a la otra pantalla
            pantallaDetalle.putExtra("MascotaUUID", mascota.uuid)
            pantallaDetalle.putExtra("NombreMascota", mascota.nombreMascota)
            pantallaDetalle.putExtra("Edad", mascota.edad)
            pantallaDetalle.putExtra("Peso", mascota.peso)
            context.startActivity(pantallaDetalle)
        }

    }
}

