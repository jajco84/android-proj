package com.asseco.android.proj;


/**
 * The IAngularUnit interface defines methods on angular units.
 */
public interface IAngularUnit extends IUnit {
    /**
     * Gets or sets the number of radians per angular unit.
     *
     * @return the radians per unit
     * @throws Exception the exception
     */
    double getRadiansPerUnit() throws Exception;

    /**
     * Sets radians per unit.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setRadiansPerUnit(double value) throws Exception;

}


