package eu.primesim.core

import java.util.*

class RandomDistributions {

    /**
     * return normal (gaussian) distributed random numbers with mean [mean] and standard deviation [sdv]
     */
    fun normalDistribution(mean: Double, sdv: Double) : Double {
        return Random().nextGaussian() * sdv + mean
    }
}