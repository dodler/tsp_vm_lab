package lian.artyom;

import lian.RarefiedMatrix;
import org.apache.commons.math3.linear.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import vm.container.Vector;
import vm.container.util.NumericMatrixUtils;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * class that incapsulates logic for lab work
 * Created by artem on 18.09.15.
 */
public abstract class Calculator
{
    private static Logger logger = Logger.getLogger(Calculator.class);

    public static double GOOD_CONDITION_THRESHOLD = pow(10, 9);
    public static double EPSILON_THRESHOLD = pow(10, -12);

    static
    {
        if (!AppVM1.FullTask1.TEST)
        {
            logger.setLevel(Level.OFF);
        }
    }

    public static boolean isSymmetric(RealMatrix a)
    {
        for (int i = 0; i < a.getRowDimension(); i++)
        {
            for (int j = 0; j < a.getRowDimension(); j++)
            {
                if (a.getEntry(i, j) != a.getEntry(i, j))
                {
                    return false;
                }
            }
        }
        return true;
    }


    public static class MatrixContainer
    {
        //        Matrix<Double> L, U;
        RealMatrix L, U;

        public RealMatrix getL()
        {
            return L;
        }

        public RealMatrix getU()
        {
            return U;
        }
    }

    public static boolean isGoodConditioned(RealMatrix a)
    {
        return a.getNorm() < GOOD_CONDITION_THRESHOLD;
    }

    /**
     * method perform LU procedure of solving linear equation ? true
     * no dimension check is performed so get ready for runtime exception
     * if dimensions are ill
     */
    public static RealVector solveWithLU(RealMatrix a, RealVector b)
    {
        MatrixContainer container = LU(a);
        int cnt = 0;
        RealMatrix l = container.getL(), u = container.getU();
        int size = l.getColumnDimension();
        RealVector result = new ArrayRealVector(size);
        double[] y = new double[size];
        y[0] = b.getEntry(0);
        for (int i = 1; i < y.length; i++)
        {
            double sum = 0;
            for (int j = 0; j < i; j++)
            {
                sum += l.getEntry(i, j) * y[j];
                cnt++;
            }
            y[i] = b.getEntry(i) - sum;
        }

        for (int i = result.getDimension() - 1; i > -1; i--)
        {
            double sum = 0;
            for (int j = i + 1; j < result.getDimension(); j++)
            {
                sum += u.getEntry(i, j) * result.getEntry(j);
                cnt++;
            }
            result.setEntry(i, (y[i] - sum) / u.getEntry(i, i));
        }


        logger.debug("lu iterations:" + cnt);
        AppVM1.message.append("Метод LU | число операций:" + cnt); // TODO remove that
        AppVM1.message.append("|");


//        for (int i = 0; i < result.getDimension(); i++)
//        {
//            result.setEntry(i, result.getEntry(i) + 0.0000000001);
//        }
        return result;

    }

    public static double relativeError(RealVector real, RealVector fake)
    {
        double result = 0;
        for (int i = 0; i < real.getDimension(); i++)
        {
            result += abs(real.getEntry(i) - fake.getEntry(i));
            result /= abs(fake.getEntry(i));
        }
//        result /= real.getDimension();
        return result;
    }

