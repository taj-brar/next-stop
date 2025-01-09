package com.tb.nextstop.data

import com.tb.nextstop.ui.map.WPG_LAT
import com.tb.nextstop.ui.map.WPG_LON
import kotlinx.serialization.json.JsonPrimitive

val dummyStop = Stop(
    stopId = 19035,
    name = "Eastbound at Maple Wood and Ester Village",
    stopNumber = 19035,
)

val dummyRoutes = listOf(
    Route(
        key = JsonPrimitive("75"),
        number = JsonPrimitive("75"),
        badgeLabel = JsonPrimitive("75")
    ),
    Route(
        key = JsonPrimitive("67"),
        number = JsonPrimitive("67"),
        badgeLabel = JsonPrimitive("67")
    ),
    Route(
        key = JsonPrimitive("14"),
        number = JsonPrimitive("14"),
        badgeLabel = JsonPrimitive("14")
    ),
    Route(
        key = JsonPrimitive("16"),
        number = JsonPrimitive("16"),
        badgeLabel = JsonPrimitive("16")
    ),
    Route(
        key = JsonPrimitive("11"),
        number = JsonPrimitive("11"),
        badgeLabel = JsonPrimitive("11")
    ),
    Route(
        key = JsonPrimitive("BLUE"),
        number = JsonPrimitive("BLUE"),
        badgeLabel = JsonPrimitive("B")
    ),
    Route(
        key = JsonPrimitive("47"),
        number = JsonPrimitive("47"),
        badgeLabel = JsonPrimitive("47")
    ),
    Route(
        key = JsonPrimitive("676"),
        number = JsonPrimitive("676"),
        badgeLabel = JsonPrimitive("676")
    ),
    Route(
        key = JsonPrimitive("60"),
        number = JsonPrimitive("60"),
        badgeLabel = JsonPrimitive("60")
    ),
    Route(
        key = JsonPrimitive("54"),
        number = JsonPrimitive("54"),
        badgeLabel = JsonPrimitive("54")
    ),
)

val dummyFeatures = listOf(
    StopFeature(
        name = "Heated Shelter",
    ),
    StopFeature(
        name = "Unheated Shelter",
    ),
    StopFeature(
        name = "Bench",
    ),
    StopFeature(
        name = "BUSwatch Electronic Sign",
    )
)

val dummyVariant = Variant(
    key = "variant_key",
    name = "Selkirk-Osborne to Tyndall Park via Manitoba"
)

val dummyBus = Bus(
    key = 123,
    bikeRack = "true",
    wifi = "true"
)

val dummyScheduledStops = listOf(
    ScheduledStop(
        key = "test_key_1",
        cancelled = "false",
        times = Times(
            arrival = ScheduleTime(
                scheduled = "2025-01-01T01:00:00",
                estimated = "2025-01-01T01:01:00"
            ),
            departure = ScheduleTime(
                scheduled = "2025-01-01T01:00:00",
                estimated = "2025-01-01T01:01:00"
            )
        ),
        variant = dummyVariant,
        bus = dummyBus
    ),
    ScheduledStop(
        key = "test_key_2",
        cancelled = "false",
        times = Times(
            arrival = ScheduleTime(
                scheduled = "2025-01-01T02:00:00",
                estimated = "2025-01-01T02:01:00"
            ),
            departure = ScheduleTime(
                scheduled = "2025-01-01T02:00:00",
                estimated = "2025-01-01T02:01:00"
            )
        ),
        variant = dummyVariant,
        bus = dummyBus
    ),
    ScheduledStop(
        key = "test_key_3",
        cancelled = "false",
        times = Times(
            arrival = ScheduleTime(
                scheduled = "2025-01-01T03:00:00",
                estimated = "2025-01-01T03:01:00"
            ),
            departure = ScheduleTime(
                scheduled = "2025-01-01T03:00:00",
                estimated = "2025-01-01T03:01:00"
            )
        ),
        variant = dummyVariant,
        bus = dummyBus
    )
)

