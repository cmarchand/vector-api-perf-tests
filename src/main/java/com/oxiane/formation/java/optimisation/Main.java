package com.oxiane.formation.java.optimisation;

import com.oxiane.formation.java.optimisation.doubles.EquationDouble;
import com.oxiane.formation.java.optimisation.doubles.EquationSolutionDouble;
import com.oxiane.formation.java.optimisation.doubles.Solver2NdDegreeEquationDoubleRegular;
import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;

public class Main {
  public static void main(String[] args) {
    // 4/3 x^2 + 5x + 3 = 0
    VectorSpecies<Double> species = DoubleVector.SPECIES_PREFERRED;
    System.out.println("Proc stream length: " + species.length()*64+"bits");
    EquationSolutionDouble solution = Solver2NdDegreeEquationDoubleRegular.solve(new EquationDouble(4d/3d, 5d, 3d));
    System.out.println(solution);
  }
}
