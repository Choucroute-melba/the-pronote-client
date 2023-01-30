package moi.choucroutemelba.thepronoteclient

import android.app.Application
import moi.choucroutemelba.thepronoteclient.data.AppContainer
import moi.choucroutemelba.thepronoteclient.data.AppContainerImpl

class ThePronoteApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}