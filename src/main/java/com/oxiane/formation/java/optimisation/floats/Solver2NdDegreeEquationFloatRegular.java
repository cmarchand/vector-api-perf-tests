package com.oxiane.formation.java.optimisation.floats;

import java.util.List;

public class Solver2NdDegreeEquationFloatRegular implements Solver2ndDegreeEquationFloat {

  public static EquationSolutionFloat solve(EquationFloat eq) {
    EquationSolutionFloat equationSolution = new EquationSolutionFloat(eq);
    equationSolution.setDiscriminant(eq.b() * eq.b() - 4 * eq.a() * eq.c());
    if (equationSolution.discriminant() >= 0) {
      float sqrt = (float)Math.sqrt(equationSolution.discriminant());
      equationSolution.addSolution(
          (-eq.b() - sqrt) /
              (eq.a() * 2)
      );
      equationSolution.addSolution(
          (-eq.b() + sqrt) /
              (eq.a() * 2)
      );
    }
    return equationSolution;
  }

  public List<EquationSolutionFloat> solve(List<EquationFloat> equations) {
    return equations
        .stream()
        .map(Solver2NdDegreeEquationFloatRegular::solve)
        .toList();
  }
}