package com.oxiane.formation.java.optimisation;

import java.util.List;
import java.util.stream.Collectors;

public class Solver2ndDegreeEquationRegular implements Solver2ndDegreeEquation {

  public static EquationSolution solve(Equation eq) {
    EquationSolution equationSolution = new EquationSolution(eq);
    equationSolution.setDiscriminant(eq.b() * eq.b() - 4 * eq.a() * eq.c());
    if (equationSolution.discriminent() >= 0) {
      double sqrt = Math.sqrt(equationSolution.discriminent());
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

  public List<EquationSolution> solve(List<Equation> equations) {
    return equations
        .stream()
        .map(Solver2ndDegreeEquationRegular::solve)
        .collect(Collectors.toList());
  }
}
