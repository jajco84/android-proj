package com.asseco.android.proj.projections;

/*
 * http://svn.osgeo.org/geotools/tags/2.6.2/modules/library/referencing/src/main/java/org/geotools/referencing/operation/projection/Polyconic.java
 * http://svn.osgeo.org/geotools/tags/2.6.2/modules/library/referencing/src/main/java/org/geotools/referencing/operation/projection/MapProjection.java
 */

import com.asseco.android.proj.ProjectionParameter;
import com.asseco.android.proj.transformations.IMathTransform;

import java.util.List;

/**
 * The type Polyconic projection.
 */
public class PolyconicProjection extends MapProjection {
    /**
     * Maximum difference allowed when comparing real numbers.
     */
    private static final double Epsilon = 1E-10;
    /**
     * Maximum number of iterations for iterative computations.
     */
    private static final int MaximumIterations = 20;
    /**
     * Difference allowed in iterative computations.
     */
    private static final double IterationTolerance = 1E-12;
    /**
     * Meridian distance at the latitude of origin.
     * Used for calculations for the ellipsoid.
     */
    private final double _ml0;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param parameters The parameter values in standard units
     * @throws Exception the exception
     */
    public PolyconicProjection(List<ProjectionParameter> parameters) throws Exception {
        this(parameters, null);
    }

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param parameters The parameter values in standard units
     * @param inverse    Defines if Projection is inverse
     * @throws Exception the exception
     */
    protected PolyconicProjection(List<ProjectionParameter> parameters, PolyconicProjection inverse) throws Exception {
        super(parameters, inverse);
        _ml0 = mlfn(lat_origin, Math.sin(lat_origin), Math.cos(lat_origin));
    }

    protected double[] radiansToMeters(double[] lonlat) throws Exception {
        double lam = lonlat[0];
        double phi = lonlat[1];
        double delta_lam = adjust_lon(lam - central_meridian);
        double x, y;
        if (Math.abs(phi) <= Epsilon) {
            x = delta_lam;
            //lam;
            y = -_ml0;
        } else {
            double sp = Math.sin(phi);
            double cp;
            double ms = Math.abs(cp = Math.cos(phi)) > Epsilon ? msfn(sp, cp) / sp : 0.0;
            /*lam =*/
            delta_lam *= sp;
            /*lam*/
            x = ms * Math.sin(delta_lam);
            /*lam*/
            y = (mlfn(phi, sp, cp) - _ml0) + ms * (1.0 - Math.cos(delta_lam));
        }
        x = scale_factor * _semiMajor * x;
        // + false_easting;
        y = scale_factor * _semiMajor * y;
        return new double[]{x, y};
    }

    // +false_northing;
    protected double[] metersToRadians(double[] p) throws Exception {
        double x = (p[0]) / (_semiMajor * scale_factor);
        double y = (p[1]) / (_semiMajor * scale_factor);
        double lam, phi;
        y += _ml0;
        if (Math.abs(y) <= Epsilon) {
            lam = x;
            phi = 0.0;
        } else {
            double r = y * y + x * x;
            phi = y;
            for (int i = 0; i <= MaximumIterations; i++) {
                double sp = Math.sin(phi);
                double cp = Math.cos(phi);
                if (Math.abs(cp) < IterationTolerance)
                    throw new Exception("No Convergence");

                double s2ph = sp * cp;
                double mlp = Math.sqrt(1.0 - _es * sp * sp);
                double c = sp * mlp / cp;
                double ml = mlfn(phi, sp, cp);
                double mlb = ml * ml + r;
                mlp = (1.0 - _es) / (mlp * mlp * mlp);
                double dPhi = (ml + ml + c * mlb - 2.0 * y * (c * ml + 1.0)) / (_es * s2ph * (mlb - 2.0 * y * ml) / c + 2.0 * (y - ml) * (c * mlp - 1.0 / s2ph) - mlp - mlp);
                if (Math.abs(dPhi) <= IterationTolerance)
                    break;

                phi += dPhi;
                if (i == MaximumIterations)
                    throw new Exception("No Convergence");
            }


            double c2 = Math.sin(phi);
            lam = Math.asin(x * Math.tan(phi) * Math.sqrt(1.0 - _es * c2 * c2)) / Math.sin(phi);
        }
        return new double[]{adjust_lon(lam + central_meridian), phi};
    }

    /**
     * Returns the inverse of this projection.
     *
     * @return IMathTransform that is the reverse of the current projection.
     */
    public IMathTransform inverse() throws Exception {
        if (_inverse == null)
            _inverse = new PolyconicProjection(_Parameters.toProjectionParameter(), this);

        return _inverse;
    }

    /**
     * Computes function
     * {@code f(s,c,e²) = c/sqrt(1 - s²*e²)}
     * needed for the true scale
     * latitude (Snyder 14-15), where s and c are the sine and cosine of
     * the true scale latitude, and e² is the eccentricity squared.
     *
     * @param s the s
     * @param c the c
     * @return the double
     * @throws Exception the exception
     */
    double msfn(double s, double c) throws Exception {
        return c / Math.sqrt(1.0 - (s * s) * _es);
    }

}


