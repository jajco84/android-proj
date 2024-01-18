package com.asseco.android.proj;

import java.util.ArrayList;
import java.util.List;

/**
 * A coordinate system based on latitude and longitude.
 *
 * Some geographic coordinate systems are Lat/Lon, and some are Lon/Lat.
 * You can find out which this is by examining the axes. You should also
 * check the angular units, since not all geographic coordinate systems
 * use degrees.
 */
public class GeographicCoordinateSystem extends HorizontalCoordinateSystem implements IGeographicCoordinateSystem {
    private IAngularUnit _AngularUnit;
    private IPrimeMeridian _PrimeMeridian;
    private List<Wgs84ConversionInfo> _WGS84ConversionInfo = new ArrayList<Wgs84ConversionInfo>();

    /**
     * Creates an instance of a Geographic Coordinate System
     *
     * @param angularUnit     Angular units
     * @param horizontalDatum Horizontal datum
     * @param primeMeridian   Prime meridian
     * @param AxisInfo        Axis info
     * @param name            Name
     * @param authority       Authority name
     * @param authorityCode   Authority-specific identification code.
     * @param alias           Alias
     * @param abbreviation    Abbreviation
     * @param remarks         Provider-supplied remarks
     * @throws Exception the exception
     */
    public GeographicCoordinateSystem(IAngularUnit angularUnit, IHorizontalDatum horizontalDatum, IPrimeMeridian primeMeridian, List<AxisInfo> AxisInfo, String name, String authority, long authorityCode, String alias, String abbreviation, String remarks) throws Exception {
        super(horizontalDatum, AxisInfo, name, authority, authorityCode, alias, abbreviation, remarks);
        _AngularUnit = angularUnit;
        _PrimeMeridian = primeMeridian;
    }

    /**
     * Creates a decimal degrees geographic coordinate system based on the WGS84 ellipsoid, suitable for GPS measurements
     *
     * @return the wgs 84
     * @throws Exception the exception
     */
    public static IGeographicCoordinateSystem getWGS84() throws Exception {
        List<AxisInfo> axes = new ArrayList<AxisInfo>(2);
        axes.add(new AxisInfo("Lon", AxisOrientationEnum.East));
        axes.add(new AxisInfo("Lat", AxisOrientationEnum.North));
        return new GeographicCoordinateSystem(AngularUnit.getDegrees(), HorizontalDatum.getWGS84(), PrimeMeridian.getGreenwich(), axes, "WGS 84", "EPSG", 4326, "", "", "");
    }

    /**
     * Gets or sets the angular units of the geographic coordinate system.
     */
    public IAngularUnit getangularUnit() throws Exception {
        return _AngularUnit;
    }

    public void setangularUnit(IAngularUnit value) throws Exception {
        _AngularUnit = value;
    }

    /**
     * Gets units for dimension within coordinate system. Each dimension in
     * the coordinate system has corresponding units.
     *
     * @param dimension Dimension
     * @return Unit
     */
    public IUnit getUnits(int dimension) throws Exception {
        return _AngularUnit;
    }

    /**
     * Gets or sets the prime meridian of the geographic coordinate system.
     */
    public IPrimeMeridian getPrimeMeridian() throws Exception {
        return _PrimeMeridian;
    }

    public void setPrimeMeridian(IPrimeMeridian value) throws Exception {
        _PrimeMeridian = value;
    }

    /**
     * Gets the number of available conversions to WGS84 coordinates.
     */
    public int getNumConversionToWGS84() throws Exception {
        return _WGS84ConversionInfo.size();
    }

    /**
     * Gets wgs 84 conversion info.
     *
     * @return the wgs 84 conversion info
     * @throws Exception the exception
     */
    public List<Wgs84ConversionInfo> getWGS84ConversionInfo() throws Exception {
        return _WGS84ConversionInfo;
    }

    /**
     * Sets wgs 84 conversion info.
     *
     * @param value the value
     * @throws Exception the exception
     */
    public void setWGS84ConversionInfo(List<Wgs84ConversionInfo> value) throws Exception {
        _WGS84ConversionInfo = value;
    }

    /**
     * Gets details on a conversion to WGS84.
     */
    public Wgs84ConversionInfo getWgs84ConversionInfo(int index) throws Exception {
        return _WGS84ConversionInfo.get(index);
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    public String getWKT() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("GEOGCS[\"%s\", %s, %s, %s", getName(), getHorizontalDatum().getWKT(), getPrimeMeridian().getWKT(), getangularUnit().getWKT()));
        //Skip axis info if they contain default values
        if (getAxisInfo().size() != 2 || !getAxisInfo().get(0).getName().equals("Lon") || getAxisInfo().get(0).getOrientation() != AxisOrientationEnum.East || !getAxisInfo().get(1).getName().equals("Lat") || getAxisInfo().get(1).getOrientation() != AxisOrientationEnum.North)
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
        sb.append(String.format("<CS_CoordinateSystem Dimension=\"%d\"><CS_GeographicCoordinateSystem>%s", this.getDimension(), getInfoXml()));
        for (Object __dummyForeachVar0 : this.getAxisInfo()) {
            AxisInfo ai = (AxisInfo) __dummyForeachVar0;
            sb.append(ai.getXML());
        }
        sb.append(String.format("%s%s%s</CS_GeographicCoordinateSystem></CS_CoordinateSystem>", getHorizontalDatum().getXML(), getangularUnit().getXML(), getPrimeMeridian().getXML()));
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
        if (!(obj instanceof GeographicCoordinateSystem))
            return false;

        GeographicCoordinateSystem gcs = (GeographicCoordinateSystem)obj;
        if (gcs.getDimension() != this.getDimension())
            return false;

        if (this.getWGS84ConversionInfo() != null && gcs.getWGS84ConversionInfo() == null)
            return false;

        if (this.getWGS84ConversionInfo() == null && gcs.getWGS84ConversionInfo() != null)
            return false;

        if (this.getWGS84ConversionInfo() != null && gcs.getWGS84ConversionInfo() != null) {
            if (this.getWGS84ConversionInfo().size() != gcs.getWGS84ConversionInfo().size())
                return false;

            for (int i = 0; i < this.getWGS84ConversionInfo().size(); i++)
                if (!gcs.getWGS84ConversionInfo().get(i).equals(this.getWGS84ConversionInfo().get(i)))
                    return false;

        }

        if (this.getAxisInfo().size() != gcs.getAxisInfo().size())
            return false;

        for (int i = 0; i < gcs.getAxisInfo().size(); i++)
            if (gcs.getAxisInfo().get(i).getOrientation() != this.getAxisInfo().get(i).getOrientation())
                return false;

        return gcs.getangularUnit().equalParams(this.getangularUnit()) && gcs.getHorizontalDatum().equalParams(this.getHorizontalDatum()) && gcs.getPrimeMeridian().equalParams(this.getPrimeMeridian());
    }

}