val dummyStopSchedule = StopSchedule(
    stop = dummyStop,
    routeSchedules = listOf(
        RouteSchedules(
            route = dummyRoutes[0],
            scheduledStops = dummyScheduledStops
        ),
        RouteSchedules(
            route = dummyRoutes[1],
            scheduledStops = dummyScheduledStops
        ),
        RouteSchedules(
            route = dummyRoutes[2],
            scheduledStops = dummyScheduledStops
        ),
        RouteSchedules(
            route = dummyRoutes[3],
            scheduledStops = dummyScheduledStops
        ),
        RouteSchedules(
            route = dummyRoutes[4],
            scheduledStops = dummyScheduledStops
        ),
        RouteSchedules(
            route = dummyRoutes[5],
            scheduledStops = dummyScheduledStops
        ),
    )
)

val dummyRouteScheduledStop = RouteScheduledStop(
    route = dummyRoutes[0],
    scheduledStop = dummyScheduledStops[0]
)

val dummyLiveScheduledStops = listOf(
    LiveTripScheduledStop(
        id = "test_id_1",
        tripId = "test_trip_id_1",
        stopId = "12345",
        name = "Eastbound Meadowood at Dakota",
        scheduledTime = "2025-01-01T01:00:00.000-06:00",
        estimatedTime = "2025-01-01T01:01:00.000-06:00",
        delayed = true,
        cancelled = false,
        position = LiveTripStopPosition(
            lat = WPG_LAT,
            lng = WPG_LON
        )
    ),
    LiveTripScheduledStop(
        id = "test_id_2",
        tripId = "test_trip_id_2",
        stopId = "12345",
        name = "Northbound Dakota at St. Vital Centre",
        scheduledTime = "2025-01-01T02:00:00.000-06:00",
        estimatedTime = "2025-01-01T02:01:00.000-06:00",
        delayed = true,
        cancelled = false,
        position = LiveTripStopPosition(
            lat = WPG_LAT,
            lng = WPG_LON
        )
    ),
    LiveTripScheduledStop(
        id = "test_id_3",
        tripId = "test_trip_id_3",
        stopId = "12345",
        name = "Northbound Dakota at Abinojii Mikanah South",
        scheduledTime = "2025-01-01T03:00:00.000-06:00",
        estimatedTime = "2025-01-01T01:01:00.000-06:00",
        delayed = true,
        cancelled = false,
        position = LiveTripStopPosition(
            lat = WPG_LAT,
            lng = WPG_LON
        )
    ),
    LiveTripScheduledStop(
        id = "test_id_4",
        tripId = "test_trip_id_4",
        stopId = "12345",
        name = "Northbound Dakota at Abinojii Mikanah",
        scheduledTime = "2025-01-01T04:00:00.000-06:00",
        estimatedTime = "2025-01-01T04:01:00.000-06:00",
        delayed = true,
        cancelled = false,
        position = LiveTripStopPosition(
            lat = WPG_LAT,
            lng = WPG_LON
        )
    ),
    LiveTripScheduledStop(
        id = "test_id_5",
        tripId = "test_trip_id_5",
        stopId = "12345",
        name = "Northbound Dakota at Beliveau",
        scheduledTime = "2025-01-01T05:00:00.000-06:00",
        estimatedTime = "2025-01-01T05:01:00.000-06:00",
        delayed = true,
        cancelled = false,
        position = LiveTripStopPosition(
            lat = WPG_LAT,
            lng = WPG_LON
        )
    ),
)

val dummyBusId = "888"

val dummyLiveRoute = LiveRoute(
    id = "test_id",
    name = "Selkirk-Osborne",
    label = "Selkirk-Osborne",
    badgeLabel = "16"
)

val dummyBusFeatures = BusFeatures(
    easyAccess = true,
    counter = true,
    bikeRack = true,
    wifi = true,
    wheelchairSecurement = true
)