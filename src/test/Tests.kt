package test

import main.KnapsackProblem
import org.testng.annotations.Test

class Tests {

    @Test
    fun first(){
        val things = mutableListOf<KnapsackProblem.Thing>()
        things.add(KnapsackProblem.Thing(3, 8))
        things.add(KnapsackProblem.Thing(1, 4))
        things.add(KnapsackProblem.Thing(3, 2))
        things.add(KnapsackProblem.Thing(2, 6))
        things.add(KnapsackProblem.Thing(1, 2))
        val knapsack = KnapsackProblem(things.toTypedArray(), 5)
        val firstMask = knapsack.knapsack(3)
        prn(firstMask)
        println(knapsack.getCost(firstMask))
        println(knapsack.getWeight(firstMask))
        val secondMask = knapsack.knapsack(3)
        prn(firstMask)
        println(knapsack.getCost(secondMask))
        println(knapsack.getWeight(secondMask))
    }

    fun prn(array: Array<Boolean>){
        for (element in array){
            print(if (element) "1 " else "0 ")
        }
        println()
    }
}