package com.oxiane.formation.java.optimisation;

import java.util.List;
import java.util.stream.Collectors;

public class Solver2ndDegreeEquationRegular implements Solver2ndDegreeEquation {

  public static EquationSolution solve(Equation equation) {
    EquationSolution equationSolution = new EquationSolution(equation);
    equationSolution.setDiscriminent(equation.b() * equation.b() - 4 * equation.a() * equation.c());
    if(equationSolution.discriminent()>0) {
      equationSolution.addSolution(
          (-equation.b() - Math.sqrt(equationSolution.discriminent())) /
              (equation.a() * 2)
      );
      equationSolution.addSolution(
          (-equation.b() + Math.sqrt(equationSolution.discriminent())) /
              (equation.a() * 2)
      );
    } else if(equationSolution.discriminent()==0) {
      equationSolution.addSolution(-equation.b() / (2 * equation.a()));
    }
    return equationSolution;
  }

  public List<EquationSolution> solve(List<Equation> equations) {
    return equations.stream()
                    .map(Solver2ndDegreeEquationRegular::solve)
                    .collect(Collectors.toList());
  }
}
