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
        .assertThat(solutions.get(0).solutions().first())
        .isEqualTo(expected.get(0).solutions().first());
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
            .assertThat(solution.solutions().first())
            .isEqualTo(expectedSolution.solutions().first());
        Assertions
            .assertThat(solution.solutions().last())
            .isEqualTo(expectedSolution.solutions().last());
      }
    }
  }
  @Test
  @DisplayName("Testing 2 roots in the corrective loop")
  public void test4() {
    // Given
    int nbElementsToHaveInList = 512 / (Double.BYTES * 8) + 1;
    ArrayList<Equation> equations = new ArrayList<>(nbElementsToHaveInList);
    equations.addAll(getEquations().subList(0, nbElementsToHaveInList - 1));
    equations.add(new Equation(2d, 5d, 2d));
    Assertions.assertThat(equations).hasSize(nbElementsToHaveInList);
    double d = 9d;
    double r1 = -2d;
    double r2 = -0.5d;
    // When
    EquationSolution lastEquationSolution = new Solver2ndDegreeEquationVector().solve(equations)
                                                               .getLast();
    // Then
    Assertions.assertThat(lastEquationSolution.discriminent()).isEqualTo(d);
    Assertions.assertThat(lastEquationSolution.solutions()).hasSize(2);
    Assertions.assertThat(lastEquationSolution.solutions().first()).isEqualTo(r1);
    Assertions.assertThat(lastEquationSolution.solutions().last()).isEqualTo(r2);
  }
  @Test
  @DisplayName("Testing 2 roots in the corrective loop with (a+c)!=(ac)")
  public void test5() {
    // Given
    int nbElementsToHaveInList = 512 / (Double.BYTES * 8) + 1;
    ArrayList<Equation> equations = new ArrayList<>(nbElementsToHaveInList);
    equations.addAll(getEquations().subList(0, nbElementsToHaveInList - 1));
    equations.add(new Equation(4d/3d, 5d, 3d));
    Assertions.assertThat(equations).hasSize(nbElementsToHaveInList);
    double d = 9d;
    double r1 = -3d;
    double r2 = -0.75d;
    // When
    EquationSolution lastEquationSolution = new Solver2ndDegreeEquationVector().solve(equations)
                                                               .getLast();
    // Then
    Assertions.assertThat(lastEquationSolution.discriminent()).isEqualTo(d);
    Assertions.assertThat(lastEquationSolution.solutions()).hasSize(2);
    Assertions.assertThat(lastEquationSolution.solutions().first()).isEqualTo(r1);
    Assertions.assertThat(lastEquationSolution.solutions().last()).isEqualTo(r2);
  }

  private static List<Equation> getEquations() {
    List<Equation> equations = new ArrayList<>(20);
    Random random = new Random(123456789l);
    equations.add(new Equation(1d, 4d, 4d)); // this one, discriminent is 0. Must be first element
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