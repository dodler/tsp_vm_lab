package lyan.artyom;

import vm.container.Matrix;
import vm.container.NumericMatrix;

/**
 * entry point for lab work
 * Created by artem on 19.09.15.
 */
public class AppVM
{
    public static void main(String[] args)
    {
        Matrix numericMatrix=NumericMatrix.randomMatrix(3,3);
        numericMatrix.set(0,0, 10.0);
        numericMatrix.set(0,1, -7.0);
        numericMatrix.set(0,2, 0.0);
        numericMatrix.set(1,0, -3.0);
        numericMatrix.set(1,1, 6.0);
        numericMatrix.set(1,2, 2.0);
        numericMatrix.set(2,0, 5.0);
        numericMatrix.set(2,1, -1.0);
        numericMatrix.set(2,2, 5.0);
        Calculator.LU(numericMatrix, null);

//        Matrix numMatr = NumericMatrix.zeroMatrix(3,3);
//        numMatr.set(0,0, 1.0);
//        numMatr.set(0,1, 2.0);
//        numMatr.set(0,2, 3.0);
//        numMatr.set(1,0, 3.0);
//        numMatr.set(1,1, 4.0);
//        numMatr.set(1,2, 8.0);
//        numMatr.set(2,0, 5.0);
//        numMatr.set(2,1, 2.0);
//        numMatr.set(2,2, 9.0);
//        NumericMatrix.printMatrix(numMatr);
//        Matrix numMatr2 = NumericMatrix.zeroMatrix(3,3);
//        numMatr2.set(1,0, 1.0);
//        numMatr2.set(1,1, 2.0);
//        numMatr2.set(1,2,3.0);
//        numMatr2.set(2,0, 3.0);
//        numMatr2.set(2,1, 4.0);
//        numMatr2.set(2,2, 8.0);
//        numMatr2.set(0,0, 5.0);
//        numMatr2.set(0,1, 2.0);
//        numMatr2.set(0,2, 9.0);
//        NumericMatrix.printMatrix(numMatr2);
//        NumericMatrix.printMatrix(NumericMatrix.multiplicateMatrix(numMatr, numMatr2));
    }
}
