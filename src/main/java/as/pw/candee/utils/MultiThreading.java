 package as.pw.candee.utils;
 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Executors;
 import java.util.concurrent.atomic.AtomicInteger;
 public class MultiThreading
 {
     public static void runAsync(Runnable task) {
         SERVICE.execute(task);
     }
     private static final AtomicInteger threadCounter = new AtomicInteger(0); static {
         SERVICE = Executors.newCachedThreadPool(task -> new Thread(task, "Candy Thread " + threadCounter.getAndIncrement()));
     }
     private static final ExecutorService SERVICE;
 }