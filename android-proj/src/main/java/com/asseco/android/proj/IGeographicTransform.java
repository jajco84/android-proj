package com.asseco.android.proj;

import java.util.List;

/**
 * The IGeographicTransform interface is implemented on geographic transformation
 * objects and implements datum transformations between geographic coordinate systems.
 */
public interface IGeographicTransform extends IInfo {
    /**
     * Gets or sets source geographic coordinate system for the transformation.
     *
     * @return the source gcs
     * @throws Exception the exception
     */
    IGeographicCoordinateSystem getSourceGCS() throws Exception;

    /**
     * Sets source gcs.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setSourceGCS(IGeographicCoordinateSystem value) throws Exception;

    /**
     * Gets or sets the target geographic coordinate system for the transformation.
     *
     * @return the target gcs
     * @throws Exception the exception
     */
    IGeographicCoordinateSystem getTargetGCS() throws Exception;

    /**
     * Sets target gcs.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setTargetGCS(IGeographicCoordinateSystem value) throws Exception;

    /**
     * Returns an accessor interface to the parameters for this geographic transformation.
     *
     * @return the parameter info
     * @throws Exception the exception
     */
    IParameterInfo getParameterInfo() throws Exception;

    /**
     * Transforms an array of points from the source geographic coordinate system
     * to the target geographic coordinate system.
     *
     * @param points Points in the source geographic coordinate system
     * @return Points in the target geographic coordinate system
     * @throws Exception the exception
     */
    List<double[]> forward(List<double[]> points) throws Exception;

    /**
     * Transforms an array of points from the target geographic coordinate system
     * to the source geographic coordinate system.
     *
     * @param points Points in the target geographic coordinate system
     * @return Points in the source geographic coordinate system
     * @throws Exception the exception
     */
    List<double[]> inverse(List<double[]> points) throws Exception;

}


