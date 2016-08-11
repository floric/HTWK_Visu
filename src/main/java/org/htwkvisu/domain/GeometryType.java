package org.htwkvisu.domain;

import org.hibernate.spatial.JTSGeometryType;
import org.hibernate.spatial.dialect.mysql.MySQLGeometryTypeDescriptor;

public class GeometryType extends JTSGeometryType {

    public GeometryType() {
        super(new MySQLGeometryTypeDescriptor());
    }
}
