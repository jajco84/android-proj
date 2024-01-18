package com.asseco.android.proj.transformations;

import com.asseco.android.proj.IGeographicCoordinateSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * The GeographicTransform class is implemented on geographic transformation objects and
 * implements datum transformations between geographic coordinate systems.
 */
public class GeographicTransform extends MathTransform {
    private IGeographicCoordinateSystem _SourceGCS;
    private IGeographicCoordinateSystem _TargetGCS;

    /**
     * Instantiates a new Geographic transform.
     *
     * @param sourceGCS the source gcs
     * @param targetGCS the target gcs
     * @throws Exception the exception
     */
    public GeographicTransform(IGeographicCoordinateSystem sourceGCS, IGeographicCoordinateSystem targetGCS) throws Exception {
        _SourceGCS = sourceGCS;
        _TargetGCS = targetGCS;
    }

    /**
     * Gets or sets the source geographic coordinate system for the transformation.
     *
     * @return the source gcs
     * @throws Exception the exception
     */
    public IGeographicCoordinateSystem getSourceGCS() throws Exception {
        return _SourceGCS;
    }

    /**
     * Sets source gcs.
     *
     * @param value the value
     * @throws Exception the exception
     */
    public void setSourceGCS(IGeographicCoordinateSystem value) throws Exception {
        _SourceGCS = value;
    }

    /**
     * Gets or sets the target geographic coordinate system for the transformation.
     *
     * @return the target gcs
     * @throws Exception the exception
     */
    public IGeographicCoordinateSystem getTargetGCS() throws Exception {
        return _TargetGCS;
    }

    /**
     * Sets target gcs.
     *
     * @param value the value
     * @throws Exception the exception
     */
    public void setTargetGCS(IGeographicCoordinateSystem value) throws Exception {
        _TargetGCS = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification. [NOT IMPLEMENTED].
     */
    public String getWKT() throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets an XML representation of this object [NOT IMPLEMENTED].
     */
    public String getXML() throws Exception {
        throw new UnsupportedOperationException();
    }

    public int getDimSource() throws Exception {
        return _SourceGCS.getDimension();
    }

    public int getDimTarget() throws Exception {
        return _TargetGCS.getDimension();
    }

    /**
     * Creates the inverse transform of this object.
     * This method may fail if the transform is not one to one. However, all cartographic projections should succeed.
     *
     * @return
     */
    public IMathTransform inverse() throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * Transforms a coordinate point. The passed parameter point should not be modified.
     *
     * @param point
     * @return
     */
    public double[] transform(double[] point) throws Exception {
        double[] pOut = (double[]) point.clone();
        pOut[0] /= getSourceGCS().getangularUnit().getRadiansPerUnit();
        pOut[0] -= getSourceGCS().getPrimeMeridian().getLongitude() / getSourceGCS().getPrimeMeridian().getangularUnit().getRadiansPerUnit();
        pOut[0] += getTargetGCS().getPrimeMeridian().getLongitude() / getTargetGCS().getPrimeMeridian().getangularUnit().getRadiansPerUnit();
        pOut[0] *= getSourceGCS().getangularUnit().getRadiansPerUnit();
        return pOut;
    }

    /**
     * Transforms a list of coordinate point ordinal values.
     *
     * This method is provided for efficiently transforming many points. The supplied array
     * of ordinal values will contain packed ordinal values. For example, if the source
     * dimension is 3, then the ordinals will be packed in this order (x0,y0,z0,x1,y1,z1 ...).
     * The size of the passed array must be an integer multiple of DimSource. The returned
     * ordinal values are packed in a similar way. In some DCPs. the ordinals may be
     * transformed in-place, and the returned array may be the same as the passed array.
     * So any client code should not attempt to reuse the passed ordinal values (although
     * they can certainly reuse the passed array). If there is any problem then the server
     * implementation will throw an exception. If this happens then the client should not
     * make any assumptions about the state of the ordinal values.
     *
     * @param points
     * @return
     */
    public List<double[]> transformList(List<double[]> points) throws Exception {
        List<double[]> trans = new ArrayList<double[]>(points.size());
        for (double[] p : points)
            trans.add(transform(p));
        return trans;
    }

   /* public IList<Coordinate> transformList(IList<Coordinate> points) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var"  trans = new List<Coordinate>(points.Count);
        for (/* [UNSUPPORTED] 'var' as type is unsupported "var"  coordinate : points)
        {
            trans.Add(Transform(coordinate));
        }
        return trans;
    }*/

    /**
     * Reverses the transformation
     */
    public void invert() throws Exception {
        throw new UnsupportedOperationException();
    }

}


