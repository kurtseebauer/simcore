package eu.primesim.core.continuous

/**
 * Executes an experiment/model
 */
interface Simulator {

    /**
     * start execution
     */
    fun start()

    /**
     * pause execution
     */
    fun pause()

    /**
     * resume execution
     */
    fun resume()
}

class ContinuousSimulator(val start: Double, val stop: Double, var timestep: Double, var time: Double) : Simulator {

    var paused = false

    override fun start() {
        while (!paused && time < stop) {
            time += timestep
            println("current time: $time")
        }
    }

    override fun pause() {
        paused = true
    }

    override fun resume() {
        paused = false
        start()
    }
}