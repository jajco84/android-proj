package com.asseco.android.proj.transformations;

import com.asseco.android.proj.Wgs84ConversionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Transformation for applying
 */
public class DatumTransform extends MathTransform {
    /**
     * The Inverse.
     */
    protected IMathTransform _inverse;
    /**
     * The V.
     */
    double[] v;
    private Wgs84ConversionInfo _toWgs94;
    private boolean _isinverse;

    /**
     * Initializes a new instance of the
     * {@link #DatumTransform}
     * class.
     *
     * @param towgs84 the towgs 84
     * @throws Exception the exception
     */
    public DatumTransform(Wgs84ConversionInfo towgs84) throws Exception {
        this(towgs84, false);
    }

    private DatumTransform(Wgs84ConversionInfo towgs84, boolean isinverse) throws Exception {
        _toWgs94 = towgs84;
        v = _toWgs94.getAffineTransform();
        _isinverse = isinverse;
    }

    /**
     * Gets a Well-Known text representation of this object.
     */
    public String getWKT() throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets an XML representation of this object.
     */
    public String getXML() throws Exception {
        throw new UnsupportedOperationException();
    }

    public int getDimSource() throws Exception {
        return 3;
    }

    public int getDimTarget() throws Exception {
        return 3;
    }

    /**
     * Creates the inverse transform of this object.
     *
     * @return This method may fail if the transform is not one to one. However, all cartographic projections should succeed.
     */
    public IMathTransform inverse() throws Exception {
        if (_inverse == null)
            _inverse = new DatumTransform(_toWgs94, !_isinverse);

        return _inverse;
    }

    /**
     * Transforms a coordinate point.
     *
     * @param p
     * @return
     * @see
     */
    private double[] apply(double[] p) throws Exception {
        return new double[]{v[0] * (p[0] - v[3] * p[1] + v[2] * p[2]) + v[4], v[0] * (v[3] * p[0] + p[1] - v[1] * p[2]) + v[5], v[0] * (-v[2] * p[0] + v[1] * p[1] + p[2]) + v[6]};
    }

    /**
     * For the reverse transformation, each element is multiplied by -1.
     *
     * @param p
     * @return
     * @see
     */
    private double[] applyInverted(double[] p) throws Exception {
        return new double[]{(1 - (v[0] - 1)) * (p[0] + v[3] * p[1] - v[2] * p[2]) - v[4], (1 - (v[0] - 1)) * (-v[3] * p[0] + p[1] + v[1] * p[2]) - v[5], (1 - (v[0] - 1)) * (v[2] * p[0] - v[1] * p[1] + p[2]) - v[6]};
    }

    /**
     * Transforms a coordinate point. The passed parameter point should not be modified.
     *
     * @param point
     * @return
     */
    public double[] transform(double[] point) throws Exception {
        if (!_isinverse)
            return apply(point);
        else
            return applyInverted(point);
    }

    /**
     * Transforms a list of coordinate point ordinal values.
     *
     * @param points
     * @return This method is provided for efficiently transforming many points. The supplied array
     * of ordinal values will contain packed ordinal values. For example, if the source
     * dimension is 3, then the ordinals will be packed in this order (x0,y0,z0,x1,y1,z1 ...).
     * The size of the passed array must be an integer multiple of DimSource. The returned
     * ordinal values are packed in a similar way. In some DCPs. the ordinals may be
     * transformed in-place, and the returned array may be the same as the passed array.
     * So any client code should not attempt to reuse the passed ordinal values (although
     * they can certainly reuse the passed array). If there is any problem then the server
     * implementation will throw an exception. If this happens then the client should not
     * make any assumptions about the state of the ordinal values.
     */
    public List<double[]> transformList(List<double[]> points) throws Exception {
        List<double[]> pnts = new ArrayList<double[]>(points.size());
        for (double[] p : points)
            pnts.add(transform(p));
        return pnts;
    }

   /* public IList<Coordinate> transformList(IList<Coordinate> points) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var"  pnts = new List<Coordinate>(points.Count);
        for (/* [UNSUPPORTED] 'var' as type is unsupported "var"  p : points)
            pnts.Add(Transform(p));
        return pnts;
    }*/

    /**
     * Reverses the transformation
     */
    public void invert() throws Exception {
        _isinverse = !_isinverse;
    }

}


