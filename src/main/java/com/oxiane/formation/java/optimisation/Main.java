package com.oxiane.formation.java.optimisation;

public class Main {
  public static void main(String[] args) {
    // 4/3 x^2 + 5x + 3 = 0
    EquationSolution solution = Solver2ndDegreeEquationRegular.solve(new Equation(4d/3d, 5d, 3d));
    System.out.println(solution);
  }
}
