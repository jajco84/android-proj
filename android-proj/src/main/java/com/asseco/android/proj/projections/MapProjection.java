package com.asseco.android.proj.projections;



import com.asseco.android.proj.IProjection;
import com.asseco.android.proj.ProjectionParameter;
import com.asseco.android.proj.transformations.MathTransform;

import java.util.ArrayList;
import java.util.List;


/**
 * Projections inherit from this abstract class to get access to useful mathematical functions.
 */
public abstract class MapProjection extends MathTransform implements IProjection {
    /**
     * PI
     */
    protected static final double PI = Math.PI;
    /**
     * Half of PI
     */
    protected static final double HALF_PI = (PI * 0.5);
    /**
     * PI * 2
     */
    protected static final double TWO_PI = (PI * 2.0);
    /**
     * EPSLN
     */
    protected static final double EPSLN = 1.0e-10;
    /**
     * S2R
     */
    protected static final double S2R = 4.848136811095359e-6;
    /**
     * MAX_VAL
     */
    protected static final double MAX_VAL = 4;
    /**
     * prjMAXLONG
     */
    protected static final double prjMAXLONG = 2147483647;
    /**
     * DBLLONG
     */
    protected static final double DBLLONG = 4.61168601e18;
    private static final double C00 = 1.0, C02 = 0.25, C04 = 0.046875, C06 = 0.01953125, C08 = 0.01068115234375, C22 = 0.75, C44 = 0.46875, C46 = 0.01302083333333333333, C48 = 0.00712076822916666666, C66 = 0.36458333333333333333, C68 = 0.00569661458333333333, C88 = 0.3076171875;
    private final double MLFN_TOL = 1E-11;
    private final int MAXIMUM_ITERATIONS = 20;
    /**
     * The E.
     */
// ReSharper disable InconsistentNaming
    protected double _e;
    /**
     * The Es.
     */
    protected double _es;
    /**
     * The Semi major.
     */
    protected double _semiMajor;
    /**
     * The Semi minor.
     */
    protected double _semiMinor;
    /**
     * The Meters per unit.
     */
    protected double _metersPerUnit;
    /**
     * The Scale factor.
     */
    protected double scale_factor;
    /**
     * The Central meridian.
     */
/* scale factor				*/
    protected double central_meridian;
    /**
     * The Lat origin.
     */
// ReSharper restore InconsistentNaming
    protected double lat_origin;
    /**
     * The False northing.
     */
/* center latitude			*/
    protected double false_northing;
    /**
     * The False easting.
     */
/* y offset in meters			*/
    protected double false_easting;
    /**
     * The En 0.
     */
/* x offset in meters			*/
    protected double en0, /**
     * The En 1.
     */
    en1, /**
     * The En 2.
     */
    en2, /**
     * The En 3.
     */
    en3, /**
     * The En 4.
     */
    en4;
    /**
     * The Parameters.
     */
    protected ProjectionParameterSet _Parameters;
    /**
     * The Inverse.
     */
    protected MathTransform _inverse;
    private boolean _isinverse;
    /**
     * Gets or sets the abbreviation of the object.
     */
    private String __Abbreviation = new String();
    /**
     * Gets or sets the alias of the object.
     */
    private String __Alias = new String();
    /**
     * Gets or sets the authority name for this object, e.g., "EPSG",
     * is this is a standard object with an authority specific
     * identity code. Returns "CUSTOM" if this is a custom object.
     */
    private String __Authority = new String();
    /**
     * Gets or sets the authority specific identification code of the object
     */
    private long __AuthorityCode;
    /**
     * Gets or sets the name of the object.
     */
    private String __Name = new String();
    /**
     * Gets or sets the provider-supplied remarks for the object.
     */
    private String __Remarks = new String();

