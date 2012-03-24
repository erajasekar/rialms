/*
<LICENCE>

Copyright (c) 2008, University of Southampton
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

  * Redistributions of source code must retain the above copyright notice, this
	list of conditions and the following disclaimer.

  *	Redistributions in binary form must reproduce the above copyright notice,
	this list of conditions and the following disclaimer in the documentation
	and/or other materials provided with the distribution.

  *	Neither the name of the University of Southampton nor the names of its
	contributors may be used to endorse or promote products derived from this
	software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

</LICENCE>
*/

package com.rialms.assessment.test;


import org.qtitools.qti.node.test.AssessmentItemRef;
import org.qtitools.qti.node.test.ControlObject;

public class TimeReport implements Serializable {
    private static final long serialVersionUID = 1L;

    private int selectedItemsCount;
    private int remainingItemsCount;
    private long maximumTimeLimit;
    private long remainingMaximumTime;

    private TimeReport(int selectedItemsCount, int remainingItemsCount, long maximumTimeLimit, long remainingMaximumTime) {
        this.selectedItemsCount = selectedItemsCount;
        this.remainingItemsCount = remainingItemsCount;
        this.maximumTimeLimit = maximumTimeLimit;
        this.remainingMaximumTime = remainingMaximumTime;
    }

    public int getSelectedItemsCount() {
        return selectedItemsCount;
    }

    public int getRemainingItemsCount() {
        return remainingItemsCount;
    }

    public long getMaximumTimeLimit() {
        return maximumTimeLimit;
    }

    public long getRemainingMaximumTime() {
        return remainingMaximumTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("TimeReport(");
        builder.append("Items = ");
        builder.append(selectedItemsCount);
        builder.append("/");
        builder.append(remainingItemsCount);
        builder.append(", Time = ");
        builder.append(maximumTimeLimit);
        builder.append("/");
        builder.append(remainingMaximumTime);
        builder.append(")");

        return builder.toString();
    }

    public static TimeReport getInstance(ControlObject control) {
        if (control == null || control.getTimeLimit() == null || control.getTimeLimit().getMaximumMillis() == null)
            return null;

        long maximumTimeLimit = control.getTimeLimit().getMaximumMillis();

        long remainingMaximumTime = maximumTimeLimit - control.getDuration();
        if (remainingMaximumTime < 0)
            remainingMaximumTime = 0;

        int total = control.getTotalCount();
        int notFinished = total - control.getFinishedCount();

        TimeReport timeReport = new TimeReport
        (total
                , notFinished
                , maximumTimeLimit
                , remainingMaximumTime);

        return timeReport;
    }

    static Long getRemainingTime(ControlObject co) {
        Long maximum = null;
        if (co.getTimeLimit() != null && co.getTimeLimit().getMaximumMillis() != null)
            maximum = co.getTimeLimit().getMaximumMillis();

        return (maximum != null) ? new Long(maximum - co.getDuration()) : null;
    }

    public static ControlObject getLowestRemainingTimeControlObject(AssessmentItemRef air) {
        ControlObject lowest = null;
        ControlObject current = air;

        while (current != null) {
            Long remaining = getRemainingTime(current);
            if (remaining != null)
                if (lowest == null || getRemainingTime(lowest) > remaining)
                    lowest = current;

            current = current.getParent();
        }

        return lowest;
    }

    public static TimeReport getLowestRemainingTimeReport(AssessmentItemRef air) {
        return TimeReport.getInstance(getLowestRemainingTimeControlObject(air));
    }
}
