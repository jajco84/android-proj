package com.asseco.android.proj.transformations;


import com.asseco.android.proj.IPrimeMeridian;

/**
 * Adjusts target Prime Meridian
 */
public class PrimeMeridianTransform extends MathTransform {
    private boolean _isinverted = false;
    private IPrimeMeridian _source;
    private IPrimeMeridian _target;

    /**
     * Creates instance prime meridian transform
     *
     * @param source the source
     * @param target the target
     * @throws Exception the exception
     */
    public PrimeMeridianTransform(IPrimeMeridian source, IPrimeMeridian target) throws Exception {
        super();
        if (!source.getangularUnit().equalParams(target.getangularUnit())) {
            throw new UnsupportedOperationException("The method or operation is not implemented.");
        }

        _source = source;
        _target = target;
    }

    /**
     * Gets a Well-Known text representation of this affine math transformation.
     */
    public String getWKT() throws Exception {
        throw new UnsupportedOperationException("The method or operation is not implemented.");
    }

    /**
     * Gets an XML representation of this affine transformation.
     */
    public String getXML() throws Exception {
        throw new UnsupportedOperationException("The method or operation is not implemented.");
    }

    /**
     * Gets the dimension of input points.
     */
    public int getDimSource() throws Exception {
        return 3;
    }

    /**
     * Gets the dimension of output points.
     */
    public int getDimTarget() throws Exception {
        return 3;
    }

    /**
     * Returns the inverse of this affine transformation.
     *
     * @return IMathTransform that is the reverse of the current affine transformation.
     */
    public IMathTransform inverse() throws Exception {
        return new PrimeMeridianTransform(_target, _source);
    }

    /**
     * Transforms a coordinate point. The passed parameter point should not be modified.
     *
     * @param point
     * @return
     */
    public double[] transform(double[] point) throws Exception {
        double[] transformed = new double[point.length];
        if (!_isinverted)
            transformed[0] = point[0] + _source.getLongitude() - _target.getLongitude();
        else
            transformed[0] = point[0] + _target.getLongitude() - _source.getLongitude();
        transformed[1] = point[1];
        if (point.length > 2)
            transformed[2] = point[2];

        return transformed;
    }

    /**
     * Reverses the transformation
     */
    public void invert() throws Exception {
        this._isinverted = !this._isinverted;
    }

}