    /**
     * Creates an instance of this class
     *
     * @param parameters An enumeration of projection parameters
     * @param inverse    Indicator if this projection is inverse
     * @throws Exception the exception
     */
    protected MapProjection(List<ProjectionParameter> parameters, MapProjection inverse) throws Exception {
        this(parameters);
        _inverse = inverse;
        if (_inverse != null) {
            inverse._inverse = this;
            _isinverse = !inverse._isinverse;
        }

    }

    /**
     * Creates an instance of this class
     *
     * @param parameters An enumeration of projection parameters
     * @throws Exception the exception
     */
    protected MapProjection(List<ProjectionParameter> parameters) throws Exception {
        _Parameters = new ProjectionParameterSet(parameters);
        _semiMajor = _Parameters.getParameterValue("semi_major");
        _semiMinor = _Parameters.getParameterValue("semi_minor");
        //_es = 1.0 - (_semiMinor * _semiMinor) / (_semiMajor * _semiMajor);
        _es = eccentricySquared(_semiMajor, _semiMinor);
        _e = Math.sqrt(_es);
        scale_factor = _Parameters.getOptionalParameterValue("scale_factor", 1);
        central_meridian = degrees2Radians(_Parameters.getParameterValue("central_meridian", "longitude_of_center"));
        lat_origin = degrees2Radians(_Parameters.getParameterValue("latitude_of_origin", "latitude_of_center"));
        _metersPerUnit = _Parameters.getParameterValue("unit");
        false_easting = _Parameters.getOptionalParameterValue("false_easting", 0) * _metersPerUnit;
        false_northing = _Parameters.getOptionalParameterValue("false_northing", 0) * _metersPerUnit;
        // TODO: Should really convert to the correct linear units??
        //  Compute constants for the mlfn
        double t;
        en0 = C00 - _es * (C02 + _es * (C04 + _es * (C06 + _es * C08)));
        en1 = _es * (C22 - _es * (C04 + _es * (C06 + _es * C08)));
        en2 = (t = _es * _es) * (C44 - _es * (C46 + _es * C48));
        en3 = (t *= _es) * (C66 - _es * C68);
        en4 = t * _es * C88;
    }

    /**
     * Returns a list of projection "cloned" projection parameters
     *
     * @param projectionParameters the projection parameters
     * @return list
     * @throws Exception the exception
     */
    protected static List<ProjectionParameter> cloneParametersList(List<ProjectionParameter> projectionParameters) throws Exception {
        List<ProjectionParameter> res = new ArrayList<ProjectionParameter>();
        for (ProjectionParameter pp : projectionParameters)
            res.add(new ProjectionParameter(pp.getName(), pp.getValue()));
        return res;
    }

    /**
     * Returns the cube of a number.
     *
     * @param x the x
     * @return the double
     * @throws Exception the exception
     */
    protected static double cUBE(double x) throws Exception {
        return Math.pow(x, 3);
    }

    /**
     * Returns the quad of a number.
     *
     * @param x the x
     * @return the double
     * @throws Exception the exception
     */
    protected static double qUAD(double x) throws Exception {
        return Math.pow(x, 4);
    }

    /**
     * IMOD
     *
     * @param A the a
     * @param B the b
     * @return double
     * @throws Exception the exception
     */
    protected static double iMOD(double A, double B) throws Exception {
        return (A) - (((A) / (B)) * (B));
    }

    /**
     * Function to return the sign of an argument
     *
     * @param x the x
     * @return the double
     * @throws Exception the exception
     */
    protected static double sign(double x) throws Exception {
        if (x < 0.0)
            return (-1);
        else
            return (1);
    }

