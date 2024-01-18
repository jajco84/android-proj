package com.asseco.android.proj;

/**
 * A 2D coordinate system suitable for positions on the Earth's surface.
 */
public interface IHorizontalCoordinateSystem extends ICoordinateSystem {
    /**
     * Returns the HorizontalDatum.
     *
     * @return the horizontal datum
     * @throws Exception the exception
     */
    IHorizontalDatum getHorizontalDatum() throws Exception;

    /**
     * Sets horizontal datum.
     *
     * @param value the value
     * @throws Exception the exception
     */
    void setHorizontalDatum(IHorizontalDatum value) throws Exception;

}


