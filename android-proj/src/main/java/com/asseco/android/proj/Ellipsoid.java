package com.asseco.android.proj;

/**
 * The IEllipsoid interface defines the standard information stored with ellipsoid objects.
 */
public class Ellipsoid extends Info implements IEllipsoid {
    /**
     * Gets or sets the value of the semi-major axis.
     */
    private double __SemiMajorAxis;
    /**
     * Gets or sets the value of the semi-minor axis.
     */
    private double __SemiMinorAxis;
    /**
     * Gets or sets the value of the inverse of the flattening constant of the ellipsoid.
     */
    private double __InverseFlattening;
    /**
     * Gets or sets the value of the axis unit.
     */
    private ILinearUnit __AxisUnit;
    /**
     * Tells if the Inverse Flattening is definitive for this ellipsoid. Some ellipsoids use
     * the IVF as the defining value, and calculate the polar radius whenever asked. Other
     * ellipsoids use the polar radius to calculate the IVF whenever asked. This
     * distinction can be important to avoid floating-point rounding errors.
     */
    private boolean __IsIvfDefinitive;

    /**
     * Initializes a new instance of an Ellipsoid
     *
     * @param semiMajorAxis     Semi major axis
     * @param semiMinorAxis     Semi minor axis
     * @param inverseFlattening Inverse flattening
     * @param isIvfDefinitive   Inverse Flattening is definitive for this ellipsoid (Semi-minor axis will be overridden)
     * @param axisUnit          Axis unit
     * @param name              Name
     * @param authority         Authority name
     * @param code              Authority-specific identification code.
     * @param alias             Alias
     * @param abbreviation      Abbreviation
     * @param remarks           Provider-supplied remarks
     * @throws Exception the exception
     */
    public Ellipsoid(double semiMajorAxis, double semiMinorAxis, double inverseFlattening, boolean isIvfDefinitive, ILinearUnit axisUnit, String name, String authority, long code, String alias, String abbreviation, String remarks) throws Exception {
        super(name, authority, code, alias, abbreviation, remarks);
        setSemiMajorAxis(semiMajorAxis);
        setInverseFlattening(inverseFlattening);
        setAxisUnit(axisUnit);
        setIsIvfDefinitive(isIvfDefinitive);
        if (isIvfDefinitive && (inverseFlattening == 0 || Double.isInfinite(inverseFlattening)))
            setSemiMinorAxis(semiMajorAxis);
        else if (isIvfDefinitive)
            setSemiMinorAxis((1.0 - (1.0 / getInverseFlattening())) * semiMajorAxis);
        else
            setSemiMinorAxis(semiMinorAxis);
    }

    /**
     * WGS 84 ellipsoid
     *
     * Inverse flattening derived from four defining parameters
     * (semi-major axis;
     * C20 = -484.16685*10e-6;
     * earth's angular velocity w = 7292115e11 rad/sec;
     * gravitational constant GM = 3986005e8 m*m*m/s/s).
     *
     * @return the wgs 84
     * @throws Exception the exception
     */
    public static Ellipsoid getWGS84() throws Exception {
        return new Ellipsoid(6378137, 0, 298.257223563, true, LinearUnit.getMetre(), "WGS 84", "EPSG", 7030, "WGS84", "", "Inverse flattening derived from four defining parameters (semi-major axis; C20 = -484.16685*10e-6; earth's angular velocity w = 7292115e11 rad/sec; gravitational constant GM = 3986005e8 m*m*m/s/s).");
    }

    /**
     * WGS 72 Ellipsoid
     *
     * @return the wgs 72
     * @throws Exception the exception
     */
    public static Ellipsoid getWGS72() throws Exception {
        return new Ellipsoid(6378135.0, 0, 298.26, true, LinearUnit.getMetre(), "WGS 72", "EPSG", 7043, "WGS 72", "", "");
    }

    /**
     * GRS 1980 / International 1979 ellipsoid
     *
     * Adopted by IUGG 1979 Canberra.
     * Inverse flattening is derived from
     * geocentric gravitational constant GM = 3986005e8 m*m*m/s/s;
     * dynamic form factor J2 = 108263e8 and Earth's angular velocity = 7292115e-11 rad/s.")
     *
     * @return the grs 80
     * @throws Exception the exception
     */
    public static Ellipsoid getGRS80() throws Exception {
        return new Ellipsoid(6378137, 0, 298.257222101, true, LinearUnit.getMetre(), "GRS 1980", "EPSG", 7019, "International 1979", "", "Adopted by IUGG 1979 Canberra.  Inverse flattening is derived from geocentric gravitational constant GM = 3986005e8 m*m*m/s/s; dynamic form factor J2 = 108263e8 and Earth's angular velocity = 7292115e-11 rad/s.");
    }