    /**
     * Adjust lon double.
     *
     * @param x the x
     * @return double
     * @throws Exception the exception
     */
    protected static double adjust_lon(double x) throws Exception {
        long count = 0;
        for (; ; ) {
            if (Math.abs(x) <= PI)
                break;
            else if (((long) Math.abs(x / Math.PI)) < 2)
                x = x - (sign(x) * TWO_PI);
            else if (((long) Math.abs(x / TWO_PI)) < prjMAXLONG) {
                x = x - (((long) (x / TWO_PI)) * TWO_PI);
            } else if (((long) Math.abs(x / (prjMAXLONG * TWO_PI))) < prjMAXLONG) {
                x = x - (((long) (x / (prjMAXLONG * TWO_PI))) * (TWO_PI * prjMAXLONG));
            } else if (((long) Math.abs(x / (DBLLONG * TWO_PI))) < prjMAXLONG) {
                x = x - (((long) (x / (DBLLONG * TWO_PI))) * (TWO_PI * DBLLONG));
            } else
                x = x - (sign(x) * TWO_PI);
            count++;
            if (count > MAX_VAL)
                break;

        }
        return (x);
    }

    /**
     * Function to compute the constant small m which is the radius of
     * a parallel of latitude, phi, divided by the semimajor axis.
     *
     * @param eccent the eccent
     * @param sinphi the sinphi
     * @param cosphi the cosphi
     * @return the double
     * @throws Exception the exception
     */
    protected static double msfnz(double eccent, double sinphi, double cosphi) throws Exception {
        double con;
        con = eccent * sinphi;
        return ((cosphi / (Math.sqrt(1.0 - con * con))));
    }

    /**
     * Function to compute constant small q which is the radius of a
     * parallel of latitude, phi, divided by the semimajor axis.
     *
     * @param eccent the eccent
     * @param sinphi the sinphi
     * @return the double
     * @throws Exception the exception
     */
    protected static double qsfnz(double eccent, double sinphi) throws Exception {
        double con;
        if (eccent > 1.0e-7) {
            con = eccent * sinphi;
            return ((1.0 - eccent * eccent) * (sinphi / (1.0 - con * con) - (.5 / eccent) * Math.log((1.0 - con) / (1.0 + con))));
        } else
            return 2.0 * sinphi;
    }

    /**
     * Function to compute the constant small t for use in the forward
     * computations in the Lambert Conformal Conic and the Polar
     * Stereographic projections.
     *
     * @param eccent the eccent
     * @param phi    the phi
     * @param sinphi the sinphi
     * @return the double
     * @throws Exception the exception
     */
    protected static double tsfnz(double eccent, double phi, double sinphi) throws Exception {
        double con;
        double com;
        con = eccent * sinphi;
        com = .5 * eccent;
        con = Math.pow(((1.0 - con) / (1.0 + con)), com);
        return (Math.tan(.5 * (HALF_PI - phi)) / con);
    }

    /**
     * Phi 1 z double.
     *
     * @param eccent the eccent
     * @param qs     the qs
     * @return double
     * @throws Exception the exception
     */
    protected static double phi1z(double eccent, double qs) throws Exception {
        double eccnts;
        double dphi;
        double con;
        double com;
        double sinpi;
        double cospi;
        double phi;
        //double asinz();
        long i;
        phi = asinz(.5 * qs);
        if (eccent < EPSLN)
            return (phi);

        eccnts = eccent * eccent;
        for (i = 1; i <= 25; i++) {
            sinpi = Math.sin(phi);
            cospi = Math.cos(phi);
            con = eccent * sinpi;
            com = 1.0 - con * con;
            dphi = .5 * com * com / cospi * (qs / (1.0 - eccnts) - sinpi / com + .5 / eccent * Math.log((1.0 - con) / (1.0 + con)));
            phi = phi + dphi;
            if (Math.abs(dphi) <= 1e-7)
                return (phi);

        }
        throw new IllegalArgumentException("Convergence error.");
    }

    /**
     * Function to eliminate roundoff errors in asin
     *
     * @param con the con
     * @return the double
     * @throws Exception the exception
     */
    protected static double asinz(double con) throws Exception {
        if (Math.abs(con) > 1.0) {
            if (con > 1.0)
                con = 1.0;
            else
                con = -1.0;
        }

        return (Math.asin(con));
    }

