package eu.primesim.core.discrete

import java.util.NoSuchElementException
import java.util.SortedSet
import java.util.TreeSet

/**
 * building block of discrete event simulation.
 *
 * We can schedule events and get the next event.
 */
interface EventQueue {
    /**
     * add event to the event queue. the event knows its scheduled time.
     */
    fun scheduleEvent(event: Event)

    /**
     * get and remove the next (lowest time then first inserted) event.
     */
    fun getNext(): Event?
}

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

    /**
     * schedule new Event
     */
    fun scheduleEvent(event: Event)
}

/**
 * Implementation of the EventQueue using a java.util.TreeSet
 */
class TreeSetEventQueue : EventQueue {
    val queue: SortedSet<Event> = TreeSet<Event>()
    var counter = 0

    override fun scheduleEvent(event: Event) {
       // println("-->scheduling new event: ${event.javaClass.name} at ${event.time}")
        event.id = counter++
        queue.add(event)
    }

    override fun getNext(): Event? {
        try {
            val event = queue.first()
            queue.remove(event)
            // println("get next event: ${event.javaClass.name}")
            return event
        } catch (e: NoSuchElementException) {
            println("no more events in the queue")
            return null
        }
    }
}

abstract class Event(open var time: Int) : Comparable<Event> {

    /**
     * the event needs an id besides the timestamp, because the queue cannot insert multiple items that are equal.
     */
    var id = 0

    /**
     * some events need to know the simulator (e.g. the simulation-stop event) or have to schedule new events..
     */
    var simulator: Simulator? = null

    abstract fun execute()

    /**
     * lower times come first, if equal, compare by id
     */
    override fun compareTo(other: Event): Int {
        var result = this.time - other.time
        if (result == 0) {
            // events have been inserted at the same time, so the lowest id wins
            result = this.id - other.id
        }
        return result
    }

    /**
     * equals uses time and id
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event

        if (time != other.time) return false
        if (id != other.id) return false

        return true
    }

    /**
     * hashCode implementation by time and id
     */
    override fun hashCode(): Int {
        var result = time
        result = 31 * result + id
        return result
    }

}

/**
 * basic event that prints a string to stdout
 */
class PrintEvent(time: Int, private val string: String) : Event(time) {
    override fun execute() = println(string)
}

class SimulationPauseEvent(time: Int) : Event(time) {
    override fun execute() {
        simulator?.pause()
    }
}

class DiscreteEventSimulator(private val eventQueue: EventQueue) : Simulator {
    override fun scheduleEvent(event: Event) {
        eventQueue.scheduleEvent(event)
    }

    var paused: Boolean = false

    override fun start() {
        println("starting simulator")
        while (!paused) {
            val event = eventQueue.getNext()

            if (event != null) {
                event.simulator = this
                event.execute()
            } else {
                // we reached the last event and have nothing more left to do than leaving the execution
                break
            }
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