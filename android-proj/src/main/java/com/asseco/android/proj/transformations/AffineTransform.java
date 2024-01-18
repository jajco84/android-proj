package com.asseco.android.proj.transformations;


import android.util.Pair;


import com.asseco.android.proj.ProjectionParameter;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents affine math transform which ttransform input coordinates to target using affine transformation matrix. Dimensionality might change.
 * If the transform's input dimension is M, and output dimension is N, then the matrix will have size [N+1][M+1].
 * The +1 in the matrix dimensions allows the matrix to do a shift, as well as a rotation.
 * The [M][j] element of the matrix will be the j'th ordinate of the moved origin.
 * The [i][N] element of the matrix will be 0 for i less than M, and 1 for i equals M.
 *
 */
public class AffineTransform extends MathTransform {
    /**
     * Saved inverse transform
     */
    private IMathTransform _inverse;
    /**
     * Dimension of source points - it's related to number of transformation matrix rows
     */
    private int dimSource;
    /**
     * Dimension of output points - it's related to number of columns
     */
    private int dimTarget;
    /**
     * Represents transform matrix of this affine transformation from input points to output ones using dimensionality defined within the affine transform
     * Number of rows = dimTarget + 1
     * Number of columns = dimSource + 1
     */
    private double[][] transformMatrix;

    /**
     * Creates instance of 2D affine transform (source dimensionality 2, target dimensionality 2) using the specified values
     *
     * @param m00 Value for row 0, column 0 - AKA ScaleX
     * @param m01 Value for row 0, column 1 - AKA ShearX
     * @param m02 Value for row 0, column 2 - AKA Translate X
     * @param m10 Value for row 1, column 0 - AKA Shear Y
     * @param m11 Value for row 1, column 1 - AKA Scale Y
     * @param m12 Value for row 1, column 2 - AKA Translate Y
     * @throws Exception the exception
     */
    public AffineTransform(double m00, double m01, double m02, double m10, double m11, double m12) throws Exception {
        super();
        //fill dimensionlity
        dimSource = 2;
        dimTarget = 2;
        //create matrix - 2D affine transform uses 3x3 matrix (3rd row is the special one)
        transformMatrix = new double[][]{{m00, m01, m02}, {m10, m11, m12}, {0, 0, 1}};
    }

    /**
     * Creates instance of affine transform using the specified matrix.
     * If the transform's input dimension is M, and output dimension is N, then the matrix will have size [N+1][M+1].
     * The +1 in the matrix dimensions allows the matrix to do a shift, as well as a rotation. The [M][j] element of the matrix will be the j'th ordinate of the moved origin. The [i][N] element of the matrix will be 0 for i less than M, and 1 for i equals M.
     *
     * @param matrix Matrix used to create afiine transform
     * @throws Exception the exception
     */
    public AffineTransform(double[][] matrix) throws Exception {
        super();
        //check validity
        if (matrix == null) {
            throw new IllegalArgumentException("matrix is null");
        }

        if (matrix[0].length <= 1) {
            throw new IllegalArgumentException("Transformation matrix must have at least 2 rows.");
        }

        if (matrix[1].length <= 1) {
            throw new IllegalArgumentException("Transformation matrix must have at least 2 columns.");
        }

        //fill dimensionlity - dimension is M, and output dimension is N, then the matrix will have size [N+1][M+1].
        dimSource = matrix[1].length - 1;
        dimTarget = matrix[0].length - 1;
        //use specified matrix
        transformMatrix = matrix;
    }

