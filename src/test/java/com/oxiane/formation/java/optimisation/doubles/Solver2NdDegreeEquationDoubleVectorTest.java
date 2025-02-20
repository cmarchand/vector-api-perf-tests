package com.oxiane.formation.java.optimisation.doubles;

import jdk.incubator.vector.DoubleVector;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Solver2NdDegreeEquationDoubleVectorTest {

  @Test
  @DisplayName("Check that discrimants are correctly calculated")
  public void test1() {
    // Given
    List<EquationDouble> equationDoubles = getEquations();
    List<EquationSolutionDouble> expected = new Solver2NdDegreeEquationDoubleRegular().solve(equationDoubles);
    // When
    List<EquationSolutionDouble> solutions = new Solver2NdDegreeEquationDoubleVector().solve(equationDoubles);
    // Then
    for (int i = 0; i < solutions.size(); i++) {
      EquationSolutionDouble solution = solutions.get(i);
      EquationSolutionDouble expectedSolution = expected.get(i);
      Assertions
          .assertThat(expectedSolution.discriminant())
          .isEqualTo(solution.discriminant());
      Assertions.assertThat(solution.discriminant()).isEqualTo(expectedSolution.discriminant());
    }
  }

  @Test
  @DisplayName("Checks roots for discriminant 0")
  public void test2() {
    // Given
    List<EquationDouble> equationDoubles = getEquations();
    List<EquationSolutionDouble> expected = new Solver2NdDegreeEquationDoubleRegular().solve(equationDoubles);
    // When
    List<EquationSolutionDouble> solutions = new Solver2NdDegreeEquationDoubleVector().solve(equationDoubles);
    // Then
    Assertions
        .assertThat(solutions.get(0).solutions())
        .hasSize(1);
    Assertions
        .assertThat(solutions.get(0).solutions().first())
        .isEqualTo(expected.get(0).solutions().first());
  }

  @Test
  @DisplayName("Check that discrimants are correctly calculated")
  public void test3() {
    // Given
    List<EquationDouble> equationDoubles = getEquations();
    List<EquationSolutionDouble> expected = new Solver2NdDegreeEquationDoubleRegular().solve(equationDoubles);
    // When
    List<EquationSolutionDouble> solutions = new Solver2NdDegreeEquationDoubleVector().solve(equationDoubles);
    // Then
    for (int i = 0; i < solutions.size(); i++) {
      EquationSolutionDouble solution = solutions.get(i);
      EquationSolutionDouble expectedSolution = expected.get(i);
      if(solution.discriminant()>0) {
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
    int nbElementsToHaveInList = DoubleVector.SPECIES_PREFERRED.length() + 1;
    ArrayList<EquationDouble> equationDoubles = new ArrayList<>(nbElementsToHaveInList);
    equationDoubles.addAll(getEquations().subList(0, nbElementsToHaveInList - 1));
    equationDoubles.add(new EquationDouble(2d, 5d, 2d));
    Assertions.assertThat(equationDoubles).hasSize(nbElementsToHaveInList);
    double d = 9d;
    double r1 = -2d;
    double r2 = -0.5d;
    // When
    EquationSolutionDouble lastEquationSolutionDouble =
        new Solver2NdDegreeEquationDoubleVector()
            .solve(equationDoubles)
            .getLast();
    // Then
    Assertions.assertThat(lastEquationSolutionDouble.discriminant()).isEqualTo(d);
    Assertions.assertThat(lastEquationSolutionDouble.solutions()).hasSize(2);
    Assertions.assertThat(lastEquationSolutionDouble.solutions().first()).isEqualTo(r1);
    Assertions.assertThat(lastEquationSolutionDouble.solutions().last()).isEqualTo(r2);
  }
  @Test
  @DisplayName("Testing 2 roots in the corrective loop with (a+c)!=(ac)")
  public void test5() {
    // Given
    int nbElementsToHaveInList = DoubleVector.SPECIES_PREFERRED.length() + 1;
    ArrayList<EquationDouble> equationDoubles = new ArrayList<>(nbElementsToHaveInList);
    equationDoubles.addAll(getEquations().subList(0, nbElementsToHaveInList - 1));
    equationDoubles.add(new EquationDouble(4d/3d, 5d, 3d));
    Assertions.assertThat(equationDoubles).hasSize(nbElementsToHaveInList);
    double d = 9d;
    double r1 = -3d;
    double r2 = -0.75d;
    // When
    EquationSolutionDouble lastEquationSolutionDouble = new Solver2NdDegreeEquationDoubleVector().solve(equationDoubles)
                                                                                                 .getLast();
    // Then
    Assertions.assertThat(lastEquationSolutionDouble.discriminant()).isEqualTo(d);
    Assertions.assertThat(lastEquationSolutionDouble.solutions()).hasSize(2);
    Assertions.assertThat(lastEquationSolutionDouble.solutions().first()).isEqualTo(r1);
    Assertions.assertThat(lastEquationSolutionDouble.solutions().last()).isEqualTo(r2);
  }

  private static List<EquationDouble> getEquations() {
    List<EquationDouble> equationDoubles = new ArrayList<>(20);
    Random random = new Random(123456789l);
    equationDoubles.add(new EquationDouble(1d, 4d, 4d)); // this one, discriminent is 0. Must be first element
    equationDoubles.add(new EquationDouble(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000), random.nextDouble(-1000, 1000)));
    return equationDoubles;
  }
}