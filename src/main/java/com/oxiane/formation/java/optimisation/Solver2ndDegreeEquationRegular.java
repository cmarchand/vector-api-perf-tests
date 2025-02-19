package com.oxiane.formation.java.optimisation;

import java.util.List;
import java.util.stream.Collectors;

public class Solver2ndDegreeEquationRegular implements Solver2ndDegreeEquation {

  public static EquationSolution solve(Equation equation) {
    EquationSolution equationSolution = new EquationSolution(equation);
    equationSolution.setDiscriminent(equation.b() * equation.b() - 4 * equation.a() * equation.c());
    if (equationSolution.discriminent() >= 0) {
      double sqrt = Math.sqrt(equationSolution.discriminent());
      equationSolution.addSolution(
          (-equation.b() - sqrt) /
              (equation.a() * 2)
      );
      equationSolution.addSolution(
          (-equation.b() + sqrt) /
              (equation.a() * 2)
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