    /**
     * Given L,U,P and b solve for x.
     * Input the L and U matrices as a single matrix LU.
     * Return the solution as a double[].
     * LU will be a n+1xm+1 matrix where the first row and columns are zero.
     * This is for ease of computation and consistency with Cormen et al.
     * pseudocode.
     * The pi array represents the permutation matrix.
     *
     * @param LU
     * @param pi
     * @param b
     * @return
     * @see
     */
    private static double[] lUPSolve(double[][] LU, int[] pi, double[] b) throws Exception {
        int n = LU[0].length - 1;
        double[] x = new double[n + 1];
        double[] y = new double[n + 1];
        double suml = 0;
        double sumu = 0;
        double lij = 0;
        for (int i = 0; i <= n; i++) {
            /*
                        * Solve for y using formward substitution
                        * */
            suml = 0;
            for (int j = 0; j <= i - 1; j++) {
                /*
                                    * since we've taken L and U as a singular matrix as an input
                                    * the value for L at index i and j will be 1 when i equals j, not LU[i][j], since
                                    * the diagonal values are all 1 for L.
                                    * */
                if (i == j) {
                    lij = 1;
                } else {
                    lij = LU[i][j];
                }
                suml = suml + (lij * y[j]);
            }
            y[i] = b[pi[i]] - suml;
        }
        for (int i = n; i >= 0; i--) {
            //Solve for x by using back substitution
            sumu = 0;
            for (int j = i + 1; j <= n; j++) {
                sumu = sumu + (LU[i][j] * x[j]);
            }
            x[i] = (y[i] - sumu) / LU[i][i];
        }
        return x;
    }

    /**
     * Perform LUP decomposition on a matrix A.
     * Return L and U as a single matrix(double[][]) and P as an array of ints.
     * We implement the code to compute LU "in place" in the matrix A.
     * In order to make some of the calculations more straight forward and to
     * match Cormen's et al. pseudocode the matrix A should have its first row and first columns
     * to be all 0.
     *
     * @param A
     * @return
     * @see
     */
    private static Pair<double[][], int[]> lUPDecomposition(double[][] A) throws Exception {
        int n = A[0].length - 1;
        /*
                    * pi represents the permutation matrix.  We implement it as an array
                    * whose value indicates which column the 1 would appear.  We use it to avoid
                    * dividing by zero or small numbers.
                    * */
        int[] pi = new int[n + 1];
        double p = 0;
        int kp = 0;
        int pik = 0;
        int pikp = 0;
        double aki = 0;
        double akpi = 0;
        for (int j = 0; j <= n; j++) {
            //Initialize the permutation matrix, will be the identity matrix
            pi[j] = j;
        }
        for (int k = 0; k <= n; k++) {
            /*
                            * In finding the permutation matrix p that avoids dividing by zero
                            * we take a slightly different approach.  For numerical stability
                            * We find the element with the largest
                            * absolute value of those in the current first column (column k).  If all elements in
                            * the current first column are zero then the matrix is singluar and throw an
                            * error.
                            * */
            p = 0;
            for (int i = k; i <= n; i++) {
                if (Math.abs(A[i][k]) > p) {
                    p = Math.abs(A[i][k]);
                    kp = i;
                }

            }
            if (p == 0) {
                throw new Exception("singular matrix");
            }

            /*
                            * These lines update the pivot array (which represents the pivot matrix)
                            * by exchanging pi[k] and pi[kp].
                            * */
            pik = pi[k];
            pikp = pi[kp];
            pi[k] = pikp;
            pi[kp] = pik;
            for (int i = 0; i <= n; i++) {
                /*
                                * Exchange rows k and kpi as determined by the pivot
                                * */
                aki = A[k][i];
                akpi = A[kp][i];
                A[k][i] = akpi;
                A[kp][i] = aki;
            }
            for (int i = k + 1; i <= n; i++) {
                /*
                                    * Compute the Schur complement
                                    * */
                A[i][k] = A[i][k] / A[k][k];
                for (int j = k + 1; j <= n; j++) {
                    A[i][j] = A[i][j] - (A[i][k] * A[k][j]);
                }
            }
        }
        return new Pair<>(A, pi);
    }

    /**
     * Given an nXn matrix A, solve n linear equations to find the inverse of A.
     *
     * @param A
     * @return
     * @see
     */
    private static double[][] invertMatrix(double[][] A) throws Exception {
        int n = A[0].length;
        int m = A[1].length;
        //x will hold the inverse matrix to be returned
        double[][] x = new double[n][m];
        /*
                    * solve will contain the vector solution for the LUP decomposition as we solve
                    * for each vector of x.  We will combine the solutions into the double[][] array x.
                    * */
        double[] solve;
        //Get the LU matrix and P matrix (as an array)
        Pair<double[][], int[]> results = lUPDecomposition(A);
        double[][] LU = results.first;//.getKey();
        int[] P = results.second;//.getValue();
        for (int i = 0; i < n; i++) {
            /*
                        * Solve AX = e for each column ei of the identity matrix using LUP decomposition
                        * */
            //e will represent each column in the identity matrix
            double[] e = new double[m];
            e[i] = 1;
            solve = lUPSolve(LU, P, e);
            for (int j = 0; j < solve.length; j++) {
                x[j][i] = solve[j];
            }
        }
        return x;
    }