    /**
     * International 1924 / Hayford 1909 ellipsoid
     *
     * Described as a=6378388 m. and b=6356909m. from which 1/f derived to be 296.95926.
     * The figure was adopted as the International ellipsoid in 1924 but with 1/f taken as
     * 297 exactly from which b is derived as 6356911.946m.
     *
     * @return the international 1924
     * @throws Exception the exception
     */
    public static Ellipsoid getInternational1924() throws Exception {
        return new Ellipsoid(6378388, 0, 297, true, LinearUnit.getMetre(), "International 1924", "EPSG", 7022, "Hayford 1909", "", "Described as a=6378388 m. and b=6356909 m. from which 1/f derived to be 296.95926. The figure was adopted as the International ellipsoid in 1924 but with 1/f taken as 297 exactly from which b is derived as 6356911.946m.");
    }

    /**
     * Clarke 1880
     *
     * Clarke gave a and b and also 1/f=293.465 (to 3 decimal places).  1/f derived from a and b = 293.4663077
     *
     * @return the clarke 1880
     * @throws Exception the exception
     */
    public static Ellipsoid getClarke1880() throws Exception {
        return new Ellipsoid(20926202, 0, 297, true, LinearUnit.getClarkesFoot(), "Clarke 1880", "EPSG", 7034, "Clarke 1880", "", "Clarke gave a and b and also 1/f=293.465 (to 3 decimal places).  1/f derived from a and b = 293.4663077…");
    }

    /**
     * Clarke 1866
     *
     * Original definition a=20926062 and b=20855121 (British) feet. Uses Clarke's 1865 inch-metre ratio of 39.370432 to obtain metres. (Metric value then converted to US survey feet for use in the United States using 39.37 exactly giving a=20925832.16 ft US).
     *
     * @return the clarke 1866
     * @throws Exception the exception
     */
    public static Ellipsoid getClarke1866() throws Exception {
        return new Ellipsoid(6378206.4, 6356583.8, Double.POSITIVE_INFINITY, false, LinearUnit.getMetre(), "Clarke 1866", "EPSG", 7008, "Clarke 1866", "", "Original definition a=20926062 and b=20855121 (British) feet. Uses Clarke's 1865 inch-metre ratio of 39.370432 to obtain metres. (Metric value then converted to US survey feet for use in the United States using 39.37 exactly giving a=20925832.16 ft US).");
    }

    /**
     * Sphere
     *
     * Authalic sphere derived from GRS 1980 ellipsoid (code 7019).  (An authalic sphere is
     * one with a surface area equal to the surface area of the ellipsoid). 1/f is infinite.
     *
     * @return the sphere
     * @throws Exception the exception
     */
    public static Ellipsoid getSphere() throws Exception {
        return new Ellipsoid(6370997.0, 6370997.0, Double.POSITIVE_INFINITY, false, LinearUnit.getMetre(), "GRS 1980 Authalic Sphere", "EPSG", 7048, "Sphere", "", "Authalic sphere derived from GRS 1980 ellipsoid (code 7019).  (An authalic sphere is one with a surface area equal to the surface area of the ellipsoid). 1/f is infinite.");
    }

    public double getSemiMajorAxis() {
        return __SemiMajorAxis;
    }

    public void setSemiMajorAxis(double value) {
        __SemiMajorAxis = value;
    }

    public double getSemiMinorAxis() {
        return __SemiMinorAxis;
    }

    public void setSemiMinorAxis(double value) {
        __SemiMinorAxis = value;
    }

    public double getInverseFlattening() {
        return __InverseFlattening;
    }

    public void setInverseFlattening(double value) {
        __InverseFlattening = value;
    }

    public ILinearUnit getAxisUnit() {
        return __AxisUnit;
    }

    public void setAxisUnit(ILinearUnit value) {
        __AxisUnit = value;
    }

    public boolean getIsIvfDefinitive() {
        return __IsIvfDefinitive;
    }

    public void setIsIvfDefinitive(boolean value) {
        __IsIvfDefinitive = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    public String getWKT() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("SPHEROID[\"%s\", %f, %f", getName(), getSemiMajorAxis(), getInverseFlattening()));
        if (getAuthority() != null && getAuthority() != "" && getAuthorityCode() > 0)
            sb.append(String.format(", AUTHORITY[\"%s\", \"%d\"]", getAuthority(), getAuthorityCode()));

        sb.append("]");
        return sb.toString();
    }

    /**
     * Gets an XML representation of this object
     */
    public String getXML() throws Exception {
        return String.format("<CS_Ellipsoid SemiMajorAxis=\"%f\" SemiMinorAxis=\"%f\" InverseFlattening=\"%f\" IvfDefinitive=\"%d\">%s%s</CS_Ellipsoid>",
                getSemiMajorAxis(), getSemiMinorAxis(), getInverseFlattening(), (getIsIvfDefinitive() ? 1 : 0), getInfoXml(), getAxisUnit().getXML());

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
        if (!(obj instanceof Ellipsoid))
            return false;

        Ellipsoid e = (Ellipsoid)obj;
        return (e.getInverseFlattening() == this.getInverseFlattening() && e.getIsIvfDefinitive() == this.getIsIvfDefinitive() && e.getSemiMajorAxis() == this.getSemiMajorAxis() && e.getSemiMinorAxis() == this.getSemiMinorAxis() && e.getAxisUnit().equalParams(this.getAxisUnit()));
    }

}


