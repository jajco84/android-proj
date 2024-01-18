package com.asseco.android.proj;

import java.util.List;

/**
 * The GeographicTransform class is implemented on geographic transformation objects and
 * implements datum transformations between geographic coordinate systems.
 */
public class GeographicTransform extends Info implements IGeographicTransform {
    private IGeographicCoordinateSystem _SourceGCS;
    private IGeographicCoordinateSystem _TargetGCS;

    /**
     * Instantiates a new Geographic transform.
     *
     * @param name         the name
     * @param authority    the authority
     * @param code         the code
     * @param alias        the alias
     * @param remarks      the remarks
     * @param abbreviation the abbreviation
     * @param sourceGCS    the source gcs
     * @param targetGCS    the target gcs
     * @throws Exception the exception
     */
    public GeographicTransform(String name, String authority, long code, String alias, String remarks, String abbreviation, IGeographicCoordinateSystem sourceGCS, IGeographicCoordinateSystem targetGCS) throws Exception {
        super(name, authority, code, alias, abbreviation, remarks);
        _SourceGCS = sourceGCS;
        _TargetGCS = targetGCS;
    }

    /**
     * Gets or sets the source geographic coordinate system for the transformation.
     */
    public IGeographicCoordinateSystem getSourceGCS() throws Exception {
        return _SourceGCS;
    }

    public void setSourceGCS(IGeographicCoordinateSystem value) throws Exception {
        _SourceGCS = value;
    }

    /**
     * Gets or sets the target geographic coordinate system for the transformation.
     */
    public IGeographicCoordinateSystem getTargetGCS() throws Exception {
        return _TargetGCS;
    }

    public void setTargetGCS(IGeographicCoordinateSystem value) throws Exception {
        _TargetGCS = value;
    }

    /**
     * Returns an accessor interface to the parameters for this geographic transformation.
     */
    public IParameterInfo getParameterInfo() throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * Transforms an array of points from the source geographic coordinate
     * system to the target geographic coordinate system.
     *
     * @param points On input points in the source geographic coordinate system
     * @return Output points in the target geographic coordinate system
     */
    public List<double[]> forward(List<double[]> points) throws Exception {
        throw new UnsupportedOperationException();
    }

    /*
                List<Point> trans = new List<Point>(points.Count);
    			foreach (Point p in points)
    			{
    			}
    			return trans;
    			*/

    /**
     * Transforms an array of points from the target geographic coordinate
     * system to the source geographic coordinate system.
     *
     * @param points Input points in the target geographic coordinate system,
     * @return Output points in the source geographic coordinate system
     */
    public List<double[]> inverse(List<double[]> points) throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
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

    /**
     * Checks whether the values of this instance is equal to the values of another instance.
     * Only parameters used for coordinate system are used for comparison.
     * Name, abbreviation, authority, alias and remarks are ignored in the comparison.
     *
     * @param obj
     * @return True if equal
     */
    public boolean equalParams(Object obj) throws Exception {
        if (!(obj instanceof GeographicTransform))
            return false;

        GeographicTransform gt = (GeographicTransform)obj;
        return gt.getSourceGCS().equalParams(this.getSourceGCS()) && gt.getTargetGCS().equalParams(this.getTargetGCS());
    }

}


