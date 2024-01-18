package com.asseco.android.proj.projections;


import com.asseco.android.proj.ProjectionParameter;
import com.asseco.android.proj.transformations.IMathTransform;

import java.util.List;

/**
 * Summary description for MathTransform.
 * Universal (UTM) and Modified (MTM) Transverses Mercator projections. This
 * is a cylindrical projection, in which the cylinder has been rotated 90°.
 * Instead of being tangent to the equator (or to an other standard latitude),
 * it is tangent to a central meridian. Deformation are more important as we
 * are going futher from the central meridian. The Transverse Mercator
 * projection is appropriate for region wich have a greater extent north-south
 * than east-west.Reference: John P. Snyder (Map Projections - A Working Manual,
 * U.S. Geological Survey Professional Paper 1395, 1987)
 */
public class TransverseMercator extends MapProjection {
    /**
     * Maximum number of iterations for iterative computations.
     */
    private static final int MAXIMUM_ITERATIONS = 15;
    /**
     * Relative iteration precision used in the {@code mlfn} method.
     * This overrides the value in the {@link MapProjection} class.
     */
    private static final double ITERATION_TOLERANCE = 1E-11;
    /**
     * Maximum difference allowed when comparing real numbers.
     */
    private static final double EPSILON = 1E-6;
    /**
     * Maximum difference allowed when comparing latitudes.
     */
    private static final double EPSILON_LATITUDE = 1E-10;
    /**
     * Contants used for the forward and inverse transform for the eliptical
     * case of the Transverse Mercator.
     */
    private static final double FC1 = 1.00000000000000000000000, FC2 = 0.50000000000000000000000, FC3 = 0.16666666666666666666666, FC4 = 0.08333333333333333333333, FC5 = 0.05000000000000000000000, FC6 = 0.03333333333333333333333, FC7 = 0.02380952380952380952380, FC8 = 0.01785714285714285714285;
    /**
     * A derived quantity of excentricity, computed by <code>e'Â² = (aÂ²-bÂ²)/bÂ² = es/(1-es)</code>
     * where <var>a</var> is the semi-major axis length and <var>b</bar> is the semi-minor axis
     * length.
     */
    private final double _esp;
    /**
     * Meridian distance at the {@code latitudeOfOrigin}.
     * Used for calculations for the ellipsoid.
     */
    private final double _ml0;
    // 1/1
    // 1/2
    // 1/6
    // 1/12
    // 1/20
    // 1/30
    // 1/42
    // 1/56
    /**
     * * Variables common to all subroutines in this code file
     */
    //   -----------------------------------------------------*/
    //private double esp;		/* eccentricity constants       */
    //private double ml0;		/* small value m			    */

    /**
     * Creates an instance of an TransverseMercatorProjection projection object.
     *
     * @param parameters List of parameters to initialize the projection.
     * @throws Exception the exception
     */
    public TransverseMercator(List<ProjectionParameter> parameters) throws Exception {
        this(parameters, null);
    }

    /**
     * Creates an instance of an TransverseMercatorProjection projection object.
     *
     * @param parameters List of parameters to initialize the projection.
     * @param inverse    Flag indicating wether is a forward/projection (false) or an inverse projection (true).ItemsDescriptionssemi_majorSemi major radiussemi_minorSemi minor radiusscale_factorcentral meridianlatitude_originfalse_eastingfalse_northing
     * @throws Exception the exception
     */
    protected TransverseMercator(List<ProjectionParameter> parameters, TransverseMercator inverse) throws Exception {
        super(parameters, inverse);
        setName("Transverse_Mercator");
        setAuthority("EPSG");
        setAuthorityCode(9807);
        _esp = _es / (1.0 - _es);
        _ml0 = mlfn(lat_origin, Math.sin(lat_origin), Math.cos(lat_origin));
    }

    /*
                e = Math.sqrt(_es);
    		    ml0 = _semiMajor*mlfn(lat_origin, Math.sin(lat_origin), Math.cos(lat_origin));
    			esp = _es / (1.0 - _es);
                 */

