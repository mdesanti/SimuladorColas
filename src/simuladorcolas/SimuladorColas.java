package simuladorcolas;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SimuladorColas {

	// llega 1 cada 3 segundos
	private double arrivingLambda = 3.5;
	// atiendo a 1 en 2.5 segundos
	private double servingLambda = 2.5;
	private int serversQty = 1;
	private BlockingDeque<Waiter> deque = new LinkedBlockingDeque<>();
	private Distributions random = new Distributions();
	private AtomicLong waitingTime = new AtomicLong(0);
	private AtomicInteger servedClients = new AtomicInteger(0);
	
	public int getQueueLength() {
		return deque.size();
	}

	public void start() {
		ExecutorService executors = Executors.newCachedThreadPool();

		for (int i = 0; i < serversQty; i++) {
			executors.execute(new Runnable() {

				@Override
				public void run() {
					try {
						while (true) {
							Waiter client = deque.takeFirst();
							long wait = System.currentTimeMillis()
									- client.getStart();
							long servingTime = Math.round(random
									.nextExponential(servingLambda)*1000);
							Thread.sleep(servingTime);
							servedClients.addAndGet(1);
							waitingTime.addAndGet(wait + servingTime);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
		executors.execute(new Runnable() {

			@Override
			public void run() {
				try {
					while (true) {
						long next = Math.round(random
								.nextExponential(arrivingLambda)*1000);
						Thread.sleep(next);
						deque.addLast(new Waiter(System.currentTimeMillis()));
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}
	
}
