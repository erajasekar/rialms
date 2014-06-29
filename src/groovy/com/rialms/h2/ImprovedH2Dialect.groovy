package com.rialms.h2

import org.hibernate.dialect.H2Dialect

/**
 * Created by relango on 6/29/14.
 * This is workaround to fix org.hibernate.tool.hbm2ddl.SchemaExport - SchemaExport.java 425 - HHH000389:
 * Based on this suggestion http://stackoverflow.com/a/23865805
 */
public class ImprovedH2Dialect extends H2Dialect {
    @Override
    public String getDropSequenceString(String sequenceName) {
        // Adding the "if exists" clause to avoid warnings
        return "drop sequence if exists " + sequenceName;
    }

    @Override
    public boolean dropConstraints() {
        // We don't need to drop constraints before dropping tables, that just
        // leads to error messages about missing tables when we don't have a
        // schema in the database
        return false;
    }


}