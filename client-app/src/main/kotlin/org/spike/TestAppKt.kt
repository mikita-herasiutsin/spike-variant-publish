package org.spike

import org.spike.clientapp.v1.client.DefaultApi


class TestAppKt {
    fun executeLogic() {
        println("executeLogic() funciton")
        val defaultApi = DefaultApi::class.java
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val testApp = TestAppKt()
            testApp.executeLogic()
        }
    }
}
