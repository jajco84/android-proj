package com.asseco.android.proj.transformations;

import com.asseco.android.proj.ICoordinateSystem;

/**
 * Creates coordinate transformations.
 */
public interface ICoordinateTransformationFactory {
    /**
     * Creates a transformation between two coordinate systems.
     *
     * This method will examine the coordinate systems in order to construct
     * a transformation between them. This method may fail if no path between
     * the coordinate systems is found, using the normal failing behavior of
     * the DCP (e.g. throwing an exception).
     *
     * @param sourceCS Source coordinate system
     * @param targetCS Target coordinate system
     * @return coordinate transformation
     * @throws Exception the exception
     */
    ICoordinateTransformation createFromCoordinateSystems(ICoordinateSystem sourceCS, ICoordinateSystem targetCS) throws Exception;

}