    /**
     * Function to compute the latitude angle, phi2, for the inverse of the
     * Lambert Conformal Conic and Polar Stereographic projections.
     *
     * @param eccent Spheroid eccentricity
     * @param ts     Constant value t
     * @return the double
     * @throws Exception the exception
     */
    protected static double phi2z(double eccent, double ts) throws Exception {
        double con;
        double dphi;
        double sinpi;
        long i;

        double eccnth = .5 * eccent;
        double chi = HALF_PI - 2 * Math.atan(ts);
        for (i = 0; i <= 15; i++) {
            sinpi = Math.sin(chi);
            con = eccent * sinpi;
            dphi = HALF_PI - 2 * Math.atan(ts * (Math.pow(((1.0 - con) / (1.0 + con)), eccnth))) - chi;
            chi += dphi;
            if (Math.abs(dphi) <= .0000000001)
                return (chi);

        }
        throw new IllegalArgumentException("Convergence error - phi2z-conv");
    }

    /**
     * Functions to compute the constants e0, e1, e2, and e3 which are used
     * in a series for calculating the distance along a meridian.  The
     * input x represents the eccentricity squared.
     *
     * @param x the x
     * @return the double
     * @throws Exception the exception
     */
    protected static double e0fn(double x) throws Exception {
        return (1.0 - 0.25 * x * (1.0 + x / 16.0 * (3.0 + 1.25 * x)));
    }

    /**
     * E 1 fn double.
     *
     * @param x the x
     * @return double
     * @throws Exception the exception
     */
    protected static double e1fn(double x) throws Exception {
        return (0.375 * x * (1.0 + 0.25 * x * (1.0 + 0.46875 * x)));
    }

    /**
     * E 2 fn double.
     *
     * @param x the x
     * @return double
     * @throws Exception the exception
     */
    protected static double e2fn(double x) throws Exception {
        return (0.05859375 * x * x * (1.0 + 0.75 * x));
    }

    /**
     * E 3 fn double.
     *
     * @param x the x
     * @return double
     * @throws Exception the exception
     */
    protected static double e3fn(double x) throws Exception {
        return (x * x * x * (35.0 / 3072.0));
    }

    /**
     * Function to compute the constant e4 from the input of the eccentricity
     * of the spheroid, x.  This constant is used in the Polar Stereographic
     * projection.
     *
     * @param x the x
     * @return the double
     * @throws Exception the exception
     */
    protected static double e4fn(double x) throws Exception {
        double con;
        double com;
        con = 1.0 + x;
        com = 1.0 - x;
        return (Math.sqrt((Math.pow(con, con)) * (Math.pow(com, com))));
    }

    /**
     * Function computes the value of M which is the distance along a meridian
     * from the Equator to latitude phi.
     *
     * @param e0  the e 0
     * @param e1  the e 1
     * @param e2  the e 2
     * @param e3  the e 3
     * @param phi the phi
     * @return the double
     * @throws Exception the exception
     */
    protected static double mlfn(double e0, double e1, double e2, double e3, double phi) throws Exception {
        return (e0 * phi - e1 * Math.sin(2.0 * phi) + e2 * Math.sin(4.0 * phi) - e3 * Math.sin(6.0 * phi));
    }

    /**
     * Calculates the flattening factor, (
     * {@code equatorialRadius}
     * -
     * {@code polarRadius}
     * ) /
     * {@code equatorialRadius}
     * .
     *
     * @param equatorialRadius The radius of the equator
     * @param polarRadius      The radius of a circle touching the poles
     * @return The flattening factor
     */
    private static double flatteningFactor(double equatorialRadius, double polarRadius) throws Exception {
        return (equatorialRadius - polarRadius) / equatorialRadius;
    }

