package com.sdrak.netcore;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPools
{
	private final ScheduledThreadPoolExecutor _generalScheduledThreadPool;
//	private final ThreadPoolExecutor _ioPacketsThreadPool;
//	private final ThreadPoolExecutor _generalThreadPool;

	protected ThreadPools()
	{
		_generalScheduledThreadPool = new ScheduledThreadPoolExecutor(1, new PriorityThreadFactory("GeneralSTPool", Thread.NORM_PRIORITY));
//		_ioPacketsThreadPool = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory("I/O Packet Pool", Thread.NORM_PRIORITY + 1));
//		_generalThreadPool = new ThreadPoolExecutor(1, 2, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory("General Pool", Thread.NORM_PRIORITY));
	}

	private static class PriorityThreadFactory implements ThreadFactory
	{
		private final int _prio;
		private final String _name;
		private final AtomicInteger _threadNumber = new AtomicInteger(1);
		private final ThreadGroup _group;

		public PriorityThreadFactory(String name, int prio)
		{
			_prio = prio;
			_name = name;
			_group = new ThreadGroup(_name);
		}

		@Override
		public Thread newThread(Runnable r)
		{
			Thread t = new Thread(_group, r, _name + "-" + _threadNumber.getAndIncrement());
			t.setPriority(_prio);
			return t;
		}

	}

	public ScheduledFuture<?> scheduleGeneral(Runnable task, long delay, TimeUnit unit)
	{
		try
		{
			return _generalScheduledThreadPool.schedule(task, delay, unit);
		}
		catch (RejectedExecutionException e)
		{
			return null; /* shutdown, ignore */
		}
	}

	public ScheduledFuture<?> scheduleGeneral(Runnable task, long delay)
	{
		return scheduleGeneral(task, delay, TimeUnit.MILLISECONDS);
	}
	
	public ScheduledFuture<?> scheduleGeneral(Runnable task)
	{
		return scheduleGeneral(task, 0);
	}

	public ScheduledFuture<?> scheduleGeneralAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit)
	{
		try
		{
			return _generalScheduledThreadPool.scheduleAtFixedRate(task, initialDelay, period, unit);
		}
		catch (RejectedExecutionException e)
		{
			return null; /* shutdown, ignore */
		}
	}

	private static class InstanceHolder
	{
		private static final ThreadPools _instance = new ThreadPools();
	}

	public static ThreadPools getInstance()
	{
		return InstanceHolder._instance;
	}
}
