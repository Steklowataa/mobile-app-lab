package pl.wsei.pam.lab06

import android.app.Application
import pl.wsei.pam.lab06.data.AppContainer
import pl.wsei.pam.lab06.data.AppDataContainer
class TodoApplication : Application() {

    // Lateinit oznacza, że zainicjalizujemy to w onCreate
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        // Tworzymy kontener i przekazujemy mu kontekst całej aplikacji
        container = AppDataContainer(this)
    }
}