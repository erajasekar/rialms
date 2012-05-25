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
    SUBMITTED;//NOT USED

    private static final String BADGE = "badge";

    private static EnumMap<AssessmentItemStatus, String> statusClass = new EnumMap<AssessmentItemStatus, String>(AssessmentItemStatus.class);

    static {
        statusClass[NOT_PRESENTED] = "badge-info";
        statusClass[PRESENTED] = "badge-warning";
        statusClass[SKIPPED] = "badge-important";
        statusClass[TIMED_OUT] = "";
        statusClass[RESPONDED] = "badge-success";
    }

    public static String format(EnumSet<AssessmentItemStatus> statuses) {
        return statuses.collect {it.name().replaceAll("_", " ").toLowerCase().capitalize()}.join(" , ");
    }

    public static String format(AssessmentItemStatus status) {
        return format(EnumSet.of(status));
    }

    public String getStatusClass() {
        return "${BADGE} ${statusClass[this]}";
    }
}
