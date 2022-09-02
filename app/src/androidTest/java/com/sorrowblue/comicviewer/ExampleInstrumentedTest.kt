package com.sorrowblue.comicviewer

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import java.security.MessageDigest
import java.security.Provider
import java.security.Security
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val providers: Array<Provider> = Security.getProviders()
        for (provider in providers) {
            showHashAlgorithms(provider, MessageDigest::class.java)
        }
    }
}

fun showHashAlgorithms(prov: Provider, typeClass: Class<*>) {
    val type = typeClass.simpleName
    val algos: MutableList<Provider.Service> = ArrayList()
    val services: Set<Provider.Service> = prov.services
    for (service in services) {
        if (service.type!!.contentEquals(type)) {
            algos.add(service)
        }
    }
    if (!algos.isEmpty()) {
        println(" --- Provider ${prov.name}, version ${prov.version}f ---")
        for (service in algos) {
            val algo: String = service.algorithm
            println("Algorithm name: \"${algo}\"")
        }
    }

    // --- find aliases (inefficiently)
    val keys = prov.keys()
    for (key in keys) {
        val prefix = "Alg.Alias.$type."
        if (key.toString().startsWith(prefix)) {
            val value: String = prov.get(key.toString()).toString()
            println("Alias: \"${key.toString().substring(prefix.length)}\" -> \"${value}\"")
        }
    }
}
