package es.utad.videoclub.activities

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import es.utad.videoclub.R
import es.utad.videoclub.fragments.FichaPeliculaFragment
import es.utad.videoclub.fragments.ListaPeliculasFragment

class CatalogoActivity : AppCompatActivity() {

    var frameLayoutLista: FrameLayout? = null
    var frameLayoutFicha: FrameLayout? = null
    var frameLayoutFragment: FrameLayout? = null
    var listaFragment: ListaPeliculasFragment? = null
    var fichaFragment: FichaPeliculaFragment? = null
    var segundoFragmentActivo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo)

        frameLayoutLista = findViewById(R.id.frameLayoutLista)
        frameLayoutFicha = findViewById(R.id.frameLayoutFicha)
        frameLayoutFragment = findViewById(R.id.frameLayoutFragment)

        listaFragment = ListaPeliculasFragment()
        listaFragment!!.activityListener = activityListener
        fichaFragment = FichaPeliculaFragment()

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (frameLayoutFragment == null) {
            // Están definidos los dos espacios para poner los fragment
            fragmentTransaction.add(R.id.frameLayoutLista, listaFragment!!)
            fragmentTransaction.add(R.id.frameLayoutFicha, fichaFragment!!)
        } else {
            fragmentTransaction.add(R.id.frameLayoutFragment, listaFragment!!)
        }
        fragmentTransaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.app_bar_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchItem.collapseActionView()

                val args = Bundle()
                args.putString("titulo", query)

                frameLayoutLista = findViewById(R.id.frameLayoutLista)
                frameLayoutFicha = findViewById(R.id.frameLayoutFicha)
                frameLayoutFragment = findViewById(R.id.frameLayoutFragment)

                frameLayoutLista?.removeAllViewsInLayout()
                frameLayoutLista?.requestLayout()
                frameLayoutLista?.invalidate()

                listaFragment = ListaPeliculasFragment()
                listaFragment!!.activityListener = activityListener
                listaFragment!!.arguments = args
                fichaFragment = FichaPeliculaFragment()

                val fragmentManager = supportFragmentManager
                fragmentManager.popBackStack()
                val fragmentTransaction = fragmentManager.beginTransaction()

                if (frameLayoutFragment == null) {
                    // Están definidos los dos espacios para poner los fragment
                    fragmentTransaction.add(R.id.frameLayoutLista, listaFragment!!)
                    fragmentTransaction.add(R.id.frameLayoutFicha, fichaFragment!!)
                } else {
                    //fragmentTransaction.add(R.id.frameLayoutFragment, listaFragment!!)
                    fragmentTransaction.replace(R.id.frameLayoutFragment, listaFragment!!)
                }
                fragmentTransaction.commit()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val args = Bundle()
                args.putString("titulo", newText)


                frameLayoutLista = findViewById(R.id.frameLayoutLista)
                frameLayoutFicha = findViewById(R.id.frameLayoutFicha)
                frameLayoutFragment = findViewById(R.id.frameLayoutFragment)

                frameLayoutLista?.removeAllViewsInLayout()
                frameLayoutLista?.requestLayout()
                frameLayoutLista?.invalidate()

                listaFragment = ListaPeliculasFragment()
                listaFragment!!.activityListener = activityListener
                listaFragment!!.arguments = args
                fichaFragment = FichaPeliculaFragment()

                val fragmentManager = supportFragmentManager
                fragmentManager.popBackStack()
                val fragmentTransaction = fragmentManager.beginTransaction()

                if (frameLayoutFragment == null) {
                    // Están definidos los dos espacios para poner los fragment
                    fragmentTransaction.add(R.id.frameLayoutLista, listaFragment!!)
                    fragmentTransaction.add(R.id.frameLayoutFicha, fichaFragment!!)
                } else {
                    //fragmentTransaction.add(R.id.frameLayoutFragment, listaFragment!!)
                    fragmentTransaction.replace(R.id.frameLayoutFragment, listaFragment!!)
                }
                fragmentTransaction.commit()

                //Toast.makeText(this@CatalogoActivity, "looking for $newText", Toast.LENGTH_LONG).show()
                return false
            }
        })

        return true
    }


    var activityListener = View.OnClickListener {
        if (frameLayoutFragment != null) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayoutFragment, fichaFragment!!)
            fragmentTransaction.commit()
            fragmentManager.executePendingTransactions()
            segundoFragmentActivo = true
        }
        Toast.makeText(this, listaFragment!!.peliculaSeleccionada.toString(), Toast.LENGTH_LONG).show()
        fichaFragment!!.updateData(listaFragment!!.peliculaSeleccionada)
    }

    override fun onBackPressed() {
        if (segundoFragmentActivo && frameLayoutFragment != null) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayoutFragment, listaFragment!!)
            fragmentTransaction.commit()
            fragmentManager.executePendingTransactions()
            segundoFragmentActivo = false
        } else {
            super.onBackPressed()
        }
    }
}