    /**
     * Converts coordinates in decimal degrees to projected meters.
     *
     * @param lonlat The point in decimal degrees.
     * @return Point in projected meters
     */
    protected double[] radiansToMeters(double[] lonlat) throws Exception {
        double x = lonlat[0];
        x = adjust_lon(x - central_meridian);
        double y = lonlat[1];
        double sinphi = Math.sin(y);
        double cosphi = Math.cos(y);
        double t = (Math.abs(cosphi) > EPSILON) ? sinphi / cosphi : 0;
        t *= t;
        double al = cosphi * x;
        double als = al * al;
        al /= Math.sqrt(1.0 - _es * sinphi * sinphi);
        double n = _esp * cosphi * cosphi;
        /* NOTE: meridinal distance at latitudeOfOrigin is always 0 */
        y = (mlfn(y, sinphi, cosphi) - _ml0 + sinphi * al * x * FC2 * (1.0 + FC4 * als * (5.0 - t + n * (9.0 + 4.0 * n) + FC6 * als * (61.0 + t * (t - 58.0) + n * (270.0 - 330.0 * t) + FC8 * als * (1385.0 + t * (t * (543.0 - t) - 3111.0))))));
        x = al * (FC1 + FC3 * als * (1.0 - t + n + FC5 * als * (5.0 + t * (t - 18.0) + n * (14.0 - 58.0 * t) + FC7 * als * (61.0 + t * (t * (179.0 - t) - 479.0)))));
        x = scale_factor * _semiMajor * x;
        y = scale_factor * _semiMajor * y;

        return lonlat.length == 2
                ? new double[]{x, y}
                : new double[]{x, y, lonlat[2]};
    }

    //double lon = Degrees2Radians(lonlat[0]);
    //double lat = Degrees2Radians(lonlat[1]);
    //double delta_lon=0.0;	/* Delta longitude (Given longitude - center 	*/
    //double sin_phi, cos_phi;/* sin and cos value				*/
    //double al, als;		/* temporary values				*/
    //double c, t, tq;	/* temporary values				*/
    //double con, n, ml;	/* cone constant, small m			*/
    //delta_lon = adjust_lon(lon - central_meridian);
    //sincos(lat, out sin_phi, out cos_phi);
    //al  = cos_phi * delta_lon;
    //als = Math.pow(al,2);
    //c = esp * Math.pow(cos_phi,2);
    //tq  = Math.tan(lat);
    //t = Math.pow(tq,2);
    //con = 1.0 - _es * Math.pow(sin_phi,2);
    //n = this._semiMajor / Math.sqrt(con);
    /**
     * /var mlold = this._semiMajor * mlfn(e0, e1, e2, e3, lat);
     */
    //ml = this._semiMajor * mlfn(lat, sin_phi, cos_phi);
    /**
     * /var d = ml - mlold;
     */
    //double x =
    //    scale_factor * n * al * (1.0 + als / 6.0 * (1.0 - t + c + als / 20.0 *
    //    (5.0 - 18.0 * t + Math.pow(t, 2) + 72.0 * c - 58.0 * esp))) + false_easting;
    //double y = scale_factor * (ml - ml0 + n * tq * (als * (0.5 + als / 24.0 *
    //    (5.0 - t + 9.0 * c + 4.0 * Math.pow(c,2) + als / 30.0 * (61.0 - 58.0 * t
    //    + Math.pow(t,2) + 600.0 * c - 330.0 * esp))))) + false_northing;
    //if(lonlat.Length<3)
    //    return new double[] { x / _metersPerUnit, y / _metersPerUnit };
    //else
    //    return new double[] { x / _metersPerUnit, y / _metersPerUnit, lonlat[2] };

