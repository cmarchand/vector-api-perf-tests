package com.oxiane.formation.java.optimisation;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Solver2ndDegreeEquationVectorTest {

  @Test
  @DisplayName("Check that discriments are correctly calculated")
  public void test1() {
    // Given
    List<Equation> equations = getEquations();
    List<EquationSolution> expected = new Solver2ndDegreeEquationRegular().solve(equations);
    // When
    List<EquationSolution> solutions = new Solver2ndDegreeEquationVector().solve(equations);
    // Then
    for (int i = 0; i < solutions.size(); i++) {
      EquationSolution solution = solutions.get(i);
      EquationSolution expectedSolution = expected.get(i);
      Assertions
          .assertThat(expectedSolution.discriminent())
          .isEqualTo(solution.discriminent());
      Assertions.assertThat(solution.discriminent()).isEqualTo(expectedSolution.discriminent());
    }
  }

  @Test
  @DisplayName("Checks roots for discriminent 0")
  public void test2() {
    // Given
    List<Equation> equations = getEquations();
    List<EquationSolution> expected = new Solver2ndDegreeEquationRegular().solve(equations);
    // When
    List<EquationSolution> solutions = new Solver2ndDegreeEquationVector().solve(equations);
    // Then
    Assertions
        .assertThat(solutions.get(0).solutions())
        .hasSize(1);
    Assertions
        .assertThat(solutions.get(0).solutions().get(0))
        .isEqualTo(expected.get(0).solutions().get(0));
  }

  @Test
  @DisplayName("Check that discriments are correctly calculated")
  public void test3() {
    // Given
    List<Equation> equations = getEquations();
    List<EquationSolution> expected = new Solver2ndDegreeEquationRegular().solve(equations);
    // When
    List<EquationSolution> solutions = new Solver2ndDegreeEquationVector().solve(equations);
    // Then
    for (int i = 0; i < solutions.size(); i++) {
      EquationSolution solution = solutions.get(i);
      EquationSolution expectedSolution = expected.get(i);
      if(solution.discriminent()>0) {
        Assertions
            .assertThat(solution.solutions()).hasSize(2);
        Assertions
            .assertThat(solution.solutions().get(0))
            .isEqualTo(expectedSolution.solutions().get(0));
        Assertions
            .assertThat(solution.solutions().get(1))
            .isEqualTo(expectedSolution.solutions().get(1));
      }
    }
  }

  private static List<Equation> getEquations() {
    List<Equation> equations = new ArrayList<>(20);
    Random random = new Random(123456789l);
    equations.add(new Equation(1d, 4d, 4d)); // this one, discriminent is 0. Must be frist element
    equations.add(new Equation(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equations.add(new Equation(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equations.add(new Equation(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equations.add(new Equation(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equations.add(new Equation(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equations.add(new Equation(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equations.add(new Equation(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equations.add(new Equation(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equations.add(new Equation(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equations.add(new Equation(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equations.add(new Equation(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equations.add(new Equation(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    return equations;
  }
}