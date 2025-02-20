package com.oxiane.formation.java.optimisation.floats;

import jdk.incubator.vector.FloatVector;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Solver2NdDegreeEquationFloatVectorTest {

  @Test
  @DisplayName("Check that discrimants are correctly calculated")
  public void test1() {
    // Given
    List<EquationFloat> equationFloats = getEquations();
    List<EquationSolutionFloat> expected = new Solver2NdDegreeEquationFloatRegular().solve(equationFloats);
    // When
    List<EquationSolutionFloat> solutions = new Solver2NdDegreeEquationFloatVector().solve(equationFloats);
    // Then
    for (int i = 0; i < solutions.size(); i++) {
      EquationSolutionFloat solution = solutions.get(i);
      EquationSolutionFloat expectedSolution = expected.get(i);
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
    List<EquationFloat> equationFloats = getEquations();
    List<EquationSolutionFloat> expected = new Solver2NdDegreeEquationFloatRegular().solve(equationFloats);
    // When
    List<EquationSolutionFloat> solutions = new Solver2NdDegreeEquationFloatVector().solve(equationFloats);
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
    List<EquationFloat> equationFloats = getEquations();
    List<EquationSolutionFloat> expected = new Solver2NdDegreeEquationFloatRegular().solve(equationFloats);
    // When
    List<EquationSolutionFloat> solutions = new Solver2NdDegreeEquationFloatVector().solve(equationFloats);
    // Then
    for (int i = 0; i < solutions.size(); i++) {
      EquationSolutionFloat solution = solutions.get(i);
      EquationSolutionFloat expectedSolution = expected.get(i);
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
    int nbElementsToHaveInList = FloatVector.SPECIES_PREFERRED.length() + 1;
    ArrayList<EquationFloat> equationFloats = new ArrayList<>(nbElementsToHaveInList);
    equationFloats.addAll(getEquations().subList(0, nbElementsToHaveInList - 1));
    equationFloats.add(new EquationFloat(2f, 5f, 2f));
    Assertions.assertThat(equationFloats).hasSize(nbElementsToHaveInList);
    float d = 9f;
    float r1 = -2f;
    float r2 = -0.5f;
    // When
    EquationSolutionFloat lastEquationSolutionFloat =
        new Solver2NdDegreeEquationFloatVector()
            .solve(equationFloats)
            .getLast();
    // Then
    Assertions.assertThat(lastEquationSolutionFloat.discriminant()).isEqualTo(d);
    Assertions.assertThat(lastEquationSolutionFloat.solutions()).hasSize(2);
    Assertions.assertThat(lastEquationSolutionFloat.solutions().first()).isEqualTo(r1);
    Assertions.assertThat(lastEquationSolutionFloat.solutions().last()).isEqualTo(r2);
  }
  @Test
  @DisplayName("Testing 2 roots in the corrective loop with (a+c)!=(ac)")
  public void test5() {
    // Given
    int nbElementsToHaveInList = FloatVector.SPECIES_PREFERRED.length() + 1;
    ArrayList<EquationFloat> equationFloats = new ArrayList<>(nbElementsToHaveInList);
    equationFloats.addAll(getEquations().subList(0, nbElementsToHaveInList - 1));
    equationFloats.add(new EquationFloat(4f/3f, 5f, 3f));
    Assertions.assertThat(equationFloats).hasSize(nbElementsToHaveInList);
    float d = 9f;
    float r1 = -3f;
    float r2 = -0.75f;
    // When
    EquationSolutionFloat lastEquationSolutionFloat = new Solver2NdDegreeEquationFloatVector().solve(equationFloats)
                                                                                                 .getLast();
    // Then
    Assertions.assertThat(lastEquationSolutionFloat.discriminant()).isEqualTo(d);
    Assertions.assertThat(lastEquationSolutionFloat.solutions()).hasSize(2);
    Assertions.assertThat(lastEquationSolutionFloat.solutions().first()).isEqualTo(r1);
    Assertions.assertThat(lastEquationSolutionFloat.solutions().last()).isEqualTo(r2);
  }

  private static List<EquationFloat> getEquations() {
    List<EquationFloat> equationFloats = new ArrayList<>(20);
    Random random = new Random(123456789l);
    equationFloats.add(new EquationFloat(1f, 4f, 4f)); // this one, discriminent is 0. Must be first element
    equationFloats.add(new EquationFloat(random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000), random.nextFloat(-1000, 1000)));
    return equationFloats;
  }
}