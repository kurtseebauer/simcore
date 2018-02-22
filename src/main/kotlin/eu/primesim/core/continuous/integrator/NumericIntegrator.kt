package eu.primesim.core.continuous.integrator

interface DifferentialEquation {
    fun dy(x: Double) : Double
}

abstract class NumericIntegrator(val equation: DifferentialEquation, val timestep: Double) {
    var sum: Double = 0.0
    abstract fun next(x: Double)
}

class Euler(equation: DifferentialEquation, timestep: Double) : NumericIntegrator(equation, timestep) {
    override fun next(x: Double) {
        sum += timestep * equation.dy(x)
    }
}