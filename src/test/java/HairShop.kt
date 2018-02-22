import eu.primesim.core.*
import org.junit.jupiter.api.Test
import java.util.*

class HairShopTest {

    @Test
    fun hairShop() {
        val queue = TreeSetEventQueue()
        queue.scheduleEvent(PrintEvent(0, "simulation starts at time 0"))

        // first customer appears 5 minutes after we open the shop
        // subsequent customers are scheduled after each customer gets the chair
        // so our event queue only needs to know the next arrival
        queue.scheduleEvent(HairShop.CustomerArrivalEvent(5, HairShop()))

        // we stop the simulation at time 600 (10h)
        queue.scheduleEvent(SimulationPauseEvent(600))

        DiscreteEventSimulator(queue).start()
    }
}

/**
 * HairShop is our Model
 */
class HairShop {

    /**
     * our revenue for the day
     */
    var cashbox = 0.0

    /**
     * we serve the customers first in first out
     */
    var waitingroom: Deque<Customer> = LinkedList<Customer>()

    /**
     * we only have one seat that can have a customer or not.
     */
    var chair: Customer? = null

    /**
     * new customer arrives. after that, the arrival of the next customer is scheduled
     */
    class CustomerArrivalEvent(time: Int, var model: HairShop) : Event(time) {
        private val customerArrivalInterval = 20.0
        private val customerArrivalStdDeviation = 10.0
        override fun execute() {
            println("$time: customer arrived at our shop")
            model.waitingroom.addLast(Customer(time.toString()))
            val nextArrivalTime = Math.max(5, RandomDistributions().normalDistribution(customerArrivalInterval, customerArrivalStdDeviation).toInt())
            simulator?.scheduleEvent(
                    CustomerArrivalEvent(time + nextArrivalTime, model))

            simulator?.scheduleEvent(NextCustomerEvent(time, model))
        }
    }

    /**
     * this event is scheduled when a customer arrives (and executed if seat is empty) and after a customer leaves
     */
    class NextCustomerEvent(time: Int, var model: HairShop) : Event(time) {
        private val customerServiceInterval = 30.0
        private val customerServiceStdDeviation = 15.0

        override fun execute() {
            if (model.chair != null) {
                // println("cannot serve next customer because seat is taken")
                return
            }
            if (model.waitingroom.isEmpty()) {
                println("waiting room is empty, nothing to do")
            }
            var nextCustomer = model.waitingroom.removeFirst()

            val serviceTime = Math.max(5, RandomDistributions().normalDistribution(customerServiceInterval, customerServiceStdDeviation).toInt())

            simulator?.scheduleEvent(CustomerLeavingEvent(time + serviceTime, model))
            model.chair = nextCustomer
        }
    }


    /**
     * customer is done with her haircut, pays and leaves.
     * NextCustomerEvent is scheduled at this moment so the next customer from the waiting room can take the seat
     */
    class CustomerLeavingEvent(time: Int, var model: HairShop) : Event(time) {
        override fun execute() {
            println("customer ${model.chair?.name} is leaving our shop at $time")
            model.chair = null
            model.cashbox += 20

            println("time: $time, cash box: ${model.cashbox}, waiting room: ${model.waitingroom.size}")
            simulator?.scheduleEvent(NextCustomerEvent(time, model))
        }
    }

    /**
     * customer is a pseudo object that has only a name (the arrival time)
     */
    data class Customer(val name: String)
}