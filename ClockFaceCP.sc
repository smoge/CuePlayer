/* 
  This class is modified from the ClockFace class created by Josh Parmenter.
  The original can be found at: https://github.com/supercollider-quarks/ClockFace 
*/

ClockFaceCP {
	var <window, <starttime, <tempo, <>inc, <>bounds, <cursecs, isPlaying = false, clock, timeString;
	var remFun;

	*new{ arg window, starttime = 0, tempo = 1, inc = 0.1, bounds = Rect(0, 0, 209, 20);
		^super.newCopyArgs(window, starttime, tempo, inc, bounds).init;
		}

	init {
		cursecs = starttime;
		this.digitalGUI;
		}

	play {
		var start;
		clock = TempoClock.new(tempo);
		start = clock.elapsedBeats;
		remFun = {this.stop};
		CmdPeriod.add(remFun);
		clock.sched(inc, {
			this.cursecs_(clock.elapsedBeats - start + starttime, false);
			inc;
			})
		}

	cursecs_ {arg curtime, updateStart = true;
		var curdisp;
		cursecs = curtime;
		curdisp = curtime.asTimeString.drop(1).drop(2);
		curdisp = curdisp[0 .. (curdisp.size-3)];
		updateStart.if({starttime = cursecs});
		{timeString.string_(curdisp)}.defer;
		}

	stop {
		starttime = cursecs;
		clock.clear;
		CmdPeriod.remove(remFun);
		clock.stop;
		}

	digitalGUI {
		//window = GUI.window.new("Digital Clock", Rect(10, 250, 450, 110)).front;
		timeString = GUI.staticText.new(window, bounds)
			.string_(cursecs.asTimeString.drop(1).drop(2))
		.font_(Font("Arial", 20))
		.align_(\center)
		.stringColor_(Color.white);

		/* window.onClose_({this.stop}); */
		}

}

