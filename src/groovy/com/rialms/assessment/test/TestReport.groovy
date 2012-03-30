package com.rialms.assessment.test

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 3/29/12
 * Time: 10:50 PM
 * To change this template use File | Settings | File Templates.
 */
class TestReport {

    private String testTitle;

    private Map<String,String> summary;

    private List<Map<String,String>> detail;

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        TestReport that = (TestReport) o

        println "this detail ${detail} ====== other detail ${that.detail} ===== ${detail == that.detail}"
        println "this summary ${summary} ====== other summary ${that.summary} ===== ${summary == that.summary}"
        if (detail != that.detail) return false
        if (summary != that.summary) return false

        return true
    }

    int hashCode() {
        int result
        result = summary.hashCode()
        result = 31 * result + detail.hashCode()
        return result
    }


}
