package com.asseco.android.proj;

/**
 * The ILinearUnit interface defines methods on linear units.
 */
public interface ILinearUnit extends IUnit {
    /**
     * Gets or sets the number of meters per
     * {@link ILinearUnit}
     * .
     *
     * @return the meters per unit
     * @throws Exception the exception
     */
    double getMetersPerUnit() throws Exception;

    /**
     * Sets meters per unit.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setMetersPerUnit(double value) throws Exception;

}