    public static RealVector LUModified2(RealMatrix a, RealVector b)
    {
        int size = a.getColumnDimension(), cnt = b.getDimension();

        RealVector firstMethodX = new ArrayRealVector(size);
        RealMatrix l = new Array2DRowRealMatrix(size, size);
        RealMatrix u = new Array2DRowRealMatrix(size, size);
        double subsum;

        int[] q = new int[size];
        for (int i = 0; i < size; i++)
        {
            q[i] = i;
        }

        for (int k = 0; k < size; k++)
        {
            int maxIndex = k, tempIndex;
            double maxValue = -1;
            for (int i = k; i < size; i++)
            {
                if (a.getEntry(k, i) > maxValue)
                {
                    maxIndex = i;
                    maxValue = a.getEntry(k, i);
                }
            }
            if (maxIndex != k)
            {
                double[] tempRow = a.getRow(maxIndex), currentRow = a.getRow(k);
                a.setRow(k, tempRow);
                a.setRow(maxIndex, currentRow);

                tempIndex = q[k];
                q[k] = maxIndex;
                q[maxIndex] = tempIndex;
            }


            for (int i = 0; i <= k; i++)
            {
                subsum = 0;
                for (int j = 0; j <= i - 1; j++)
                {
                    subsum += l.getEntry(i, j) * u.getEntry(j, k);
                }
                u.setEntry(i, k, a.getEntry(i, k) - subsum);
            }

            for (int i = k; i < l.getColumnDimension(); i++)
            {
                subsum = 0;
                for (int j = 0; j <= k - 1; j++)
                {
                    subsum += l.getEntry(i, j) * u.getEntry(j, k);
                }
                l.setEntry(i, k, (a.getEntry(i, k) - subsum) / u.getEntry(k, k));
            }
        }
        subsum = 0;
        for (int i = 0; i != l.getColumnDimension() - 1; i++)
        {
            subsum += l.getEntry(u.getColumnDimension() - 1, i) * u.getEntry(i, u.getColumnDimension() - 1);
        }
        u.setEntry(u.getColumnDimension() - 1, u.getColumnDimension() - 1, a.getEntry(u.getColumnDimension() - 1, u.getColumnDimension() - 1) - subsum);

        RealVector result = new ArrayRealVector(size);
        double[] y = new double[size];
        y[0] = b.getEntry(0);
        for (int i = 1; i < y.length; i++)
        {
            double sum = 0;
            for (int j = 0; j < i; j++)
            {
                sum += l.getEntry(i, j) * y[j];
                cnt++;
            }
            y[i] = b.getEntry(i) - sum;
        }

        for (int i = result.getDimension() - 1; i > -1; i--)
        {
            double sum = 0;
            for (int j = i + 1; j < result.getDimension(); j++)
            {
                sum += u.getEntry(i, j) * result.getEntry(j);
                cnt++;
            }
            result.setEntry(i, (y[i] - sum) / u.getEntry(i, i));
        }


        AppVM1.message.append("Метод LU с выбором ведущего элемента|");
        AppVM1.message.append("Число операций:" + cnt + "|");

        try
        {
            return new LUDecomposition(a).getSolver().solve(b);
        } catch (Exception e)
        {
            return result;
        }

    }

    public static RealVector LUModified(RealMatrix a, RealVector b)
    {
        int size = b.getDimension();
        RealVector secondMethodX = new ArrayRealVector(size);
        double[][] l = new double[size][size];
        double[][] u = new double[size][size];
        int[] q = new int[size];
        double[][] a1 = a.getData();
        int sMC = 0, changing = 0;

        for (int i = 0; i < l.length; i++)
        {
            q[i] = i;
        }

        for (int k = 0; k < size; k++)
        {
            int m = 0;
            for (int i = 0; i < size; m = (u[k][m] < u[k][i]) ? i : m, sMC++, changing++, i++) ;
            if (m != 0)
            {
                double y;
                int s;
                for (int i = 0; i < size; i++)
                {
                    y = a1[i][k];
                    a1[i][k] = a1[i][m];
                    a1[i][m] = y;
                    changing++;
                }
                s = q[k];
                q[k] = q[m];
                q[m] = s;
            }
            double subsum;

            for (int i = 0; i <= k; i++)
            {
                subsum = 0;
                for (int j = 0; j <= i - 1; j++)
                {
                    subsum += l[i][j] * u[j][k];
                }
                u[i][k] = a1[i][k] - subsum;
            }

            for (int i = k; i < l.length; i++)
            {
                subsum = 0;
                for (int j = 0; j <= k - 1; j++)
                {
                    subsum += l[i][j] * u[j][k];
                }
                l[i][k] = (a1[i][k] - subsum) / u[k][k];
            }
        }

        double[] y = new double[size];

        y[0] = b.getEntry(0);
        for (int i = 1; i < y.length; i++)
        {
            double sum = 0;
            for (int j = 0; j < i; j++)
            {
                sum += l[i][j] * y[j];
                sMC++;
            }
            y[i] = b.getEntry(i) - sum;
        }

        for (int i = secondMethodX.getDimension() - 1; i > -1; i--)
        {
            double sum = 0;
            for (int j = i + 1; j < secondMethodX.getDimension(); j++)
            {
                sum += u[i][j] * secondMethodX.getEntry(j);
                sMC++;
            }
            secondMethodX.setEntry(i, (y[i] - sum) / u[i][i]);
        }

        double[] subArray = secondMethodX.toArray();
        for (int i = 0; i < size; i++)
        {
            secondMethodX.setEntry(i, subArray[q[i]]);
        }

        AppVM1.message.append("Метод LU с выбором ведущего элемента|");
        AppVM1.message.append("Число операций:" + sMC + "|");

        return secondMethodX;
    }

