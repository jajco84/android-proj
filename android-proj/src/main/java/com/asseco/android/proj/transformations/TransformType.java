package com.asseco.android.proj.transformations;


/**
 * The enum Transform type.
 */
public enum TransformType {
    /**
     * Semantic type of transform used in coordinate transformation.
     *
     * Unknown or unspecified type of transform.
     */
    Other,
    /**
     * Transform depends only on defined parameters. For example, a cartographic projection.
     */
    Conversion,
    /**
     * Transform depends only on empirically derived parameters. For example a datum transformation.
     */
    Transformation,
    /**
     * Transform depends on both defined and empirical parameters.
     */
    ConversionAndTransformation
}

