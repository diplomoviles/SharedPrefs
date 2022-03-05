package com.tallercmovil.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.material.snackbar.Snackbar
import com.tallercmovil.sharedprefs.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var navHostFragment: Fragment

    private lateinit var encrypted_sp: SharedPreferences
    private lateinit var encrypted_spEditor: SharedPreferences.Editor
    private lateinit var usuario_sp: String
    private lateinit var contrasenia_sp: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val masterKey = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        encrypted_sp = EncryptedSharedPreferences.create(this, "encrypted_sp", masterKey
            , EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV
            , EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

        encrypted_spEditor = encrypted_sp.edit()


        val user = encrypted_sp.getString("usuario_sp", null)

        if(user != null){
            Toast.makeText(this, "El usuario encriptado almacenado es: $user", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "No hay ningún usuario registrado", Toast.LENGTH_LONG).show()
        }


        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as Fragment

        //navHostFragment.view?.setBackgroundColor(Color.BLUE)
        //navHostFragment.view?.setBackgroundColor(Color.rgb(100,30, 40))
        //navHostFragment.view?.setBackgroundColor(ContextCompat.getColor(this, R.color.mi_azul))

        val color = getPreferences(Context.MODE_PRIVATE).getInt("color", R.color.white)
        cambiaColor(color)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_azul ->{
                cambiaColor(R.color.mi_azul)
                true
            }
            R.id.action_rojo -> {
                cambiaColor(R.color.mi_rojo)
                true
            }
            R.id.action_verde -> {
                cambiaColor(R.color.mi_verde)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun cambiaColor(color: Int){
        navHostFragment.view?.setBackgroundColor(ContextCompat.getColor(this, color))
        guardaColor(color)
    }

    private fun guardaColor(color: Int){
        val sp = getPreferences(Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putInt("color", color).apply()
        //editor.commit()

        encrypted_spEditor.putString("usuario_sp", "Amaury")
        encrypted_spEditor.putString("contrasenia_sp", "password1234")
        encrypted_spEditor.apply()
    }
}