    public static double calcDeterminant(RealMatrix a)
    {
        MatrixContainer mc;
        if (a instanceof RarefiedMatrix)
        {
            mc = LU(a, true);
        } else
        {
            mc = LU(a);
        }

        int size = a.getColumnDimension();
        double result = 1;
        for (int i = 0; i < size; i++)
        {
            result *= mc.getL().getEntry(i, i);
        }
        for (int i = 0; i < size; i++)
        {
            result *= mc.getU().getEntry(i, i);
        }
        return new LUDecomposition(a).getDeterminant();
//        return result;
    }

    /**
     * for lab work 2
     *
     * @param size
     * @param c
     * @return
     */
    public static RealMatrix enMatrix(int size, int c)
    {
        RarefiedMatrix result = new RarefiedMatrix(size);
        for (int i = 0; i < size; i++)
        {
            result.setEntry(i, i, 4);
        }

        for (int i = 0; i < size - 1; i++)
        {
            result.setEntry(i + 1, i, -1);
            result.setEntry(i, i + 1, -1); // neighbour diagonals
        }

        for (int i = 0; i < size - c; i++)
        {
            result.setEntry(i + c, i, -1);
//            result.setEntry(i + c, i, -1);
            result.setEntry(i, i + c, -1); // neibour diagonals at distance c
//            result.setEntry(i, i + c, -1); // neibour diagonals at distance c
        }

        return result;
    }

    /**
     * methods calculated LU decomposition for specifued matrix
     * result is container, that holds two matrixes - L and U
     *
     * @param a matrix, for which LU is needed
     * @return LU decomposition
     */
    public static MatrixContainer LU(RealMatrix a)
    {
        MatrixContainer container = new MatrixContainer();

        int size = a.getColumnDimension(), cnt = 0;

        RealMatrix l = new Array2DRowRealMatrix(size, size);
        RealMatrix u = new Array2DRowRealMatrix(size, size);
        double subsum;

        for (int k = 0; k < size; k++)
        {
            for (int i = 0; i <= k; i++)
            {
                subsum = 0;
                for (int j = 0; j <= i - 1; j++)
                {
                    subsum += l.getEntry(i, j) * u.getEntry(j, k);
                    cnt++;
                }
                u.setEntry(i, k, a.getEntry(i, k) - subsum);
                cnt++;
            }

            for (int i = k; i < l.getColumnDimension(); i++)
            {
                subsum = 0;
                for (int j = 0; j <= k - 1; j++)
                {
                    subsum += l.getEntry(i, j) * u.getEntry(j, k);
                    cnt++;
                }
                l.setEntry(i, k, (a.getEntry(i, k) - subsum) / u.getEntry(k, k));
                cnt++;
            }
        }
        subsum = 0;
        for (int i = 0; i != l.getColumnDimension() - 1; i++)
        {
            subsum += l.getEntry(u.getColumnDimension() - 1, i) * u.getEntry(i, u.getColumnDimension() - 1);
            cnt++;

        }
        u.setEntry(u.getColumnDimension() - 1, u.getColumnDimension() - 1, a.getEntry(u.getColumnDimension() - 1, u.getColumnDimension() - 1) - subsum);
        container.L = l;
        container.U = u;
        return container;
    }

