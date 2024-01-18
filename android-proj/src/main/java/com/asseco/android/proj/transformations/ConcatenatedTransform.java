package com.asseco.android.proj.transformations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The type Concatenated transform.
 */
public class ConcatenatedTransform extends MathTransform implements Cloneable {
    /**
     * The Inverse.
     */
    protected IMathTransform _inverse;
    private List<ICoordinateTransformation> _coordinateTransformationList = new ArrayList<ICoordinateTransformation>();

    /**
     * Instantiates a new Concatenated transform.
     *
     * @throws Exception the exception
     */
    public ConcatenatedTransform() throws Exception {
        this(new ArrayList<ICoordinateTransformation>());
    }

    /**
     * Instantiates a new Concatenated transform.
     *
     * @param transformlist the transformlist
     * @throws Exception the exception
     */
    public ConcatenatedTransform(List<ICoordinateTransformation> transformlist) throws Exception {
        _coordinateTransformationList = transformlist;
    }

    /**
     * Gets coordinate transformation list.
     *
     * @return the coordinate transformation list
     * @throws Exception the exception
     */
    public List<ICoordinateTransformation> getCoordinateTransformationList() throws Exception {
        return _coordinateTransformationList;
    }

    /**
     * Sets coordinate transformation list.
     *
     * @param value the value
     * @throws Exception the exception
     */
    public void setCoordinateTransformationList(List<ICoordinateTransformation> value) throws Exception {
        _coordinateTransformationList = value;
        _inverse = null;
    }

    public int getDimSource() throws Exception {
        return _coordinateTransformationList.get(0).getSourceCS().getDimension();
    }

    public int getDimTarget() throws Exception {
        return _coordinateTransformationList.get(_coordinateTransformationList.size() - 1).getTargetCS().getDimension();
    }

    /**
     * Transforms a point
     *
     * @param point point
     * @return transformed point
     */
    public double[] transform(double[] point) throws Exception {
        for (Object __dummyForeachVar0 : _coordinateTransformationList) {
            ICoordinateTransformation ct = (ICoordinateTransformation) __dummyForeachVar0;
            point = ct.getMathTransform().transform(point);
        }
        return point;
    }

    /**
     * Transforms a list point
     *
     * @param points points
     * @return transformed points
     */
    public List<double[]> transformList(List<double[]> points) throws Exception {
        List<double[]> pnts = new ArrayList<double[]>(points);
        for (ICoordinateTransformation ct : _coordinateTransformationList) {
            pnts = ct.getMathTransform().transformList(pnts);
        }
        return pnts;
    }

    /*public IList<Coordinate> transformList(IList<Coordinate> points) throws Exception {
        IList<Coordinate> pnts = new List<Coordinate>(points);
        for (/* [UNSUPPORTED] 'var' as type is unsupported "var"  ct : _coordinateTransformationList)
        {
            pnts = ct.MathTransform.TransformList(pnts);
        }
        return pnts;
    }*/

  /*  public ICoordinateSequence transform(ICoordinateSequence coordinateSequence) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var"  res = (ICoordinateSequence)coordinateSequence.Clone();
        for (/* [UNSUPPORTED] 'var' as type is unsupported "var"  ct : _coordinateTransformationList)
        {
            res = ct.MathTransform.Transform(res);
        }
        return res;
    }*/

    /**
     * Returns the inverse of this conversion.
     *
     * @return IMathTransform that is the reverse of the current conversion.
     */
    public IMathTransform inverse() throws Exception {
        if (_inverse == null) {
            _inverse = clone();
            _inverse.invert();
        }

        return _inverse;
    }

    /**
     * Reverses the transformation
     */
    public void invert() throws Exception {

        Collections.reverse(_coordinateTransformationList);

        for (ICoordinateTransformation ic : _coordinateTransformationList) {
            ic.getMathTransform().invert();
        }
    }

    public ConcatenatedTransform clone() {
        try {
            List<ICoordinateTransformation> clonedList = new ArrayList<ICoordinateTransformation>(_coordinateTransformationList.size());
            for (Object __dummyForeachVar5 : _coordinateTransformationList) {
                ICoordinateTransformation ct = (ICoordinateTransformation) __dummyForeachVar5;
                clonedList.add(ct);
            }
            return new ConcatenatedTransform(clonedList);
        } catch (RuntimeException __dummyCatchVar0) {
            throw __dummyCatchVar0;
        } catch (Exception __dummyCatchVar0) {
            throw new RuntimeException(__dummyCatchVar0);
        }

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

}


