package main

import java.util.*

class KnapsackProblem(val thingList: Array<Thing>, val maxWeight: Int) {

    val totalCost: Int

    init {
        var cost = 0
        for (element in thingList) {
            cost += element.cost
        }
        totalCost = cost
    }

    class Thing(val weight: Int, val cost: Int)

    fun getWeight(mask: Array<Boolean>): Int {
        var weight = 0
        for (i in 0..thingList.lastIndex) {
            if (mask[i]) {
                weight += thingList[i].weight
            }
        }
        return weight
    }

    fun getCost(mask: Array<Boolean>): Int {
        var cost = 0
        for (i in 0..thingList.lastIndex) {
            if (mask[i]) {
                cost += thingList[i].cost
            }
        }
        return cost
    }

    fun knapsack(countOfMutations: Int): Array<Boolean> {
        var mutationCount = countOfMutations
        var generation = randomGeneration()
        var bestMask = generation.maxBy { estimate(it) }!!
        var bestEstimation = estimate(bestMask)
        while (mutationCount > 0 && bestEstimation != 1.0) {
            val estimateOfGeneration = estimate(generation)
            generation = nextGeneration(generation)
            if (estimate(generation) <= estimateOfGeneration) {
                val random = Random()
                val index = random.nextInt(thingList.lastIndex)
                mutationCount--
                generation[index] = mutation(generation[index])
            }
            if (estimate(generation) <= estimateOfGeneration) {
                val random = Random()
                val index = random.nextInt(thingList.lastIndex)
                generation[index] = randomMask()
            }
            val bestOfGeneration = generation.maxBy { estimate(it) }!!
            val estimationOfMask = estimate(bestOfGeneration)
            if (estimationOfMask > bestEstimation) {
                bestMask = bestOfGeneration
                bestEstimation = estimationOfMask
            }
        }
        return bestMask
    }

    private fun randomGeneration(): Array<Array<Boolean>> {
        val generation = Array(thingList.size, { Array(thingList.size, { false }) })
        for (i in 0..thingList.lastIndex) {
            for (j in 0..thingList.lastIndex) {
                val random = Random()
                generation[i][j] = random.nextBoolean()
            }
        }
        return generation
    }

    private fun nextGeneration(generation: Array<Array<Boolean>>): Array<Array<Boolean>> {
        val countOfNeeded = deFactorial(generation.size)
        val nextGeneration = Array(thingList.size, { Array(thingList.size, { false }) })
        val fittest = generation.sortedBy { estimate(it) }.take(countOfNeeded)
        val random = Random()
        for (i in 0..generation.lastIndex) {
            val indexOfFather = random.nextInt(countOfNeeded - 1)
            var indexOfMother = random.nextInt(countOfNeeded - 1)
            while (indexOfFather == indexOfMother) {
                indexOfMother = random.nextInt(countOfNeeded - 1)
            }
            val crossingOver = 1 + random.nextInt(generation.size - 2)
            nextGeneration[i] = arrayOf(
                    *(fittest[indexOfFather].take(crossingOver).toTypedArray()),
                    *(fittest[indexOfMother].takeLast(thingList.size - crossingOver).toTypedArray()))
        }
        return nextGeneration
    }

    private fun randomMask(): Array<Boolean> {
        val random = Random()
        return Array(thingList.size, { random.nextBoolean() })
    }

    private fun mutation(mask: Array<Boolean>): Array<Boolean> {
        val newMask = mask.clone()
        val random = Random()
        val position = random.nextInt(mask.lastIndex)
        newMask[position] = !newMask[position]
        return newMask
    }

    private fun estimate(generation: Array<Array<Boolean>>): Double {
        return generation.sumByDouble { estimate(it) } / thingList.size
    }

    private fun estimate(mask: Array<Boolean>): Double {
        return if (getWeight(mask) > maxWeight) 0.0 else getCost(mask).toDouble() / totalCost
    }

    private fun deFactorial(number: Int): Int {
        var index = 0
        var value = 1
        while (value < number) {
            index++
            value *= index
        }
        return index
    }
}