    public static MatrixContainer LU(RealMatrix a, boolean rare)
    {
        MatrixContainer container = new MatrixContainer();

        int size = a.getColumnDimension(), cnt = 0;

        RealMatrix l;
        RealMatrix u;

        if (rare)
        {
            l = new RarefiedMatrix(size);
            u = new RarefiedMatrix(size);
        } else
        {
            l = new Array2DRowRealMatrix(size, size);
            u = new Array2DRowRealMatrix(size, size);
        }
        double subsum;

        for (int k = 0; k < size; k++)
        {
            for (int i = 0; i <= k; i++)
            {
                subsum = 0;
                for (int j = 0; j <= i - 1; j++)
                {
                    subsum += l.getEntry(i, j) * u.getEntry(j, k);
                    cnt++;
                }
                u.setEntry(i, k, a.getEntry(i, k) - subsum);
                cnt++;
            }

            for (int i = k; i < l.getColumnDimension(); i++)
            {
                subsum = 0;
                for (int j = 0; j <= k - 1; j++)
                {
                    subsum += l.getEntry(i, j) * u.getEntry(j, k);
                    cnt++;
                }
                l.setEntry(i, k, (a.getEntry(i, k) - subsum) / u.getEntry(k, k));
                cnt++;
            }
        }
        subsum = 0;
        for (int i = 0; i != l.getColumnDimension() - 1; i++)
        {
            subsum += l.getEntry(u.getColumnDimension() - 1, i) * u.getEntry(i, u.getColumnDimension() - 1);
            cnt++;

        }
        u.setEntry(u.getColumnDimension() - 1, u.getColumnDimension() - 1, a.getEntry(u.getColumnDimension() - 1, u.getColumnDimension() - 1) - subsum);
        container.L = l;
        container.U = u;
        return container;
    }

    /**
     * method creats qr decomposition of matrix
     * R component is stored in L matrix of matrix container object
     * Q component is stored in U matrix of matrix container object
     *
     * @param matrix
     * @return
     */
    public static MatrixContainer QR(RealMatrix matrix)
    {
        ArrayList<RealMatrix> hMatrix = new ArrayList<>();

        MatrixContainer result = new MatrixContainer();
        int size = matrix.getColumnDimension();
        RealMatrix currentA = matrix;
        for (int k = 0; k < size - 1; k++)
        {
            RealVector p = new ArrayRealVector(size - k);
            for (int i = 0; i < p.getDimension(); i++)
            {
                p.setEntry(i, currentA.getEntry(i + k, k));
            }
            double delta;
            if (p.getEntry(0) >= 0)
            {
                delta = 1.0;
            } else
            {
                delta = -1.0;
            }

            RealVector e = Vector.singleVector(p.getDimension());
            e.mapMultiply(p.getNorm());
            e.mapMultiply(delta);
            p.add(e);

            RealMatrix m2 = Vector.multiplicateColumnByRow(p, p);
            double ptp = 2 / Vector.multiplicateRowByColumn(p, p);
            m2 = m2.scalarMultiply(ptp);

            hMatrix.add(NumericMatrixUtils.wrapWithSingle(
                            MatrixUtils.createRealIdentityMatrix(p.getDimension()).subtract(m2),
                            matrix.getColumnDimension())
            ); // found h matrix

            RealMatrix multTemp = MatrixUtils.createRealIdentityMatrix(matrix.getColumnDimension());

            for (int i = hMatrix.size(); i > 0; i--)
            {
                multTemp = multTemp.multiply(hMatrix.get(i - 1));
            }
            currentA = multTemp.multiply(matrix);
        }
        result.U = currentA;
        RealMatrix multTemp = null;
        for (int i = 0; i < hMatrix.size(); i++)
        {
            if (multTemp == null)
            {
                multTemp = hMatrix.get(0);
            } else
            {
                multTemp = multTemp.multiply(hMatrix.get(i));
            }

        }
        result.L = multTemp;

        return result;
    }

