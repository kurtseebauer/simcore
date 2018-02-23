package eu.primesim.core.continuous.integrator

abstract class DifferentialEquation {
    abstract fun dy(x: Double) : Double
    open var value: Double = 0.0
}

abstract class NumericIntegrator(val equation: DifferentialEquation, val timestep: Double) {
    var sum: Double = 0.0
    abstract fun next(x: Double)
}

class Euler(equation: DifferentialEquation, timestep: Double) : NumericIntegrator(equation, timestep) {
    init {
        sum = equation.value
    }
    override fun next(x: Double) {
        sum += timestep * equation.dy(x)
        equation.value=sum
    }
}