package com.asseco.android.proj;

/**
 * Definition of angular units.
 */
public class AngularUnit extends Info implements IAngularUnit {
    /**
     * Equality tolerance value. Values with a difference less than this are considered equal.
     */
    private static final double EqualityTolerance = 2.0e-17;
    private double _RadiansPerUnit;

    /**
     * Initializes a new instance of a angular unit
     *
     * @param radiansPerUnit Radians per unit
     * @throws Exception the exception
     */
    public AngularUnit(double radiansPerUnit) throws Exception {
        this(radiansPerUnit, "", "", -1, "", "", "");
    }

    /**
     * Initializes a new instance of a angular unit
     *
     * @param radiansPerUnit Radians per unit
     * @param name           Name
     * @param authority      Authority name
     * @param authorityCode  Authority-specific identification code.
     * @param alias          Alias
     * @param abbreviation   Abbreviation
     * @param remarks        Provider-supplied remarks
     * @throws Exception the exception
     */
    public AngularUnit(double radiansPerUnit, String name, String authority, long authorityCode, String alias, String abbreviation, String remarks) throws Exception {
        super(name, authority, authorityCode, alias, abbreviation, remarks);
        _RadiansPerUnit = radiansPerUnit;
    }

    /**
     * The angular degrees are PI/180 = 0.017453292519943295769236907684886 radians
     *
     * @return the degrees
     * @throws Exception the exception
     */
    public static AngularUnit getDegrees() throws Exception {
        return new AngularUnit(0.017453292519943295769236907684886, "degree", "EPSG", 9102, "deg", "", "=pi/180 radians");
    }

    /**
     * SI standard unit
     *
     * @return the radian
     * @throws Exception the exception
     */
    public static AngularUnit getRadian() throws Exception {
        return new AngularUnit(1, "radian", "EPSG", 9101, "rad", "", "SI standard unit.");
    }

    /**
     * Pi / 200 = 0.015707963267948966192313216916398 radians
     *
     * @return the grad
     * @throws Exception the exception
     */
    public static AngularUnit getGrad() throws Exception {
        return new AngularUnit(0.015707963267948966192313216916398, "grad", "EPSG", 9105, "gr", "", "=pi/200 radians.");
    }

    /**
     * Pi / 200 = 0.015707963267948966192313216916398 radians
     *
     * @return the gon
     * @throws Exception the exception
     */
    public static AngularUnit getGon() throws Exception {
        return new AngularUnit(0.015707963267948966192313216916398, "gon", "EPSG", 9106, "g", "", "=pi/200 radians.");
    }

    /**
     * Gets or sets the number of radians per
     * {@link #AngularUnit}
     * .
     */
    public double getRadiansPerUnit() throws Exception {
        return _RadiansPerUnit;
    }

    public void setRadiansPerUnit(double value) throws Exception {
        _RadiansPerUnit = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    public String getWKT() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("UNIT[\"%s\", %f", getName(), getRadiansPerUnit()));
        if (getAuthority() != null && getAuthority() != "" && getAuthorityCode() > 0)
            sb.append(String.format(", AUTHORITY[\"%s\", \"%d\"]", getAuthority(), getAuthorityCode()));

        sb.append("]");
        return sb.toString();
    }

    /**
     * Gets an XML representation of this object.
     */
    public String getXML() throws Exception {
        return String.format("<CS_AngularUnit RadiansPerUnit=\"%f\">%s</CS_AngularUnit>", getRadiansPerUnit(), getInfoXml());
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
        if (!(obj instanceof AngularUnit))
            return false;

        return Math.abs(((AngularUnit) obj).getRadiansPerUnit() - this.getRadiansPerUnit()) < EqualityTolerance;
    }

}


