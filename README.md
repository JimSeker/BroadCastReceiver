BroadCastReceiver examples
=================
`eclipse/` is the examples for use in eclipse. This is no longer updated. The rest of the examples are in Android studio format.

`BroadCastDemo1`: Simple implementation of a receiver with a static and dynamic registered intent-filter

`BroadCastDemo2`: Setup to receive intents about battery status and power status.  it uses dynamic registered receivers to get the information.

`BroadcastNoti`: A reimplementation of the [notification demo](https://github.com/JimSeker/notifications), but using only receivers for the broadcast.

`BroadcastBoot`: Receives a broadcast on boot, that starts a alarm (since background services are no longer allowed in Oreo).

`BroadcastBoot2`: Receives a broadcast on boot, that starts a JobIntentService, which is allowed to run in the background.

---

These are example code for University of Wyoming, Cosc 4730 Mobile Programming course and cosc 4735 Advance Mobile Programing course. 
All examples are for Android.
