package eu.primesim.core.continuous

import eu.primesim.core.continuous.integrator.DifferentialEquation
import eu.primesim.core.continuous.integrator.Euler
import org.junit.jupiter.api.Test

class DifferentialEquationTest {

    @Test
    fun integrateLinear() {

        var eq = Line()
        val timestep = 0.001
        val integrator = Euler(eq, timestep)

        var x = 0.0
        while (x < 10.0) {
            integrator.next(x)
            x += timestep
        }

        // analytic solution: (10*10)/2=50
        println(integrator.sum)
    }

    @Test
    fun integrateConstant() {

        var eq = Constant()
        val timestep = 0.001
        val integrator = Euler(eq, timestep)

        var x = 0.0
        while (x < 10.0) {
            integrator.next(x)
            x += timestep
        }
        // analytic solution: 10*1/1 = 10
        println(integrator.sum)
    }

    @Test
    fun integrateSquare() {

        var eq = Square()
        val timestep = 0.001
        val integrator = Euler(eq, timestep)

        var x = 0.0
        while (x < 10.0) {
            integrator.next(x)
            x += timestep
        }
        // analytic solution: 10^3/3 = 333.33
        println(integrator.sum)
    }

    /**
     * dy = x
     */
    class Line : DifferentialEquation {
        override fun dy(x: Double): Double {
            return x
        }
    }

    /**
     * dy = 1
     */
    class Constant : DifferentialEquation {
        override fun dy(x: Double): Double {
            return 1.0
        }
    }

    /**
     * dy = x^2
     */
    class Square : DifferentialEquation {
        override fun dy(x: Double): Double {
            return x * x
        }
    }
}