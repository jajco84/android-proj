package com.asseco.android.proj;

import java.util.List;

/**
 * A 3D coordinate system, with its origin at the center of the Earth.
 */
public class GeocentricCoordinateSystem extends CoordinateSystem implements IGeocentricCoordinateSystem {
    private IHorizontalDatum _HorizontalDatum;
    private ILinearUnit _LinearUnit;
    private IPrimeMeridian _Primemeridan;

    /**
     * Instantiates a new Geocentric coordinate system.
     *
     * @param datum         the datum
     * @param linearUnit    the linear unit
     * @param primeMeridian the prime meridian
     * @param AxisInfo      the axis info
     * @param name          the name
     * @param authority     the authority
     * @param code          the code
     * @param alias         the alias
     * @param remarks       the remarks
     * @param abbreviation  the abbreviation
     * @throws Exception the exception
     */
    public GeocentricCoordinateSystem(IHorizontalDatum datum, ILinearUnit linearUnit, IPrimeMeridian primeMeridian, List<AxisInfo> AxisInfo, String name, String authority, long code, String alias, String remarks, String abbreviation) throws Exception {
        super(name, authority, code, alias, abbreviation, remarks);
        _HorizontalDatum = datum;
        _LinearUnit = linearUnit;
        _Primemeridan = primeMeridian;
        if (AxisInfo.size() != 3)
            throw new IllegalArgumentException("Axis info should contain three axes for geocentric coordinate systems");

        super.setAxisInfo(AxisInfo);
    }

    /**
     * Creates a geocentric coordinate system based on the WGS84 ellipsoid, suitable for GPS measurements
     *
     * @return the wgs 84
     * @throws Exception the exception
     */
    public static IGeocentricCoordinateSystem getWGS84() throws Exception {
        return new CoordinateSystemFactory().createGeocentricCoordinateSystem("WGS84 Geocentric", HorizontalDatum.getWGS84(), LinearUnit.getMetre(), PrimeMeridian.getGreenwich());
    }

    /**
     * Returns the HorizontalDatum. The horizontal datum is used to determine where
     * the centre of the Earth is considered to be. All coordinate points will be
     * measured from the centre of the Earth, and not the surface.
     */
    public IHorizontalDatum getHorizontalDatum() throws Exception {
        return _HorizontalDatum;
    }

    public void setHorizontalDatum(IHorizontalDatum value) throws Exception {
        _HorizontalDatum = value;
    }

    /**
     * Gets the units used along all the axes.
     */
    public ILinearUnit getLinearUnit() throws Exception {
        return _LinearUnit;
    }

    public void setLinearUnit(ILinearUnit value) throws Exception {
        _LinearUnit = value;
    }

    /**
     * Gets units for dimension within coordinate system. Each dimension in
     * the coordinate system has corresponding units.
     *
     * @param dimension Dimension
     * @return Unit
     */
    public IUnit getUnits(int dimension) throws Exception {
        return _LinearUnit;
    }

    /**
     * Returns the PrimeMeridian.
     */
    public IPrimeMeridian getPrimeMeridian() throws Exception {
        return _Primemeridan;
    }

    public void setPrimeMeridian(IPrimeMeridian value) throws Exception {
        _Primemeridan = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    public String getWKT() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("GEOCCS[\"%s\", %s, %s, %s", getName(), getHorizontalDatum().getWKT(), getPrimeMeridian().getWKT(), getLinearUnit().getWKT()));
        //Skip axis info if they contain default values
        if (getAxisInfo().size() != 3 || !getAxisInfo().get(0).getName().equals("X") || getAxisInfo().get(0).getOrientation() != AxisOrientationEnum.Other || !getAxisInfo().get(1).getName().equals("Y") || getAxisInfo().get(1).getOrientation() != AxisOrientationEnum.East || !getAxisInfo().get(2).getName().equals("Z") || getAxisInfo().get(2).getOrientation() != AxisOrientationEnum.North)
            for (int i = 0; i < getAxisInfo().size(); i++)
                sb.append(String.format(", %s", getAxis(i).getWKT()));

        if (getAuthority() != null && getAuthority() != "" && getAuthorityCode() > 0)
            sb.append(String.format(", AUTHORITY[\"%s\", \"%d\"]", getAuthority(), getAuthorityCode()));

        sb.append("]");
        return sb.toString();
    }

    /**
     * Gets an XML representation of this object
     */
    public String getXML() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<CS_CoordinateSystem Dimension=\"%d\"><CS_GeocentricCoordinateSystem>%s", this.getDimension(), getInfoXml()));
        for (AxisInfo ai : this.getAxisInfo()) {
            sb.append(ai.getXML());
        }
        sb.append(String.format("%s%s%s</CS_GeocentricCoordinateSystem></CS_CoordinateSystem>", getHorizontalDatum().getXML(), getLinearUnit().getXML(), getPrimeMeridian().getXML()));
        return sb.toString();
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
        if (!(obj instanceof GeocentricCoordinateSystem))
            return false;

        GeocentricCoordinateSystem gcc = (GeocentricCoordinateSystem)obj;
        return gcc.getHorizontalDatum().equalParams(this.getHorizontalDatum()) && gcc.getLinearUnit().equalParams(this.getLinearUnit()) && gcc.getPrimeMeridian().equalParams(this.getPrimeMeridian());
    }

}


