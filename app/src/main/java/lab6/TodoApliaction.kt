package pl.oleksandra.pam.lab06 // Upewnij się, że to pasuje do Twojego Manifestu

import android.app.Application
import pl.oleksandra.pam.lab06.data.AppContainer
import pl.oleksandra.pam.lab06.data.AppDataContainer

class TodoApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        // To jest najważniejsza linia!
        container = AppDataContainer(this)
    }
}