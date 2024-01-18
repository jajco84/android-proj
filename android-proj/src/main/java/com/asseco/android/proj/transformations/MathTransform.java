package com.asseco.android.proj.transformations;

import java.util.ArrayList;
import java.util.List;

/**
 * abstract class for creating multi-dimensional coordinate points transformations.
 *
 * If a client application wishes to query the source and target coordinate
 * systems of a transformation, then it should keep hold of the
 *
 * ICoordinateTransformation
 * interface, and use the contained
 * math transform object whenever it wishes to perform a transform.
 */
public abstract class MathTransform implements IMathTransform {
    /**
     * R2D
     */
    protected static final double R2D = 180 / Math.PI;
    /**
     * D2R
     */
    protected static final double D2R = Math.PI / 180;

    /**
     * To convert degrees to radians, multiply degrees by pi/180.
     *
     * @param deg the deg
     * @return the double
     * @throws Exception the exception
     */
    protected static double degrees2Radians(double deg) throws Exception {
        return (D2R * deg);
    }

    /**
     * Radians 2 degrees double.
     *
     * @param rad the rad
     * @return double
     * @throws Exception the exception
     */
    protected static double radians2Degrees(double rad) throws Exception {
        return (R2D * rad);
    }

    /**
     * Gets the dimension of input points.
     */
    public abstract int getDimSource() throws Exception;

    /**
     * Gets the dimension of output points.
     */
    public abstract int getDimTarget() throws Exception;

    /**
     * Gets transformed convex hull.
     * The supplied ordinates are interpreted as a sequence of points, which generates a convex
     * hull in the source space. The returned sequence of ordinates represents a convex hull in the
     * output space. The number of output points will often be different from the number of input
     * points. Each of the input points should be inside the valid domain (this can be checked by
     * testing the points' domain flags individually). However, the convex hull of the input points
     * may go outside the valid domain. The returned convex hull should contain the transformed image
     * of the intersection of the source convex hull and the source domain.A convex hull is a shape in a coordinate system, where if two positions A and B are
     * inside the shape, then all positions in the straight line between A and B are also inside
     * the shape. So in 3D a cube and a sphere are both convex hulls. Other less obvious examples
     * of convex hulls are straight lines, and single points. (A single point is a convex hull,
     * because the positions A and B must both be the same - i.e. the point itself. So the straight
     * line between A and B has zero length.)Some examples of shapes that are NOT convex hulls are donuts, and horseshoes.
     *  @param points
     *  @return
     */
  /*  public List<double> getCodomainConvexHull(List<double> points) throws Exception {
        throw new UnsupportedOperationException();
    }*/

    /**
     * Gets flags classifying domain points within a convex hull.
     *
     * The supplied ordinates are interpreted as a sequence of points, which
     * generates a convex hull in the source space. Conceptually, each of the
     * (usually infinite) points inside the convex hull is then tested against
     * the source domain. The flags of all these tests are then combined. In
     * practice, implementations of different transforms will use different
     * short-cuts to avoid doing an infinite number of tests.
     *
     *  @param points
     *  @return
     */
   /* public DomainFlags getDomainFlags(List<double> points) throws Exception {
        throw new UnsupportedOperationException();
    }*/

    /**
     * Tests whether this transform does not move any points.
     *
     * @return isidentity
     */
    public boolean identity() throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets a Well-Known text representation of this object.
     */
    public abstract String getWKT() throws Exception;

    /**
     * Gets an XML representation of this object.
     */
    public abstract String getXML() throws Exception;

    /*public IList<Coordinate> transformList(IList<Coordinate> points) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var"  result = new List<Coordinate>(points.Count);
        for (/* [UNSUPPORTED] 'var' as type is unsupported "var"  c : points)
            result.Add(Transform(c));
        return result;
    }*/

    /*public ICoordinate transform(ICoordinate coordinate) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var"  ret = Transform();
        coordinate.X = ret[0];
        coordinate.Y = ret[1];
        if (getDimTarget() > 2)
            coordinate.Z = ret[2];
         
        return coordinate;
    }*/

   /* public Coordinate transform(Coordinate coordinate) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var"  ordinates = getDimSource() == 2 ?  : ;
        /* [UNSUPPORTED] 'var' as type is unsupported "var"  ret = Transform(ordinates);
        return (getDimTarget() == 2) ? new Coordinate(ret[0], ret[1]) : new Coordinate(ret[0], ret[1], ret[2]);
    }*/

   /* public ICoordinateSequence transform(ICoordinateSequence coordinateSequence) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var"  clone = new Coordinate();
        for (/* [UNSUPPORTED] 'var' as type is unsupported "var"  i = 0;i < coordinateSequence.Count;i++)
        {
            clone.CoordinateValue = coordinateSequence.GetCoordinate(i);
            clone = Transform(clone);
            coordinateSequence.SetOrdinate(i, Ordinate.X, clone.X);
            coordinateSequence.SetOrdinate(i, Ordinate.Y, clone.Y);
            if (getDimTarget() > 2)
                coordinateSequence.SetOrdinate(i, Ordinate.Z, clone.Z);
             
        }
        return coordinateSequence;
    }*/

    /**
     * Gets the derivative of this transform at a point. If the transform does
     * not have a well-defined derivative at the point, then this function should
     * fail in the usual way for the DCP. The derivative is the matrix of the
     * non-translating portion of the approximate affine map at the point. The
     * matrix will have dimensions corresponding to the source and target
     * coordinate systems. If the input dimension is M, and the output dimension
     * is N, then the matrix will have size [M][N]. The elements of the matrix
     * {elt[n][m] : n=0..(N-1)} form a vector in the output space which is
     * parallel to the displacement caused by a small change in the m'th ordinate
     * in the input space.
     *
     * @param point point
     * @return derivative
     */
    public double[][] derivative(double[] point) throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates the inverse transform of this object.
     * This method may fail if the transform is not one to one. However, all cartographic projections should succeed.
     *
     * @return inverse transformation
     */
    public abstract IMathTransform inverse() throws Exception;

    /**
     * Transforms a coordinate point. The passed parameter point should not be modified.
     *
     * @param point point
     * @return transformed point
     */
    public abstract double[] transform(double[] point) throws Exception;

    /**
     * Transforms a list of coordinate point ordinal values.
     *
     * This method is provided for efficiently transforming many points. The supplied array
     * of ordinal values will contain packed ordinal values. For example, if the source
     * dimension is 3, then the ordinals will be packed in this order (x0,y0,z0,x1,y1,z1 ...).
     * The size of the passed array must be an integer multiple of DimSource. The returned
     * ordinal values are packed in a similar way. In some DCPs. the ordinals may be
     * transformed in-place, and the returned array may be the same as the passed array.
     * So any client code should not attempt to reuse the passed ordinal values (although
     * they can certainly reuse the passed array). If there is any problem then the server
     * implementation will throw an exception. If this happens then the client should not
     * make any assumptions about the state of the ordinal values.
     *
     * @param points points
     * @return transformed points
     */
    public List<double[]> transformList(List<double[]> points) throws Exception {
        List<double[]> result = new ArrayList<double[]>(points.size());
        for (double[] c : points)
            result.add(transform(c));
        return result;
    }

    /**
     * Reverses the transformation
     */
    public abstract void invert() throws Exception;

}


