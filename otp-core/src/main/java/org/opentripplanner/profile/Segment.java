package org.opentripplanner.profile;

import java.util.Collections;
import java.util.List;

import lombok.Getter;

import com.beust.jcommander.internal.Lists;

public class Segment {

    public static class SegmentPattern implements Comparable<SegmentPattern> {
        public String patternId;
        public int fromIndex;
        public int toIndex;
        public int nTrips;
        //public Stats stats;
        public SegmentPattern (PatternRide patternRide) {
            this.patternId = patternRide.pattern.getCode();
            this.fromIndex = patternRide.fromIndex;
            this.toIndex   = patternRide.toIndex;
            this.nTrips    = patternRide.pattern.getNumScheduledTrips();
            //this.stats     = patternRide.stats;
        }
        @Override
        public int compareTo (SegmentPattern other) {
            return other.nTrips - this.nTrips;
        }
    }
    
    @Getter int walkTime;
    @Getter Stats waitStats;

    @Getter String route;
    @Getter String from;
    @Getter String to;
    @Getter String fromName;
    @Getter String toName;
    @Getter String routeShortName;
    @Getter String routeLongName;
    @Getter Stats rideStats;
    @Getter List<SegmentPattern> segmentPatterns = Lists.newArrayList();

    public Segment (Ride ride, TimeWindow window) {
        route = ride.route.getId().getId();
        routeShortName = ride.route.getShortName();
        routeLongName = ride.route.getLongName();
        from = ride.from.getId().getId();
        to = ride.to.getId().getId();
        fromName = ride.from.getName();
        toName = ride.to.getName();
        rideStats = ride.getStats();
        for (PatternRide patternRide : ride.patternRides) {
            segmentPatterns.add(new SegmentPattern(patternRide));
        }
        Collections.sort(segmentPatterns);
        walkTime = (int) (ride.getTransferDistance() / ProfileRouter.WALK_SPEED);
        /* At this point we know all patterns on rides. Calculate transfer time information. */
        waitStats = ride.statsForTransfer (window);
    }

}