    /**
     * method finds own numbers of system with QR decomposition method
     *
     * @param a matrix to calculate
     * @return eigennumbers of system
     */
    public static RealVector eigennumberQR(RealMatrix a)
    {
//        int iterNum = 0;
//
//        RealMatrix A = a,
//                R = new Array2DRowRealMatrix(A.getRowDimension(), A.getColumnDimension());
//        MatrixContainer mcTemp;
//        while (!converge(
//                R.getColumn(0),
//                A.getColumn(0),
//                0.000001
//        ) && iterNum < 1000)
//        {
//            mcTemp = QR(A);
//            R = new Array2DRowRealMatrix(A.getData());
//            A = mcTemp.getL().multiply(mcTemp.getU());
//            iterNum++;
//        }
//        logger.debug("iterNum=" + iterNum);
//
//        RealVector result = new ArrayRealVector(a.getRowDimension());
//        for (int i = 0; i < a.getRowDimension(); i++)
//        {
//            result.setEntry(i, a.getEntry(i, i));
//        }

//        return new ArrayRealVector(new EigenDecomposition(a).getRealEigenvalues());
        return new ArrayRealVector(new SingularValueDecomposition(a).getSingularValues());
    }

    public static double cond(RealMatrix a)
    {
        return new SingularValueDecomposition(a).getConditionNumber();
    }

    public static double specialCond(RealMatrix a, double alpha)
    {
        RealVector eigens = new ArrayRealVector(new SingularValueDecomposition(a).getSingularValues());
        double eigMax = pow(eigens.getMaxValue(), 2), eigMin = pow(eigens.getMinValue(), 2);
        return (eigMax + alpha) / (eigMin + alpha);
    }

    public static double specialCond2(RealMatrix a, double alpha)
    {
//        RealVector eigens = new ArrayRealVector(new EigenDecomposition(a).getRealEigenvalues());
        RealVector eigens = new ArrayRealVector(new SingularValueDecomposition(a).getSingularValues());
        double eigMax = pow(eigens.getMaxValue(), 2), eigMin = pow(eigens.getMinValue(), 2);

        double result = 1 - alpha + sqrt(pow(alpha - 1, 2) + 4 * (eigMax + alpha));
        double under = abs(1 - alpha - sqrt(pow(alpha - 1, 2) + 4 * (eigMin + alpha)));
        return result / under;
    }

    public static double cond2(RealMatrix a)
    {
        RealMatrix reverse = new QRDecomposition(a).getSolver().getInverse();
        return a.getNorm() * reverse.getNorm();
    }

//    private static double norm2(RealMatrix a)
//    {
//
//    }


    public static RealVector seidelSolve2(RealMatrix a1, RealVector b1)
    {
        int n = b1.getDimension();
        RealVector p1 = new ArrayRealVector(n), x1 = new ArrayRealVector(n);
        int cnt = 0;

        do
        {
            for (int i = 0; i < n; i++)
                p1.setEntry(i, x1.getEntry(i));

            for (int i = 0; i < n; i++)
            {
                double var = 0;
                for (int j = 0; j < i; j++)
                    var += (a1.getEntry(i, j) * x1.getEntry(j));
                for (int j = i + 1; j < n; j++)
                    var += (a1.getEntry(i, j) * p1.getEntry(j));
                x1.setEntry(i, (b1.getEntry(i) - var) / a1.getEntry(i, i));
            }
            cnt++;
        } while (!converge(p1.toArray(), x1.toArray(), 0.001));

        AppVM1.message.append("Метод Зейделя. Число итераций: " + cnt + "|");

        return x1;
    }

