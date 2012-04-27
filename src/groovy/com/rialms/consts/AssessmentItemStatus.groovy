package com.rialms.consts

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 4/12/12
 * Time: 11:18 PM
 * To change this template use File | Settings | File Templates.
 */
//TODO move to consts
public enum AssessmentItemStatus {
    NOT_PRESENTED,
    PRESENTED,
    SKIPPED,
    TIMED_OUT,
    RESPONDED,
    SUBMITTED;

    //TODO : remove
    /* public static EnumSet<AssessmentItemStatus> getStatuses(boolean isPresented, boolean isSkipped, boolean isIimedOut, boolean isResponded) {
        EnumSet<AssessmentItemStatus> statuses = EnumSet.noneOf(AssessmentItemStatus.class);
        if (isPresented) {
            statuses.addAll(EnumSet.of(PRESENTED));
        }
        if (isSkipped) {
            statuses.addAll(EnumSet.of(SKIPPED));
        }
        if (isIimedOut) {
            statuses.addAll(EnumSet.of(TIMED_OUT));
        }
        if (isResponded) {
            statuses.addAll(EnumSet.of(RESPONDED));
        }
        return statuses
    }*/

    public static String format(EnumSet<AssessmentItemStatus> statuses) {
        return statuses.collect {it.name()}.join(" , ");
    }

    public static String format(AssessmentItemStatus status) {
        return format(EnumSet.of(status));
    }
}
