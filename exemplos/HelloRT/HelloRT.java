/**
 * Small demonstration of a periodic thread in Java:
 */

import javax.realtime.*;

public class HelloRT {

  public static void main(String [] args) {
    /* priority for new thread: min+10 */
    int pri = PriorityScheduler.instance().getMinPriority() + 10;
    PriorityParameters prip = new PriorityParameters(pri);

    /* period: 20ms */
    RelativeTime period = new RelativeTime(20 /* ms */, 0 /* ns */);

    /* release parameters for periodic thread: */
    PeriodicParameters perp = new PeriodicParameters(null, period, null, null, null, null);

    /* create periodic thread: */
    RealtimeThread rt = new RealtimeThread(prip, perp) {
        public void run() {
          int n=1;
          while (waitForNextPeriod() && (n<100)) {
              System.out.println("Hello "+n);
              n++;
            }
        }
      };

    /* start periodic thread: */
    rt.start();
  }

}
