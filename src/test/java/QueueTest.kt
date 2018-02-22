import eu.primesim.core.*
import org.junit.jupiter.api.Test
import java.util.*

class QueueTest {
    @Test
    fun addToQueue() {
        val queue = TreeSetEventQueue()
        val printHelloEvent = PrintEvent(0, "hello ")
        queue.scheduleEvent(printHelloEvent)
        assert(queue.queue.size == 1)
    }

    @Test
    fun treeSetTest() {
        val treeSet = TreeSet<Event>()
        treeSet.add(PrintEvent(1, "hello"))
        treeSet.add(PrintEvent(2, " world"))
        assert(treeSet.size == 2)
    }

    @Test
    fun queueOrdered() {
        val queue = TreeSetEventQueue()
        var e1 = PrintEvent(2, "world!")
        var e2 = PrintEvent(5, "hello ")
        queue.scheduleEvent(e1)
        queue.scheduleEvent(e2)

        assert(queue.queue.size == 2)
        var next = queue.getNext()
        assert(next == e2)
        assert(queue.queue.size == 1)
        next = queue.getNext()
        assert(next == e1)
        assert(queue.queue.size == 0)
    }

    @Test
    fun multipleEventsAtSameTime() {
        val queue = TreeSetEventQueue()
        var e1 = PrintEvent(2, "world!")
        var e2 = PrintEvent(2, "world!")
        queue.scheduleEvent(e1)
        queue.scheduleEvent(e2)
        assert(queue.queue.size == 2)
    }

}

class SimulatorTest {

    @Test
    fun runSimulator() {
        val queue = TreeSetEventQueue()
        var e1 = PrintEvent(1, "hello ")
        var e2 = PrintEvent(2, "world!")
        queue.scheduleEvent(e1)
        queue.scheduleEvent(e2)
        queue.scheduleEvent(SimulationPauseEvent(3))

        var simulator = DiscreteEventSimulator(queue)
        simulator.start()

        println("done")
    }

}


