package com.asseco.android.proj.projections;


import com.asseco.android.proj.ProjectionParameter;
import com.asseco.android.proj.transformations.IMathTransform;

import java.util.List;

/**
 * The type Cassini soldner projection.
 */
public class CassiniSoldnerProjection extends MapProjection {
    // ReSharper disable InconsistentNaming
    private static final Double One6th = 0.16666666666666666666d;
    //C1
    private static final Double One120th = 0.00833333333333333333d;
    //C2
    private static final Double One24th = 0.04166666666666666666d;
    //C3
    private static final Double One3rd = 0.33333333333333333333d;
    //C4
    private static final Double One15th = 0.06666666666666666666d;
    //C5
    // ReSharper restore InconsistentNaming
    private final double _cFactor;
    private final double _m0;
    private final double _reciprocalSemiMajor;

    private final int maxIter = 10;
    private final double eps = 1e-11;

    /**
     * Instantiates a new Cassini soldner projection.
     *
     * @param parameters the parameters
     * @throws Exception the exception
     */
    public CassiniSoldnerProjection(List<ProjectionParameter> parameters) throws Exception {
        this(parameters, null);
    }

    /**
     * Instantiates a new Cassini soldner projection.
     *
     * @param parameters the parameters
     * @param inverse    the inverse
     * @throws Exception the exception
     */
    public CassiniSoldnerProjection(List<ProjectionParameter> parameters, CassiniSoldnerProjection inverse) throws Exception {
        super(parameters, inverse);
        setAuthority("EPSG");
        setAuthorityCode(9806);
        setName("Cassini_Soldner");
        _cFactor = _es / (1 - _es);
        _m0 = mlfn(lat_origin, Math.sin(lat_origin), Math.cos(lat_origin));
        _reciprocalSemiMajor = 1d / _semiMajor;
    }

    public IMathTransform inverse() throws Exception {
        if (_inverse == null)
            _inverse = new CassiniSoldnerProjection(_Parameters.toProjectionParameter(), this);

        return _inverse;
    }

    protected double[] radiansToMeters(double[] lonlat) throws Exception {
        double lambda = lonlat[0] - central_meridian;
        double phi = lonlat[1];
        double sinPhi, cosPhi;
        // sin and cos value
        sinPhi = Math.sin(phi);
        cosPhi = Math.cos(phi);
        double y = mlfn(phi, sinPhi, cosPhi);
        double n = 1.0d / Math.sqrt(1 - _es * sinPhi * sinPhi);
        double tn = Math.tan(phi);
        double t = tn * tn;
        double a1 = lambda * cosPhi;
        double a2 = a1 * a1;
        double c = _cFactor * Math.pow(cosPhi, 2.0d);
        double x = n * a1 * (1.0d - a2 * t * (One6th - (8.0d - t + 8.0d * c) * a2 * One120th));
        y -= _m0 - n * tn * a2 * (0.5d + (5.0d - t + 6.0d * c) * a2 * One24th);

        return lonlat.length == 2
                ? new double[]{_semiMajor * x, _semiMajor * y}
                : new double[]{_semiMajor * x, _semiMajor * y, lonlat[2]};
    }

    protected double[] metersToRadians(double[] p) throws Exception {
        double x = p[0] * _reciprocalSemiMajor;
        double y = p[1] * _reciprocalSemiMajor;
        double phi1 = phi1(_m0 + y);
        double tn = Math.tan(phi1);
        double t = tn * tn;
        double n = Math.sin(phi1);
        double r = 1.0d / (1.0d - _es * n * n);
        n = Math.sqrt(r);
        r *= (1.0d - _es) * n;
        double dd = x / n;
        double d2 = dd * dd;
        double phi = phi1 - (n * tn / r) * d2 * (.5 - (1.0 + 3.0 * t) * d2 * One24th);
        double lambda = dd * (1.0 + t * d2 * (-One3rd + (1.0 + 3.0 * t) * d2 * One15th)) / Math.cos(phi1);
        lambda = adjust_lon(lambda + central_meridian);

        return p.length == 2
                ? new double[]{lambda, phi}
                : new double[]{lambda, phi, p[2]};
    }

    private double phi1(Double arg) throws Exception {

        double k = 1.0d / (1.0d - _es);
        double phi = arg;
        for (int i = maxIter; i > 0; --i) {
            // rarely goes over 2 iterations
            double sinPhi = Math.sin(phi);
            double t = 1.0d - _es * sinPhi * sinPhi;
            t = (mlfn(phi, sinPhi, Math.cos(phi)) - arg) * (t * Math.sqrt(t)) * k;
            phi -= t;
            if (Math.abs(t) < eps)
                return phi;

        }
        throw new IllegalArgumentException("Convergence error.");
    }

}


