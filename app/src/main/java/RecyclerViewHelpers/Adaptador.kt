package RecyclerViewHelper

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jonathan.orellana.myapplication.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.claseConexion
import modelo.classMascotas


class Adaptador(private var Datos: List<classMascotas>) : RecyclerView.Adapter<ViewHolder>() {

    fun ActualizarLista(nuevaLista: List <classMascotas> ){
        Datos = nuevaLista
        notifyDataSetChanged()//notifica al recycle que hay datos nuevos
    }

    //////// TODO: Eliminar datos
    fun eliminarDatos(nombreMascota: String, posicion: Int){

        //Actualizar lista de datos y notificar al adaptador
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO) {
            //crear objeto clase conexion
            val objConexion = claseConexion().cadenaConexion()

            //crear variable con prepare statement
            val deleteMascota =objConexion?.prepareStatement("delete from tbMascotas where nombreMascota = ?")!!
            deleteMascota.setString(1, nombreMascota)
            deleteMascota.executeUpdate()

            val commit = objConexion?.prepareStatement("commit")!!
            commit.executeUpdate()
        }

        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
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

            //Botones

            builder.setPositiveButton("si") { dialog, which ->

            }

            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

    }
}