    /**
     * Gets a Well-Known text representation of this affine math transformation.
     */
    public String getWKT() throws Exception {
        //PARAM_MT["Affine",
        //    PARAMETER["num_row",3],
        //    PARAMETER["num_col",3],
        //    PARAMETER["elt_0_1",1],
        //    PARAMETER["elt_0_2",2],
        //    PARAMETER["elt 1 2",3]]
        StringBuilder sb = new StringBuilder();
        sb.append("PARAM_MT[\"Affine\"");
        for (ProjectionParameter param : this.getParameterValues()) {
            sb.append(",");
            sb.append(param.getWKT());
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Gets an XML representation of this affine transformation.
     */
    public String getXML() throws Exception {
        throw new UnsupportedOperationException("The method or operation is not implemented.");
    }

    /**
     * Gets the dimension of input points.
     */
    public int getDimSource() throws Exception {
        return dimSource;
    }

    /**
     * Gets the dimension of output points.
     */
    public int getDimTarget() throws Exception {
        return dimTarget;
    }

    /**
     * Return affine transformation matrix as group of parameter values that maiy be used for retrieving WKT of this affine transform
     *
     * @return List of string pairs NAME VALUE
     */
    private List<ProjectionParameter> getParameterValues() throws Exception {
        int rowCnt = transformMatrix[0].length;
        int colCnt = transformMatrix[1].length;
        List<ProjectionParameter> pInfo = new ArrayList<ProjectionParameter>();
        pInfo.add(new ProjectionParameter("num_row", rowCnt));
        pInfo.add(new ProjectionParameter("num_col", colCnt));
        for (int row = 0; row < rowCnt; row++) {
            for (int col = 0; col < colCnt; col++) {
                //fill matrix values
                String name = String.format("elt_%d_%d", row, col);
                pInfo.add(new ProjectionParameter(name, transformMatrix[row][col]));
            }
        }
        return pInfo;
    }

    /**
     * Returns the inverse of this affine transformation.
     *
     * @return IMathTransform that is the reverse of the current affine transformation.
     */
    public IMathTransform inverse() throws Exception {
        if (_inverse == null) {
            //find the inverse transformation matrix - use cloned matrix array
            //remarks about dimensionality: if input dimension is M, and output dimension is N, then the matrix will have size [N+1][M+1].
            double[][] invMatrix = invertMatrix((double[][]) transformMatrix.clone());
            _inverse = new AffineTransform(invMatrix);
        }

        return _inverse;
    }

    /**
     * Transforms a coordinate point. The passed parameter point should not be modified.
     *
     * @param point point
     * @return point
     */
    public double[] transform(double[] point) throws Exception {
        //check source dimensionality - alow coordinate clipping, if source dimensionality is greater then expected source dimensionality of affine transformation
        if (point.length >= dimSource) {
            //use transformation matrix to create output points that has dimTarget dimensionality
            double[] transformed = new double[dimTarget];
            for (int row = 0; row < dimTarget; row++) {
                //count each target dimension using the apropriate row
                //start with the last value which is in fact multiplied by 1
                double dimVal = transformMatrix[row][dimSource];
                for (int col = 0; col < dimSource; col++) {
                    dimVal += transformMatrix[row][col] * point[col];
                }
                transformed[row] = dimVal;
            }
            return transformed;
        }

        throw new Exception("Dimensionality of point is not supported!");
    }

    //nepodporovane

    /**
     * Reverses the transformation
     */
    public void invert() throws Exception {
        throw new UnsupportedOperationException("The method or operation is not implemented.");
    }

    /**
     * Returns this affine transform as an affine transform matrix.
     *
     * @return point double [ ] [ ]
     * @throws Exception the exception
     */
    public double[][] getMatrix() throws Exception {
        return (double[][]) this.transformMatrix.clone();
    }

}


