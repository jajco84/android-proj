# android-proj
android library for coordinate systems transformations

# usage

```
ICoordinateSystem cs1 = (ICoordinateSystem)CoordinateSystemWktReader.parse("GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]]");
ICoordinateSystem cs2 = (ICoordinateSystem)CoordinateSystemWktReader.parse("PROJCS[\"S-JTSK (Greenwich) / Krovak\",GEOGCS[\"S-JTSK (Greenwich)\",DATUM[\"S_JTSK_Greenwich\",SPHEROID[\"Bessel 1841\",6377397.155,299.1528128,AUTHORITY[\"EPSG\",\"7004\"]],TOWGS84[570.8,85.7,462.8,4.998,1.587,5.261,3.56],AUTHORITY[\"EPSG\",\"6818\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199432958,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4818\"]],PROJECTION[\"Krovak\"],PARAMETER[\"latitude_of_center\",49.5],PARAMETER[\"longitude_of_center\",24.83333333333333],PARAMETER[\"X_Scale\",-1],PARAMETER[\"Y_Scale\",1],PARAMETER[\"XY_Plane_Rotation\",90],PARAMETER[\"azimuth\",30.28813975277778],PARAMETER[\"pseudo_standard_parallel_1\",78.5],PARAMETER[\"scale_factor\",0.9999],PARAMETER[\"false_easting\",0],PARAMETER[\"false_northing\",0],UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],AUTHORITY[\"EPSG\",\"102067\"]]");

CoordinateTransformationFactory ctf = new CoordinateTransformationFactory();
ICoordinateTransformation ct = ctf.createFromCoordinateSystems(cs1, cs2);

GeoPoint point....
double[] result = ct.getMathTransform().transform(new double[]{point.getLongitude(), point.getLatitude()});
```
