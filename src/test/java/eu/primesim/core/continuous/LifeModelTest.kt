package eu.primesim.core.continuous

import eu.primesim.core.continuous.integrator.DifferentialEquation
import eu.primesim.core.discrete.Event
import eu.primesim.core.discrete.PrintEvent
import eu.primesim.core.discrete.TreeSetEventQueue
import org.junit.jupiter.api.Test

class LifeModelTest {

    @Test
    fun doLifeModel(){

        var queue = TreeSetEventQueue()
        var model = LifeModel()
        var sim = ContinuousSimulator(0.0, 12.0, 0.01, 0.0, model, queue)
        sim.scheduleEvent(PrintEvent(0,"starting simulation at time 0"))
        sim.scheduleEvent(LifeModel.AnimationEvent(1, model))
        sim.start()

    }
}


class LifeModel : Model() {

    var initialPredator = 20
    var initialPrey = 200

    init {
        val prey = Prey()
        prey.value= 200.0
        val predator = Predator()
        predator.value = 20.0
        predator.prey = prey
        prey.predator = predator
        differentialEquations.add(prey)
        differentialEquations.add(predator)
    }


    class Prey : DifferentialEquation() {
        var predator : Predator? = null
        val growthRatePerYear = 1.3
        val deathRatePerPredator = 0.4

        override fun dy(x: Double): Double {
            return growthRatePerYear * value - deathRatePerPredator * predator!!.value
        }
    }

    class Predator : DifferentialEquation() {
        var prey : Prey? = null
        val growthRatePerPrey = 0.05
        val deathRatePerYear = 0.4

        override fun dy(x: Double): Double {
            return -deathRatePerYear * value + growthRatePerPrey * prey!!.value
        }
    }

    class AnimationEvent(time: Int, val model: Model) : Event(time) {
        override fun execute() {
            for (eq in model.differentialEquations)
            {
                println("${eq.javaClass}: ${eq.value}")
            }
            simulator?.scheduleEvent(AnimationEvent(time + 1, model))
        }
    }
}
