package com.tsystems.javaschool.tasks.pyramid;

import java.util.List;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */

    public int[][] buildPyramid(List<Integer> inputNumbers) {

        //check if size of the array corresponds the conditions for pyramid//

        int rows = 0;
        int elementsInRow = 1;
        int inputNumbersLength = inputNumbers.size();

        while (inputNumbersLength > 0) {
            inputNumbersLength -= elementsInRow;
            rows++;
            elementsInRow++;
        }

        int columns = 2 * elementsInRow - 3;

        /* If one of the elements is a null */
        for (Integer inputNumber : inputNumbers) {
            if (inputNumber == null)
                throw new CannotBuildPyramidException();
        }

        /* If size of array doesn't meet requirements */
        if (inputNumbersLength < 0)
            throw new CannotBuildPyramidException();

        /* ---- Sort of given array ---- */
        inputNumbersLength = inputNumbers.size();
        for (int i = 0; i < inputNumbersLength - 1; i++) {
            for (int j = i + 1; j < inputNumbersLength; j++) {
                if (inputNumbers.get(i) > inputNumbers.get(j)) {
                    int sorting = inputNumbers.get(i);
                    inputNumbers.set(i, inputNumbers.get(j));
                    inputNumbers.set(j, sorting);
                }
            }
        }

        int[][] result = new int[rows][columns];

        //Populating output array with zeros//
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                result[i][j] = 0;

        //Populating output array with pyramid values //
        elementsInRow = 1;
        int startIndex = columns / 2;
        int currentInputNumbersIndex = 0;

        for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
            int currentIndex = startIndex;
            for (int j = 0; j < elementsInRow; j++) {
                result[rowIndex][currentIndex] = inputNumbers.get(currentInputNumbersIndex);
                currentInputNumbersIndex++;
                currentIndex += 2;
            }
            startIndex--;
            elementsInRow++;
        }
        return result;
    }
}
