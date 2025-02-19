package com.oxiane.formation.java.optimisation;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;

public class Main {
  public static void main(String[] args) {
    // 4/3 x^2 + 5x + 3 = 0
    VectorSpecies<Double> species = DoubleVector.SPECIES_PREFERRED;
    System.out.println("Proc stream length: " + species.length()*64+"bits");
    EquationSolution solution = Solver2ndDegreeEquationRegular.solve(new Equation(4d/3d, 5d, 3d));
    System.out.println(solution);
  }
}