    /**
     * Calculates the square of eccentricity according to es = (2f - f^2) where f is the FlatteningFactor
     * .
     *
     * @param equatorialRadius The radius of the equator
     * @param polarRadius      The radius of a circle touching the poles
     * @return The square of eccentricity
     */
    private static double eccentricySquared(double equatorialRadius, double polarRadius) throws Exception {
        double f = flatteningFactor(equatorialRadius, polarRadius);
        return 2 * f - f * f;
    }

    /**
     * Function to calculate UTM zone number
     *
     * @param lon The longitudinal value (in Degrees!)
     * @return The UTM zone number
     * @throws Exception the exception
     */
    protected static long calcUtmZone(double lon) throws Exception {
        return ((long) (((lon + 180.0) / 6.0) + 1.0));
    }

    /**
     * Converts a longitude value in degrees to radians.
     *
     * @param x    The value in degrees to convert to radians.
     * @param edge If true, -180 and +180 are valid, otherwise they are considered out of range.
     * @return double
     * @throws Exception the exception
     */
    protected static double longitudeToRadians(double x, boolean edge) throws Exception {
        if (edge ? (x >= -180 && x <= 180) : (x > -180 && x < 180))
            return degrees2Radians(x);

        throw new IllegalArgumentException("x " + x + " not a valid longitude in degrees.");
    }

    //var projectedPoint = new double[] { 0, 0, 0, };

    /**
     * Converts a latitude value in degrees to radians.
     *
     * @param y    The value in degrees to to radians.
     * @param edge If true, -90 and +90 are valid, otherwise they are considered out of range.
     * @return double
     * @throws Exception the exception
     */
    protected static double latitudeToRadians(double y, boolean edge) throws Exception {
        if (edge ? (y >= -90 && y <= 90) : (y > -90 && y < 90))
            return degrees2Radians(y);

        throw new IllegalArgumentException("y " + y + " not a valid latitude in degrees.");
    }

    /*
                if (proj.NumParameters != NumParameters)
    				return false;
    			
                for (var i = 0; i < _Parameters.Count; i++)
    			{
    				var param = _Parameters.Find(par => par.Name.Equals(proj.GetParameter(i).Name, StringComparison.OrdinalIgnoreCase));
    				if (param == null)
    					return false;
    				if (param.Value != proj.GetParameter(i).Value)
    					return false;
    			}
                 */
    // defines some usefull constants that are used in the projection routines

    /**
     * Gets origin.
     *
     * @return the origin
     * @throws Exception the exception
     */
/* Center longitude (projection center) */
    protected double getlon_origin() throws Exception {
        return central_meridian;
    }

    /**
     * Sets origin.
     *
     * @param value the value
     * @throws Exception the exception
     */
    protected void setlon_origin(double value) throws Exception {
        central_meridian = value;
    }

    /**
     * Gets the projection classification name (e.g. 'Transverse_Mercator').
     */
    public String getClassName() throws Exception {
        return getName();
    }

    /**
     * @param index
     * @return
     */
    public ProjectionParameter getParameter(int index) throws Exception {
        return _Parameters.getAtIndex(index);
    }

    /**
     * Gets an named parameter of the projection.
     * The parameter name is case insensitive
     *
     * @param name Name of parameter
     * @return parameter or null if not found
     */
    public ProjectionParameter getParameter(String name) throws Exception {
        return _Parameters.find(name);
    }

    /**
     *
     */
    public int getNumParameters() throws Exception {
        return _Parameters.size();
    }

    public String getAbbreviation() {
        return __Abbreviation;
    }

    /**
     * Sets abbreviation.
     *
     * @param value the value
     */
    public void setAbbreviation(String value) {
        __Abbreviation = value;
    }

    public String getAlias() {
        return __Alias;
    }

    /* x^3 */

    /**
     * Sets alias.
     *
     * @param value the value
     */
    public void setAlias(String value) {
        __Alias = value;
    }


    /* assign minimum of a and b */

    public String getAuthority() {
        return __Authority;
    }

    /* Integer mod function */

