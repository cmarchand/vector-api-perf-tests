package com.oxiane.formation.java.optimisation.doubles;

import java.util.List;

public class Solver2NdDegreeEquationDoubleRegular implements Solver2ndDegreeEquationDouble {

  public static EquationSolutionDouble solve(EquationDouble eq) {
    EquationSolutionDouble equationSolution = new EquationSolutionDouble(eq);
    equationSolution.setDiscriminant(eq.b() * eq.b() - 4 * eq.a() * eq.c());
    if (equationSolution.discriminant() >= 0) {
      double sqrt = Math.sqrt(equationSolution.discriminant());
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

  public List<EquationSolutionDouble> solve(List<EquationDouble> equations) {
    return equations
        .stream()
        .map(Solver2NdDegreeEquationDoubleRegular::solve)
        .toList();
  }
}