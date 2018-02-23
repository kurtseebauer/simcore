package eu.primesim.core.continuous

import eu.primesim.core.continuous.integrator.DifferentialEquation
import eu.primesim.core.continuous.integrator.Euler
import eu.primesim.core.discrete.Event
import eu.primesim.core.discrete.EventQueue
import eu.primesim.core.discrete.Simulator


class ContinuousSimulator(val start: Double, val stop: Double, var timestep: Double, var time: Double, var model: Model, var eventQueue: EventQueue) : Simulator {

    var paused = false

    override fun start() {
        while (!paused && time < stop) {
            var event = eventQueue.getNext()
            var nextEventTime = event?.time ?: Int.MAX_VALUE

            while(event != null && time < event.time) {

                model.differentialEquations.forEach {
                    // we should not jump over the event, reduce timestep if necessary
                    Euler(it, timestep).next(time + timestep)
                    time += timestep
                }
            }
            // after we did the equations, we execute the events
            if (event == null){
                break
            } else if (time >= event.time.toDouble()){
                event.execute()
            }

            println("current time: $time")
        }
        println ("stop time reached")
    }

    override fun pause() {
        paused = true
    }

    override fun resume() {
        paused = false
        start()
    }

    override fun scheduleEvent(event: Event) {
        event.simulator = this
        eventQueue.scheduleEvent(event)
    }
}

abstract class Model {
    open var differentialEquations: ArrayList<DifferentialEquation> = arrayListOf()
}