    /**
     * Sets authority.
     *
     * @param value the value
     */
    public void setAuthority(String value) {
        __Authority = value;
    }

    public long getAuthorityCode() {
        return __AuthorityCode;
    }

    /**
     * Sets authority code.
     *
     * @param value the value
     */
    public void setAuthorityCode(long value) {
        __AuthorityCode = value;
    }

    public String getName() {
        return __Name;
    }

    /**
     * Function to calculate the sine and cosine in one call.  Some computer
     * systems have implemented this function, resulting in a faster implementation
     * than calling each function separately.  It is provided here for those
     * computer systems which don`t implement this function
     *
     * @param value the value
     */
/* protected static void sincos(double val, RefSupport<double> sin_val, RefSupport<double> cos_val) throws Exception {
        sin_val.setValue(Math.sin(val));
        cos_val.setValue(Math.cos(val));
    }*/

    public void setName(String value) {
        __Name = value;
    }

    public String getRemarks() {
        return __Remarks;
    }

    //p_error ("Convergence error","phi1z-conv");
    //ASSERT(FALSE);

    /**
     * Sets remarks.
     *
     * @param value the value
     */
    public void setRemarks(String value) {
        __Remarks = value;
    }

    /**
     * Returns the Well-known text for this object
     * as defined in the simple features specification.
     */
    public String getWKT() throws Exception {
        StringBuilder sb = new StringBuilder();
        if (_isinverse)
            sb.append("INVERSE_MT[");

        sb.append(String.format("PARAM_MT[\"%s\"", getName()));
        for (int i = 0; i < getNumParameters(); i++)
            sb.append(String.format(", %s", getParameter(i).getWKT()));
        //if (!String.IsNullOrEmpty(Authority) && AuthorityCode > 0)
        //	sb.AppendFormat(", AUTHORITY[\"{0}\", \"{1}\"]", Authority, AuthorityCode);
        sb.append("]");
        if (_isinverse)
            sb.append("]");

        return sb.toString();
    }