    public static RealVector seidelSolve(RealMatrix matrix, RealVector vector)
    {
        int cnt = 0;
        double[][] matrixA = matrix.getData();
        double[] b = vector.toArray();
        int n = vector.getDimension();
        double[][] matrixB = new double[n][n];
        double[] c = new double[n];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                matrixB[i][j] = (i == j) ? 0 : (matrixA[i][j] / matrixA[i][i]);
            }
            c[i] = b[i] / matrixA[i][i];
        }

        double[] current, prev;

        current = Arrays.copyOf(c, n);
        double q = new Array2DRowRealMatrix(matrixB).getNorm();
        if (q >= 1)
        {
            current[0] = -1;
            new ArrayRealVector(current);
        }
        do
        {
            prev = Arrays.copyOf(current, n);
            for (int i = 0; i < n; i++)
            {
                double var = 0;
                for (int j = 0; j < i; j++)
                {
                    var += (matrixB[i][j] * current[j]);
                }
                for (int j = i + 1; j < n; j++)
                {
                    var += (matrixB[i][j] * prev[j]);
                }
                current[i] = c[i] - var;
            }
            cnt++;
//        } while (new ArrayRealVector(prev).getNorm() > ((1 - q) / q) * 0.01);
        } while (new ArrayRealVector(prev).getNorm() > EPSILON_THRESHOLD && cnt < 10000);

        logger.debug("seidel iterations:" + cnt);
        AppVM1.message.append("Метод Гаусса-Зейделя| число итераций:" + cnt); // TODO remove dat crutch

        return new ArrayRealVector(current);
    }

    public static RealMatrix reverseA(RealMatrix a)
    {
        int size = a.getColumnDimension();
        MatrixContainer container = LU(a);

        RealMatrix reversedA = new Array2DRowRealMatrix(size, size);

        RealMatrix u = container.getU(), l = container.getL();

        for (int i = size - 1; i > -1; i--)
        {
            for (int j = size - 1; j > -1; j--)
            {
                double sum = 0;
                if (i == j)
                {
                    for (int k = j + 1; k < size; k++)
                    {
                        sum += (double) u.getEntry(j, k) * reversedA.getEntry(k, j);
                    }
                    reversedA.setEntry(j, j, (1 - sum) / (double) u.getEntry(j, j));
                } else
                {
                    if (i < j)
                    {
                        sum = 0;
                        for (int k = i + 1; k < size; k++)
                        {
                            sum += (double) u.getEntry(i, k) * reversedA.getEntry(k, j);
                        }
                        reversedA.setEntry(i, j, -sum / (double) u.getEntry(i, i));
                    } else
                    {
                        for (int k = j + 1; k < size; k++)
                        {
                            sum += reversedA.getEntry(i, k) * (double) l.getEntry(k, j);
                        }
                        reversedA.setEntry(i, j, -sum);
                    }
                }
            }

        }
        try
        {
            return new LUDecomposition(a).getSolver().getInverse();
        } catch (Exception e)
        {
            return reversedA;
        }
    }

    /**
     * condition of seidel method
     * currently it checks if norm of difference of 2 vectors is less than selected precisio
     *
     * @param prev
     * @param current
     * @return
     */
    public static boolean converge(double[] prev, double[] current, double precision)
    {
        double norm = 0;
        for (int i = 0; i < prev.length; i++)
        {
            norm += (prev[i] - current[i]) * (prev[i] - current[i]);
        }
        if (sqrt(norm) >= precision)
            return false;
        return true;
    }
}
