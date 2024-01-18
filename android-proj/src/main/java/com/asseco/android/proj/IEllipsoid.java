package com.asseco.android.proj;


/**
 * The IEllipsoid interface defines the standard information stored with ellipsoid objects.
 */
public interface IEllipsoid extends IInfo {
    /**
     * Gets or sets the value of the semi-major axis.
     *
     * @return the semi major axis
     * @throws Exception the exception
     */
    double getSemiMajorAxis() throws Exception;

    /**
     * Sets semi major axis.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setSemiMajorAxis(double value) throws Exception;

    /**
     * Gets or sets the value of the semi-minor axis.
     *
     * @return the semi minor axis
     * @throws Exception the exception
     */
    double getSemiMinorAxis() throws Exception;

    /**
     * Sets semi minor axis.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setSemiMinorAxis(double value) throws Exception;

    /**
     * Gets or sets the value of the inverse of the flattening constant of the ellipsoid.
     *
     * @return the inverse flattening
     * @throws Exception the exception
     */
    double getInverseFlattening() throws Exception;

    /**
     * Sets inverse flattening.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setInverseFlattening(double value) throws Exception;

    /**
     * Gets or sets the value of the axis unit.
     *
     * @return the axis unit
     * @throws Exception the exception
     */
    ILinearUnit getAxisUnit() throws Exception;

    /**
     * Sets axis unit.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setAxisUnit(ILinearUnit value) throws Exception;

    /**
     * Is the Inverse Flattening definitive for this ellipsoid? Some ellipsoids use the
     * IVF as the defining value, and calculate the polar radius whenever asked. Other
     * ellipsoids use the polar radius to calculate the IVF whenever asked. This
     * distinction can be important to avoid floating-point rounding errors.
     *
     * @return the is ivf definitive
     * @throws Exception the exception
     */
    boolean getIsIvfDefinitive() throws Exception;

    /**
     * Sets is ivf definitive.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setIsIvfDefinitive(boolean value) throws Exception;

}