    /**
     * Gets an XML representation of this object
     */
    public String getXML() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<CT_MathTransform>");
        sb.append(String.format(_isinverse ? "<CT_InverseTransform Name=\"%s\">" : "<CT_ParameterizedMathTransform Name=\"%s\">", getClassName()));
        for (int i = 0; i < getNumParameters(); i++)
            sb.append(getParameter(i).getXML());
        sb.append(_isinverse ? "</CT_InverseTransform>" : "</CT_ParameterizedMathTransform>");
        sb.append("</CT_MathTransform>");
        return sb.toString();
    }

    public int getDimSource() throws Exception {
        return 2;
    }

    public int getDimTarget() throws Exception {
        return 2;
    }

    /**
     * Function to transform from meters to degrees
     *
     * @param p The ordinates of the point
     * @return The transformed ordinates
     * @throws Exception the exception
     */
    public double[] metersToDegrees(double[] p) throws Exception {
        double[] tmp = p.length == 2
                ? new double[]{p[0] * _metersPerUnit - false_easting, p[1] * _metersPerUnit - false_northing}
                : new double[]
                {
                        p[0] * _metersPerUnit - false_easting, p[1] * _metersPerUnit - false_northing,
                        p[2] * _metersPerUnit
                };

        double[] res = metersToRadians(tmp);
        res[0] = radians2Degrees(res[0]);
        res[1] = radians2Degrees(res[1]);
        if (getDimTarget() == 3)
            res[2] = radians2Degrees(res[2]);

        return res;
    }

    /**
     * Function to transform from degrees to meters
     *
     * @param lonlat The ordinates of the point
     * @return The transformed ordinates
     * @throws Exception the exception
     */
    public double[] degreesToMeters(double[] lonlat) throws Exception {
        // Convert to radians
        double[] tmp = lonlat.length == 2
                ? new double[]{degrees2Radians(lonlat[0]), degrees2Radians(lonlat[1])}
                : new double[]{degrees2Radians(lonlat[0]), degrees2Radians(lonlat[1]), degrees2Radians(lonlat[2])};
        // Convert to meters
        double[] res = radiansToMeters(tmp);
        // Add false easting and northing, convert to unit
        res[0] = (res[0] + false_easting) / _metersPerUnit;
        res[1] = (res[1] + false_northing) / _metersPerUnit;
        if (res.length == 3)
            res[2] = res[2] / _metersPerUnit;

        return res;
    }

    /**
     * Radians to meters double [ ].
     *
     * @param lonlat the lonlat
     * @return the double [ ]
     * @throws Exception the exception
     */
    protected abstract double[] radiansToMeters(double[] lonlat) throws Exception;

    /**
     * Meters to radians double [ ].
     *
     * @param p the p
     * @return the double [ ]
     * @throws Exception the exception
     */
    protected abstract double[] metersToRadians(double[] p) throws Exception;

    /**
     * Reverses the transformation
     */
    public void invert() throws Exception {
        _isinverse = !_isinverse;
        if (_inverse != null)
            ((MapProjection) _inverse).invert(false);

    }

    /**
     * Invert.
     *
     * @param invertInverse the invert inverse
     * @throws Exception the exception
     */
    protected void invert(boolean invertInverse) throws Exception {
        _isinverse = !_isinverse;
        if (invertInverse && _inverse != null)
            ((MapProjection) _inverse).invert(false);

    }

    /**
     * Returns true if this projection is inverted.
     * Most map projections define forward projection as "from geographic to projection", and backwards
     * as "from projection to geographic". If this projection is inverted, this will be the other way around.
     *
     * @return the isinverse
     * @throws Exception the exception
     */
    protected boolean getIsinverse() throws Exception {
        return _isinverse;
    }

    /**
     * Transforms the specified cp.
     *
     * @param cp The cp.
     * @return
     */
    public double[] transform(double[] cp) throws Exception {
        return !_isinverse ? degreesToMeters(cp) : metersToDegrees(cp);
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
        if (!(obj instanceof MapProjection))
            return false;

        MapProjection proj = obj instanceof MapProjection ? (MapProjection) obj : (MapProjection) null;
        if (proj != null) {
            if (!_Parameters.equals(proj._Parameters))
                return false;

            return getIsinverse() == proj.getIsinverse();
        }
        return false;
    }

    /**
     * Calculates the meridian distance. This is the distance along the central
     * meridian from the equator to
     * {@code phi}
     * . Accurate to less then 1e-5 meters
     * when used in conjuction with typical major axis values.
     *
     * @param phi  the phi
     * @param sphi the sphi
     * @param cphi the cphi
     * @return double
     * @throws Exception the exception
     */
    protected double mlfn(double phi, double sphi, double cphi) throws Exception {
        cphi *= sphi;
        sphi *= sphi;
        return en0 * phi - cphi * (en1 + sphi * (en2 + sphi * (en3 + sphi * en4)));
    }

    /**
     * Calculates the latitude (phi) from a meridian distance.
     * Determines phi to TOL (1e-11) radians, about 1e-6 seconds.
     *
     * @param arg The meridonial distance
     * @return The latitude of the meridian distance.
     * @throws Exception the exception
     */
    protected double inv_mlfn(double arg) throws Exception {
        ;
        ;
        double s, t, phi, k = 1.0 / (1.0 - _es);
        int i;
        phi = arg;
        for (i = MAXIMUM_ITERATIONS; true; ) {
            // rarely goes over 5 iterations
            if (--i < 0) {
                throw new IllegalArgumentException("No convergence");
            }

            s = Math.sin(phi);
            t = 1.0 - _es * s * s;
            t = (mlfn(phi, s, Math.cos(phi)) - arg) * (t * Math.sqrt(t)) * k;
            phi -= t;
            if (Math.abs(t) < MLFN_TOL) {
                return phi;
            }

        }
    }

}


