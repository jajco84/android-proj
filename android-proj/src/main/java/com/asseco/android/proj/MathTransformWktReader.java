package com.asseco.android.proj;

import com.asseco.android.proj.transformations.AffineTransform;
import com.asseco.android.proj.transformations.IMathTransform;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates an math transform based on the supplied Well Known Text (WKT).
 */
public class MathTransformWktReader {
    /**
     * Reads and parses a WKT-formatted projection string.
     *
     * @param wkt String containing WKT.
     * @return Object representation of the WKT.
     * @throws Exception the exception
     */
    public static IMathTransform parse(String wkt) throws Exception {
        if (wkt == null || wkt == "")
            throw new Exception("wkt is null");

        // if (encoding == null)
        //     throw new ArgumentNullException("encoding");

        // byte[] arr = encoding.GetBytes(wkt);
        // Stream stream = new MemoryStream(arr);
        try {
            {
                StringReader reader = new StringReader(wkt);//, encoding);
                try {
                    {
                        WktStreamTokenizer tokenizer = new WktStreamTokenizer(reader);
                        tokenizer.nextToken();
                        String objectName = tokenizer.getStringValue();
                        String __dummyScrutVar0 = objectName;
                        if (__dummyScrutVar0.equals("PARAM_MT")) {
                            return readMathTransform(tokenizer);
                        } else {
                            throw new IllegalArgumentException(String.format("'%s' is not recognized.", objectName));
                        }
                    }
                } finally {
                    //   if (reader != null)
                    //     Disposable.mkDisposable(reader).dispose();

                }
            }
        } finally {
            //    if (stream != null)
            //      Disposable.mkDisposable(stream).dispose();

        }
    }

    /**
     * Reads math transform from using current token from the specified tokenizer
     *
     * @param tokenizer the tokenizer
     * @return math transform
     * @throws Exception the exception
     */
    public static IMathTransform readMathTransform(WktStreamTokenizer tokenizer) throws Exception {
        if (!tokenizer.getStringValue().equals("PARAM_MT"))
            tokenizer.readToken("PARAM_MT");

        tokenizer.readToken("[");
        String transformName = tokenizer.readDoubleQuotedWord();
        tokenizer.readToken(",");
        String __dummyScrutVar1 = transformName.toUpperCase();
        if (__dummyScrutVar1.equals("AFFINE")) {
            return readAffineTransform(tokenizer);
        } else {
            throw new Exception("Transform not supported '" + transformName + "'");
        }
    }

    private static IParameterInfo readParameters(WktStreamTokenizer tokenizer) throws Exception {
        List<Parameter> paramList = new ArrayList<Parameter>();
        while (tokenizer.getStringValue().equals("PARAMETER")) {
            tokenizer.readToken("[");
            String paramName = tokenizer.readDoubleQuotedWord();
            tokenizer.readToken(",");
            tokenizer.nextToken();
            double paramValue = tokenizer.getNumericValue();
            tokenizer.readToken("]");
            //test, whether next parameter is delimited by comma
            tokenizer.nextToken();
            if (!tokenizer.getStringValue().equals("]"))
                tokenizer.nextToken();

            paramList.add(new Parameter(paramName, paramValue));
        }
        IParameterInfo info = new ParameterInfo();
        info.setParameters(paramList);
        return info;
    }

    private static IMathTransform readAffineTransform(WktStreamTokenizer tokenizer) throws Exception {
        /*
                         PARAM_MT[
                            "Affine",
                            PARAMETER["num_row",3],
                            PARAMETER["num_col",3],
                            PARAMETER["elt_0_0", 0.883485346527455],
                            PARAMETER["elt_0_1", -0.468458794848877],
                            PARAMETER["elt_0_2", 3455869.17937689],
                            PARAMETER["elt_1_0", 0.468458794848877],
                            PARAMETER["elt_1_1", 0.883485346527455],
                            PARAMETER["elt_1_2", 5478710.88035753],
                            PARAMETER["elt_2_2", 1]
                         ]
                    */
        //tokenizer stands on the first PARAMETER
        if (!tokenizer.getStringValue().equals("PARAMETER"))
            tokenizer.readToken("PARAMETER");

        IParameterInfo paramInfo = readParameters(tokenizer);
        //manage required parameters - row, col
        Parameter rowParam = paramInfo.getParameterByName("num_row");
        Parameter colParam = paramInfo.getParameterByName("num_col");
        if (rowParam == null) {
            throw new Exception("Affine transform does not contain 'num_row' parameter");
        }

        if (colParam == null) {
            throw new Exception("Affine transform does not contain 'num_col' parameter");
        }

        int rowVal = (int) rowParam.getValue();
        int colVal = (int) colParam.getValue();
        if (rowVal <= 0) {
            throw new IllegalArgumentException("Affine transform contains invalid value of 'num_row' parameter");
        }

        if (colVal <= 0) {
            throw new IllegalArgumentException("Affine transform contains invalid value of 'num_col' parameter");
        }

        //creates working matrix;
        double[][] matrix = new double[rowVal][colVal];
        for (Parameter param : paramInfo.getParameters()) {
            //simply process matrix values - no elt_ROW_COL parsing
            if (param == null || param.getName() == null) {
                continue;
            }

            String __dummyScrutVar2 = param.getName();
            if (__dummyScrutVar2.equals("num_row") || __dummyScrutVar2.equals("num_col")) {
            } else if (__dummyScrutVar2.equals("elt_0_0")) {
                matrix[0][0] = param.getValue();
            } else if (__dummyScrutVar2.equals("elt_0_1")) {
                matrix[0][1] = param.getValue();
            } else if (__dummyScrutVar2.equals("elt_0_2")) {
                matrix[0][2] = param.getValue();
            } else if (__dummyScrutVar2.equals("elt_0_3")) {
                matrix[0][3] = param.getValue();
            } else if (__dummyScrutVar2.equals("elt_1_0")) {
                matrix[1][0] = param.getValue();
            } else if (__dummyScrutVar2.equals("elt_1_1")) {
                matrix[1][1] = param.getValue();
            } else if (__dummyScrutVar2.equals("elt_1_2")) {
                matrix[1][2] = param.getValue();
            } else if (__dummyScrutVar2.equals("elt_1_3")) {
                matrix[1][3] = param.getValue();
            } else if (__dummyScrutVar2.equals("elt_2_0")) {
                matrix[2][0] = param.getValue();
            } else if (__dummyScrutVar2.equals("elt_2_1")) {
                matrix[2][1] = param.getValue();
            } else if (__dummyScrutVar2.equals("elt_2_2")) {
                matrix[2][2] = param.getValue();
            } else if (__dummyScrutVar2.equals("elt_2_3")) {
                matrix[2][3] = param.getValue();
            } else if (__dummyScrutVar2.equals("elt_3_0")) {
                matrix[3][0] = param.getValue();
            } else if (__dummyScrutVar2.equals("elt_3_1")) {
                matrix[3][1] = param.getValue();
            } else if (__dummyScrutVar2.equals("elt_3_2")) {
                matrix[3][2] = param.getValue();
            } else if (__dummyScrutVar2.equals("elt_3_3")) {
                matrix[3][3] = param.getValue();
            } else {
            }
        }
        //unknown parameter
        //read rest of WKT
        if (!tokenizer.getStringValue().equals("]"))
            tokenizer.readToken("]");

        //use "matrix" constructor to create transformation matrix
        IMathTransform affineTransform = new AffineTransform(matrix);
        return affineTransform;
    }

}


