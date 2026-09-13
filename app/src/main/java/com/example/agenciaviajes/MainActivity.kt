package com.example.agenciaviajes

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agenciaviajes.model.Destino
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DestinoAdapter
    private val listaDestinos = mutableListOf<Destino>()

    companion object {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val refDestinos: DatabaseReference = database.getReference("destinos")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.catalogo_titulo)

        recyclerView = findViewById(R.id.recyclerDestinos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = DestinoAdapter(
            listaDestinos,
            onEditClick = { destino ->
                val intent = Intent(this, DestinoFormActivity::class.java)
                intent.putExtra("key", destino.key)
                startActivity(intent)
            },
            onDeleteClick = { destino ->
                confirmarEliminacion(destino)
            }
        )
        recyclerView.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabAgregar).setOnClickListener {
            startActivity(Intent(this, DestinoFormActivity::class.java))
        }

        escucharCambios()
    }

    private fun escucharCambios() {
        refDestinos.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaDestinos.clear()
                for (item in snapshot.children) {
                    val destino = item.getValue(Destino::class.java)
                    if (destino != null) {
                        destino.key = item.key
                        listaDestinos.add(destino)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun confirmarEliminacion(destino: Destino) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirmacion_titulo))
            .setMessage(getString(R.string.confirmacion_eliminar))
            .setPositiveButton(getString(R.string.si)) { _, _ ->
                destino.key?.let { refDestinos.child(it).removeValue() }
                Toast.makeText(this, getString(R.string.registro_eliminado), Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_sign_out) {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}