    /**
     * Converts coordinates in projected meters to decimal degrees.
     *
     * @param p Point in meters
     * @return Transformed point in decimal degrees
     */
    protected double[] metersToRadians(double[] p) throws Exception {
        /*scale_factor* */
        double x = p[0] / (_semiMajor);
        /*scale_factor* */
        double y = p[1] / (_semiMajor);
        double phi = inv_mlfn(_ml0 + y / scale_factor);
        if (Math.abs(phi) >= PI / 2) {
            y = y < 0.0 ? -(PI / 2) : (PI / 2);
            x = 0.0;
        } else {
            double sinphi = Math.sin(phi);
            double cosphi = Math.cos(phi);
            double t = (Math.abs(cosphi) > EPSILON) ? sinphi / cosphi : 0.0;
            double n = _esp * cosphi * cosphi;
            double con = 1.0 - _es * sinphi * sinphi;
            double d = x * Math.sqrt(con) / scale_factor;
            con *= t;
            t *= t;
            double ds = d * d;
            y = phi - (con * ds / (1.0 - _es)) * FC2 * (1.0 - ds * FC4 * (5.0 + t * (3.0 - 9.0 * n) + n * (1.0 - 4 * n) - ds * FC6 * (61.0 + t * (90.0 - 252.0 * n + 45.0 * t) + 46.0 * n - ds * FC8 * (1385.0 + t * (3633.0 + t * (4095.0 + 1574.0 * t))))));
            x = adjust_lon(central_meridian + d * (FC1 - ds * FC3 * (1.0 + 2.0 * t + n - ds * FC5 * (5.0 + t * (28.0 + 24 * t + 8.0 * n) + 6.0 * n - ds * FC7 * (61.0 + t * (662.0 + t * (1320.0 + 720.0 * t)))))) / cosphi);
        }

        return p.length == 2
                ? new double[]{x, y}
                : new double[]{x, y, p[2]};
    }

    //double con,phi;		/* temporary angles				*/
    //double delta_phi;	/* difference between longitudes		*/
    //long i;			/* counter variable				*/
    //double sin_phi, cos_phi, tan_phi;	/* sin cos and tangent values	*/
    //double c, cs, t, ts, n, r, d, ds;	/* temporary variables		*/
    //long max_iter = 6;			/* maximun number of iterations	*/
    //double x = p[0] * _metersPerUnit - false_easting;
    //double y = p[1] * _metersPerUnit - false_northing;
    //con = (ml0 + y / scale_factor) / this._semiMajor;
    //phi = con;
    //for (i=0;;i++)
    //{
    //    delta_phi = ((con + en1 * Math.sin(2.0*phi) - en2 * Math.sin(4.0*phi) + en3 * Math.sin(6.0*phi) /*+ en4 * Math.sin(8.0*phi)*/)
    //        / en0) - phi;
    //    phi += delta_phi;
    //    if (Math.abs(delta_phi) <= EPSLN) break;
    //    if (i >= max_iter)
    //        throw new IllegalArgumentException("Latitude failed to converge");
    //}
    //if (Math.abs(phi) < HALF_PI)
    //{
    //    sincos(phi, out sin_phi, out cos_phi);
    //    tan_phi = Math.tan(phi);
    //    c = esp * Math.pow(cos_phi, 2);
    //    cs = Math.pow(c, 2);
    //    t = Math.pow(tan_phi, 2);
    //    ts = Math.pow(t, 2);
    //    con = 1.0 - _es * Math.pow(sin_phi, 2);
    //    n = this._semiMajor / Math.sqrt(con);
    //    r = n * (1.0 - _es) / con;
    //    d = x / (n * scale_factor);
    //    ds = Math.pow(d, 2);
    //    double lat = phi - (n * tan_phi * ds / r) * (0.5 - ds / 24.0 * (5.0 + 3.0 * t +
    //        10.0 * c - 4.0 * cs - 9.0 * esp - ds / 30.0 * (61.0 + 90.0 * t +
    //        298.0 * c + 45.0 * ts - 252.0 * esp - 3.0 * cs)));
    //    double lon = adjust_lon(central_meridian + (d * (1.0 - ds / 6.0 * (1.0 + 2.0 * t +
    //        c - ds / 20.0 * (5.0 - 2.0 * c + 28.0 * t - 3.0 * cs + 8.0 * esp +
    //        24.0 * ts))) / cos_phi));
    //    if (p.Length < 3)
    //        return new double[] { Radians2Degrees(lon), Radians2Degrees(lat) };
    //    else
    //        return new double[] { Radians2Degrees(lon), Radians2Degrees(lat), p[2] };
    //}
    //else
    //{
    //    if (p.Length < 3)
    //        return new double[] { Radians2Degrees(HALF_PI * sign(y)), Radians2Degrees(central_meridian) };
    //    else
    //        return new double[] { Radians2Degrees(HALF_PI * sign(y)), Radians2Degrees(central_meridian), p[2] };
    //}

    /**
     * Returns the inverse of this projection.
     *
     * @return IMathTransform that is the reverse of the current projection.
     */
    public IMathTransform inverse() throws Exception {
        if (_inverse == null)
            _inverse = new TransverseMercator(_Parameters.toProjectionParameter(), this);

        return _inverse;
    }

}


