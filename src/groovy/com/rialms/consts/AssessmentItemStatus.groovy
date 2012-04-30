package com.rialms.consts

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 4/12/12
 * Time: 11:18 PM
 * To change this template use File | Settings | File Templates.
 */
public enum AssessmentItemStatus {
    NOT_PRESENTED,
    PRESENTED,
    SKIPPED,
    TIMED_OUT,
    RESPONDED,
    SUBMITTED;

    public static String format(EnumSet<AssessmentItemStatus> statuses) {
        return statuses.collect {it.name()}.join(" , ");
    }

    public static String format(AssessmentItemStatus status) {
        return format(EnumSet.of(status));
    }
}
